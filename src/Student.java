import java.util.Scanner;

public class Student {
    private static final int MAX_STUDENTS = 8000;
    private static int numOfStudents = 0;
    private static final int COURSE_FEE = 600;
    private static final int ID = 1000;

    private final String firstName;
    private final String lastName;
    private final String year;
    private int studentID;
    private final StringBuilder courses = new StringBuilder();
    private int tuitionBalance = 0;
    
    private Student() {
        Scanner in = new Scanner( System.in );
        System.out.print( "Enter student first name: " );
        this.firstName = in.nextLine();
        System.out.print( "Enter student last name: ");
        this.lastName = in.nextLine();
        System.out.print( "Enter student class level (i.e. freshman, sophomore...): ");
        this.year = in.nextLine();
        this.setStudentID();
        System.out.println( "New student added: " + firstName + " " + lastName + " " + year.toUpperCase() +
                " " + this.studentID );
        numOfStudents++;
    }

    public static Student addNewStudent() throws IllegalStateException {
            if( numOfStudents < MAX_STUDENTS )
                return new Student();
            else {
                throw new IllegalStateException( "Max students reached. Please push enrollment to next year and" +
                        " notify student." );
            }
    }

    private void setStudentID() {
        this.studentID = ID + numOfStudents;
    }

    public void enroll() {
        do {
            System.out.print("Enter course to enroll (Q to quit): ");
            Scanner in = new Scanner(System.in);
            String course = in.nextLine();
            if ( !course.matches( "[Qq]" ) ) {
                courses.append("\n");
                courses.append(course);
                tuitionBalance += COURSE_FEE;
            } else {
                break;
            }
        } while(1 != 0);
        System.out.println( "Enrolled in: " + courses.toString() );
    }

    public void payTuition() {
        viewBalance();
        System.out.print( "Enter your payment: " );
        Scanner in = new Scanner( System.in );
        int payment = in.nextInt();
        tuitionBalance -= payment;
        System.out.println( "Thank you for your payment of $" + payment );
        viewBalance();
    }

    public static int getNumOfStudents() {return numOfStudents;       }
    public static int getMaxStudents()   { return MAX_STUDENTS;       }
    public static int getCourseFee()     { return COURSE_FEE;         }

    public String getFirstName()         { return firstName;          }
    public String getLastName()          { return lastName;           }
    public String getYear()              { return year;               }
    public int getStudentID()            { return studentID;          }
    public String getCourses()           { return courses.toString(); }
    public int getTuitionBalance()       { return tuitionBalance;     }

    public void viewBalance() {
        System.out.println( "Student's balance is $" + tuitionBalance );
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof Student))
            return false;
        Student st = (Student) o;
        return st.getFirstName().equals(this.getFirstName()) &&
                st.getLastName().equals(this.getLastName()) &&
                st.getStudentID() == this.getStudentID();
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + Integer.hashCode(studentID);
        return result;
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName +
                "\nStudent ID number is " + studentID +
                "\nCourses enrolled: " + courses +
                "\nTuition balance: $" + tuitionBalance;
    }
}
