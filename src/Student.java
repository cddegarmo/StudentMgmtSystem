import java.util.Scanner;

class Student {
    private String firstName;
    private String lastName;
    private int studentID;
    private String year;
    private String courses = "";
    private int tuitionBalance = 0;
    private static final int courseFee = 600;
    private static int id = 1000;

    // Private constructor forces public static factory
    private Student() {
        Scanner in = new Scanner( System.in );
        System.out.print( "Enter student first name: " );
        this.firstName = in.nextLine();
        System.out.print( "Enter student last name: ");
        this.lastName = in.nextLine();
        System.out.print( "Enter student class level (i.e. freshman, sophomore...): ");
        this.year = in.nextLine();
        setStudentID();
        System.out.println( "New student added: " + firstName + " " + lastName + " " + year.toUpperCase() +
                " " + studentID );
    }

    // Static factory provides only access to class instantiation
    public static Student addNewStudent() {
        return new Student();
    }

    // Generate a student ID
    private void setStudentID() {
        id++;
        this.studentID = id * 31;
    }

    // Enroll in courses
    void enroll() {
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

    // View balance and pay tuition
    void viewBalance() {
        System.out.println( "Your balance is $" + tuitionBalance );
    }

    void payTuition() {
        viewBalance();
        System.out.print( "Enter your payment: " );
        Scanner in = new Scanner( System.in );
        int payment = in.nextInt();
        tuitionBalance -= payment;
        System.out.println( "Thank you for your payment of $" + payment );
        viewBalance();
    }

    // Overridden toString method to return concise summary of Student
    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName +
                "\nStudent ID number is " + studentID +
                "\nCourses enrolled: " + courses +
                "\nTuition balance: $" + tuitionBalance;
    }
}
