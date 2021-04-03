import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Student implements Comparable<Student> {

    private final String firstName;
    private final String lastName;
    private final int year;
    private int studentID;
    private final Map<Integer, List<String>> courses = new HashMap<>();
    private int tuitionBalance = 0;
    
    Student(String first, String last, int year) {
        firstName = first;
        lastName = last;
        this.year = year;
    }

    public String getFirstName()   { return firstName;      }
    public String getLastName()    { return lastName;       }
    public int getYear()           { return year;           }
    public int getTuitionBalance() { return tuitionBalance; }

    public void setStudentID(int value) {
        studentID = value;
    }

    public void enroll(Integer year, List<String> courses) {
        this.courses.putIfAbsent(year, courses);
    }

    public void setTuitionBalance(int balance) {
        tuitionBalance = balance;
    }

    public void payTuition(int deposit) {
        tuitionBalance -= deposit;
    }

    String getYearName(int year) {
        String[] years = {"Freshman", "Sophomore", "Junior", "Senior"};
        return years[year - 1];
    }

    public int compareTo(Student s) {
        return lastName.compareTo(s.lastName);
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Student))
            return false;
        Student s = (Student) o;
        return s.firstName.equals(firstName) && s.lastName.equals(lastName) &&
             s.year == year;
    }

    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + Integer.hashCode(year);
        return result;
    }

    public String toString() {
        return String.format("%n%s, %s : %s",
                             lastName, firstName, getYearName(year));
    }
}
