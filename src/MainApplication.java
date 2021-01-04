import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MainApplication {
    public static void main( String[] args ){
        System.out.print( "Enter number of new students to enroll:" );
        Scanner in = new Scanner( System.in );
        int numStudents = in.nextInt();
        List<Student> students = new ArrayList<>();

        for( int i = 0; i < numStudents; i++ ){
            students.add( Student.addNewStudent());
            students.get( i ).enroll();
            students.get( i ).payTuition();
        }

        for( Student student : students ){
            System.out.println( student.toString());
        }

        System.out.println( "Number of students enrolled: " + Student.getNumOfStudents());
        System.out.println( "Thank you for using. Please restart program when you wish to add more students." );
    }
}
