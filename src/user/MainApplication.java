package user;

import appclasses.Student;
import appclasses.StudentManager;
import appclasses.StudentManagerException;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MainApplication {
    public static void main(String[] args){
        StudentManager sm = StudentManager.getInstance();
        sm.loadData();
        System.out.println(StudentManager.getNumOfStudents());
        System.out.println(sm.printStudents());
    }
}
