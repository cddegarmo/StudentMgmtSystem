package appclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Student implements Comparable<Student> {
    private int id;
    private final String firstName;
    private final String lastName;
    private final int year;
    private int tuitionBalance = 0;
    
    Student(int id, String first, String last, int year) {
        this.id = id;
        firstName = first;
        lastName  = last;
        this.year = year;
    }

    public String getFirstName()   { return firstName;      }
    public String getLastName()    { return lastName;       }
    public int getYear()           { return year;           }
    public int getTuitionBalance() { return tuitionBalance; }
    public int getId()             { return id;             }

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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Student))
            return false;
        Student s = (Student) o;
        return s.firstName.equals(firstName) && s.lastName.equals(lastName) &&
             s.year == year;
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + Integer.hashCode(year);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%n%s, %s : %s",
                             lastName, firstName, getYearName(year));
    }
}
