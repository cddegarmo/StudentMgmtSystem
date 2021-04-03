package user;

import appclasses.StudentManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MainApplication {
    public static void main(String[] args){
        StudentManager sm = StudentManager.getInstance();
        sm.loadStudents();
        sm.loadStudent();
        System.out.println(sm.printStudents());
        System.out.println(sm.getNumOfStudents());
    }
}
