package com.example.midexam;

public class StudentModel {

    private int id;
    private String studentName;
    private String studentId;
    private boolean enrolled;

    public StudentModel(int id, String studentName, String studentId, boolean enrolled) {
        this.id = id;
        this.studentName = studentName;
        this.studentId = studentId;
        this.enrolled = enrolled;
    }

    public StudentModel(String studentName, String studentId, boolean enrolled) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.enrolled = enrolled;
    }

    public int getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }
}