package com.spring.example.lec05.student;

public class StudentInfo {
    private Student student;

    public StudentInfo(Student student) {
        this.student = student;
    }

    public void getStudentInfo() {
        if (null != student) {
            System.out.println("Name  : " + student.getName());
            System.out.println("Age   : " + student.getAge());
            System.out.println("Grade : " + student.getGradeNum());
            System.out.println("Class : " + student.getClassNum());
            System.out.println("=================");
        }
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
