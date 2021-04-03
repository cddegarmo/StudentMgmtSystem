package appclasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StudentManager {
   // Enforce singleton
   private static final StudentManager sm = new StudentManager();

   // Keep track of total enrollment
   private static final int MAX_STUDENTS = 2000;
   private static int numOfStudents = 0;
   private static final int COURSE_FEE = 600;
   private static int id = 1000;
   private static final Logger logger = Logger.getLogger(Student.class.getName());

   // Nested class for formatting student output
   private static class StudentFormatter {
      private ResourceBundle resource;
      private ResourceBundle config;
      private MessageFormat studentFormat;
      private Path dataFolder;

      private StudentFormatter() {
         resource = ResourceBundle.getBundle("appclasses.students");
         config = ResourceBundle.getBundle("appclasses.config");
         studentFormat = new MessageFormat(config.getString("student.data"));
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

   public final List<Student> students = new ArrayList<>();
   private final StudentFormatter sf = new StudentFormatter();

   private StudentManager() {}

   public static StudentManager getInstance() {
      return sm;
   }

   public int getMaxStudents()   { return MAX_STUDENTS;  }
   public int getNumOfStudents() { return numOfStudents; }
   public int getCourseFee()     { return COURSE_FEE;    }

   public String printStudents() {
      return students.stream()
                     .sorted()
                     .map(s -> sf.formatStudent(s))
                     .collect(
                          Collectors.joining("\n"));
   }

   private Student parseStudent(String text) {
      Student student = null;
      try {
         Object[] values = sf.studentFormat.parse(text);
         String firstName = (String) values[0];
         String lastName = (String) values[1];
         int year = Integer.parseInt((String) values[2]);
         student = new Student(firstName, lastName, year, ++id);
      } catch (ParseException e) {
         logger.log(Level.WARNING, "Error parsing student " + e.getMessage(), e);
      }
      return student;
   }

   public void loadStudents() {
      try (var in = new BufferedReader(
           new FileReader(String.valueOf(sf.dataFolder.resolve("students.csv"))))) {
         String line = null;
         while ((line = in.readLine()) != null) {
            students.add(parseStudent(line));
            numOfStudents++;
         }
      } catch (IOException e) {
         logger.log(Level.SEVERE, "Error loading students " + e.getMessage(), e);
      }
   }

   public void loadStudent() {
      try (var in = new BufferedReader(
           new FileReader(String.valueOf(sf.dataFolder.resolve("student.csv"))))) {
         String line = null;
         while ((line = in.readLine()) != null) {
            students.add(parseStudent(line));
            numOfStudents++;
         }
      } catch (IOException e) {
         logger.log(Level.SEVERE, "Error loading student " + e.getMessage(), e);
      }
   }
}

