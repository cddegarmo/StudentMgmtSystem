package appclasses;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StudentManager {
   // Enforce singleton
   private static final StudentManager sm = new StudentManager();

   // Keep track of total enrollment
   private static final int MAX_STUDENTS = 999;
   private static int numOfStudents      = 0;
   private static final int COURSE_FEE   = 600;
   private static final Logger logger    = Logger.getLogger(Student.class.getName());

   // Nested class for formatting student input/output
   private static class StudentFormatter {
      private ResourceBundle resource;
      private ResourceBundle config;
      private MessageFormat studentFormat;
      private MessageFormat courseFormat;
      private Path dataFolder;
      private Path studentsFile;
      private Path paymentsFile;

      private StudentFormatter() {
         resource = ResourceBundle.getBundle("appclasses.students");
         config   = ResourceBundle.getBundle("appclasses.config");
         studentFormat = new MessageFormat(config.getString("student.data"));
         courseFormat  = new MessageFormat(config.getString("course.data"));
         dataFolder    = Path.of(config.getString("data.folder"));
         studentsFile  = dataFolder.resolve(config.getString("students.file"));
         paymentsFile  = dataFolder.resolve(config.getString("payments.file"));
      }

      private String formatStudent(Student student) {
         return MessageFormat.format(resource.getString("student.format"),
                                     student.getLastName(),
                                     student.getFirstName(),
                                     student.getYearName(student.getYear()),
                                     student.getTuitionBalance());
      }
   }

   private final Map<Student, List<Course>> students = new HashMap<>();
   private final StudentFormatter sf = new StudentFormatter();

   private StudentManager() {}

   public static StudentManager getInstance() {
      return sm;
   }

   public static int getMaxStudents()   { return MAX_STUDENTS;  }
   public static int getNumOfStudents() { return numOfStudents; }
   public static int getCourseFee()     { return COURSE_FEE;    }

   private static int generateId() {
      return 891_000 + numOfStudents;
   }

   private void addStudent(Student student) {
      if (numOfStudents < MAX_STUDENTS) {
         students.putIfAbsent(student, new ArrayList<>());
         numOfStudents++;
      } else
         throw new IllegalStateException("Max students reached. Push enrollment to following year.");
   }

   public String printStudents() {
      return students.keySet()
                     .stream()
                     .sorted()
                     .map(s -> s.getId() + " " + sf.formatStudent(s))
                     .collect(
                          Collectors.joining("\n"));
   }

   private Student parseStudent(String text) {
      Student student = null;
      try {
         Object[] values  = sf.studentFormat.parse(text);
         String firstName = (String) values[0];
         String lastName  = (String) values[1];
         int year = Integer.parseInt((String) values[2]);
         student  = new Student(generateId(), firstName, lastName, year);
      } catch (ParseException e) {
         logger.log(Level.WARNING, "Error parsing student " + e.getMessage(), e);
      }
      return student;
   }

   private Course parseCourse(String text) {
      Course course = null;
      try {
         Object[] values = sf.courseFormat.parse(text);
         int number = Integer.parseInt((String) values[0]);
         String name = (String) values[1];
         course = new Course(number, name);
      } catch (ParseException e) {
         logger.log(Level.WARNING, "Error parsing course " + e.getMessage(), e);
      }
      return course;
   }

   public void loadStudents() {
      try (var in = new BufferedReader(
           new FileReader(String.valueOf(sf.studentsFile)))) {
         String line  = null;
         while ((line = in.readLine()) != null) {
            addStudent(parseStudent(line));
         }
      } catch (IOException e) {
         logger.log(Level.SEVERE, "Error loading students " + e.getMessage(), e);
      }
   }

   public void createCourseFiles() {
      for (Student student : students.keySet()) {
         Path courseFile = sf.dataFolder.resolve(MessageFormat.format(
              sf.config.getString("courses.file"), student.getId()));
         try (var out = new PrintWriter(new OutputStreamWriter(
              Files.newOutputStream(courseFile, StandardOpenOption.CREATE), "UTF-8"))) {
            out.append(System.lineSeparator());
         } catch (UnsupportedEncodingException e) {
            logger.log(Level.WARNING, "Error with encoding " + e.getMessage(), e);
         } catch (IOException e) {
            logger.log(Level.WARNING, "Error with output " + e.getMessage(), e);
         }
      }
   }

   public Student findStudent(int id) throws StudentManagerException {
      return students.keySet()
                     .stream()
                     .filter(s -> s.getId() == id)
                     .findFirst()
                     .orElseThrow(() -> new StudentManagerException("Student of id " + id + "not found."));
   }
}

