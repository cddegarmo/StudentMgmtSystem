package appclasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
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
   private static final int MAX_STUDENTS = 2000;
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

      private StudentFormatter() {
         resource = ResourceBundle.getBundle("appclasses.students");
         config = ResourceBundle.getBundle("appclasses.config");
         studentFormat = new MessageFormat(config.getString("student.data"));
         courseFormat  = new MessageFormat(config.getString("course.data"));
         dataFolder = Path.of(config.getString("data.folder"));
      }

      private String formatStudent(Student student) {
         return MessageFormat.format(resource.getString("student.format"),
                                     student.getLastName(),
                                     student.getFirstName(),
                                     student.getYearName(student.getYear()),
                                     student.getTuitionBalance());
      }
   }

   public  final Map<Student, List<Course>> students = new HashMap<>();
   private final StudentFormatter sf = new StudentFormatter();

   private StudentManager() {}

   public static StudentManager getInstance() {
      return sm;
   }

   public static int getMaxStudents()   { return MAX_STUDENTS;  }
   public static int getNumOfStudents() { return numOfStudents; }
   public static int getCourseFee()     { return COURSE_FEE;    }

   private static int generateId() {
      return numOfStudents + 1000;
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
                     .map(s -> sf.formatStudent(s))
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
           new FileReader(String.valueOf(sf.dataFolder.resolve("students.csv"))))) {
         String line  = null;
         while ((line = in.readLine()) != null) {
            addStudent(parseStudent(line));
         }
      } catch (IOException e) {
         logger.log(Level.SEVERE, "Error loading students " + e.getMessage(), e);
      }
   }

//   public void loadStudent() {
//      try (var in = new BufferedReader(
//           new FileReader(String.valueOf(sf.dataFolder.resolve("student.csv"))))) {
//         String line  = null;
//         while ((line = in.readLine()) != null) {
//            addStudent(parseStudent(line));
//         }
//      } catch (IOException e) {
//         logger.log(Level.SEVERE, "Error loading student " + e.getMessage(), e);
//      }
//   }

   public Student findStudent(int id) throws StudentManagerException {
      return students.keySet()
                     .stream()
                     .filter(s -> s.getId() == id)
                     .findFirst()
                     .orElseThrow(() -> new StudentManagerException("Student of id " + id + "not found."));
   }
}

