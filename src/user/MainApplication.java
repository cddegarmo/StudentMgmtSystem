package user;

import appclasses.StudentManager;

class MainApplication {
    public static void main(String[] args){
        StudentManager sm = StudentManager.getInstance();
        sm.loadData();
        System.out.println(sm.printStudents());
    }
}
