import java.util.Scanner;
import java.lang.Exception;

public class Student {
    private static final int MAX_STUDENTS = 8000;
    private static int numOfStudents = 0;
    private final String firstName;
    private final String lastName;
    private int studentID;
    private final String year;
    private String courses = "";
    private int tuitionBalance = 0;
    private static final int courseFee = 600;
    private static int id = 1000;

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
        this.studentID = id * 31;
        id++;
    }

    protected void enroll() {
        do {
            System.out.print("Enter course to enroll (Q to quit): ");
            Scanner in = new Scanner(System.in);
            String course = in.nextLine();
            if ( !course.matches( "[Qq]" ) ) {
                courses += "\n" + course;
                tuitionBalance += courseFee;
            } else {
                break;
            }
        } while( 1 != 0 );
        System.out.println( "Enrolled in: " + courses );
    }

    protected void viewBalance() {
        System.out.println( "Your balance is $" + tuitionBalance );
    }

    protected void payTuition() {
        viewBalance();
        System.out.print( "Enter your payment: " );
        Scanner in = new Scanner( System.in );
        int payment = in.nextInt();
        tuitionBalance -= payment;
        System.out.println( "Thank you for your payment of $" + payment );
        viewBalance();
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName +
                "\nStudent ID number is " + studentID +
                "\nCourses enrolled: " + courses +
                "\nTuition balance: $" + tuitionBalance;
    }

    public static int getNumOfStudents(){
        return numOfStudents;
    }
}
