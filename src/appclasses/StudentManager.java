package appclasses;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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
      private final ResourceBundle resource;
      private final ResourceBundle config;
      private final MessageFormat studentFormat;
      private final MessageFormat courseFormat;
      private final MessageFormat paymentFormat;
      private final Path dataFolder;

      private StudentFormatter() {
         resource = ResourceBundle.getBundle("appclasses.students");
         config   = ResourceBundle.getBundle("appclasses.config");
         studentFormat = new MessageFormat(config.getString("student.data"));
         courseFormat  = new MessageFormat(config.getString("course.data"));
         paymentFormat = new MessageFormat(config.getString("payment.data"));
         Path current  = Path.of("").toAbsolutePath();
         dataFolder    = current.resolve(config.getString("data.folder"));
      }

      private String formatStudent(Student student) {
         return MessageFormat.format(resource.getString("student.format"),
                                     student.getLastName(),
                                     student.getFirstName(),
                                     student.getYearName(student.getYear()),
                                     student.getTuitionBalance());
      }
   }

   private Map<Student, List<Course>> students = new HashMap<>();
   private final StudentFormatter sf = new StudentFormatter();

   // Enforce static factory, prohibit subclassing
   private StudentManager() {}

   public static StudentManager getInstance() {
      return sm;
   }

   public static int getMaxStudents()   { return MAX_STUDENTS;  }
   public static int getNumOfStudents() { return numOfStudents; }
   public static int getCourseFee()     { return COURSE_FEE;    }
   
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
         int id = Integer.parseInt((String) values[0]);
         String firstName = (String) values[1];
         String lastName  = (String) values[2];
         int year = Integer.parseInt((String) values[3]);
         if (numOfStudents < MAX_STUDENTS) {
            student = new Student(id, firstName, lastName, year);
            numOfStudents++;
         }
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

   private int parsePayment(String text) {
      int payment = 0;
      try {
         Object[] values = sf.paymentFormat.parse(text);
         payment = Integer.parseInt((String) values[0]);
      } catch (ParseException e) {
         logger.log(Level.WARNING, "Error parsing payment " + e.getMessage(), e);
      }
      return payment;
   }

   private Student loadStudent(Path file) {
      Student student = null;
      try {
         student = parseStudent(Files.lines(
              sf.dataFolder.resolve(file), StandardCharsets.UTF_8)
                               .findFirst()
                               .orElseThrow());
      } catch (IOException e) {
         logger.log(Level.SEVERE, "Error loading student " + e.getMessage(), e);
      }
      return student;
   }

   private List<Course> loadCourses(Student student) {
      List<Course> courses = null;
      Path file = sf.dataFolder.resolve(MessageFormat.format(
           sf.config.getString("courses.file"), student.getId()));
      try {
         courses = Files.lines(file, StandardCharsets.UTF_8)
                        .map(line -> parseCourse(line))
                        .filter(course -> course != null)
                        .collect(Collectors.toList());
      } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading courses " + e.getMessage(), e);
      }
      return courses;
   }

   private int loadPayment(Student student) {
      int payment = 0;
      Path file = sf.dataFolder.resolve(MessageFormat.format(
           sf.config.getString("payment.file"), student.getId()));
      try {
         payment = Files.lines(file, StandardCharsets.UTF_8)
                        .map(x -> parsePayment(x))
                        .findFirst()
                        .orElseThrow();
      } catch (IOException e) {
         logger.log(Level.SEVERE, "Error loading payment " + e.getMessage(), e);
      }
      return payment;
   }

   // This method is fairly complex. It collects the students and their courses into
   // a map. The method calls within proceed as follows:
   // loadStudent() -> parseStudent() -> constructor
   // loadCourses() -> parseCourse() -> constructor
   public void loadData() {
      try {
         students = Files.list(sf.dataFolder)
                         .filter(file -> file.getFileName().toString().startsWith("student891"))
                         .map(file -> loadStudent(file))
                         .filter(Objects::nonNull)
                         .collect(Collectors.toMap(student -> student,
                                                   student -> loadCourses(student)));
         processTuition();
      } catch (IOException e) {
         logger.log(Level.SEVERE, "Error loading all data " + e.getMessage(), e);
      }
   }

   private void processTuition() {
      for (var student : students.keySet()) {
         int tuition = students.get(student).size() * COURSE_FEE;
         student.setTuitionBalance(tuition);
         student.payTuition(loadPayment(student));
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

