package appclasses;

public class Course implements Comparable<Course> {
   private int courseNumber;
   private String courseName;

   public Course(int number, String name) {
      courseNumber = number;
      courseName   = name;
   }

   public int getCourseNumber()  { return courseNumber; }
   public String getCourseName() { return courseName;   }

   @Override
   public String toString() {
      return String.format("%s: %s%n", courseNumber, courseName);
   }

   public int compareTo(Course c) {
      int result = Integer.compare(courseNumber, c.getCourseNumber());
      if (result == 0)
         result = courseName.compareTo(c.getCourseName());
      return result;
   }
}
