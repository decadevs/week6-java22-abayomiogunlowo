package model;

public class User {
    private String name;
    private Integer gradeLevel; // Null for teachers
    private boolean isTeacher;

    // Constructor for Student
    public User(String name, int gradeLevel) {
        this.name = name;
        this.gradeLevel = gradeLevel;
        this.isTeacher = false;
    }

    // Constructor for Teacher
    public User(String name) {
        this.name = name;
        this.gradeLevel = null;
        this.isTeacher = true;
    }

    public String getName() {
        return name;
    }

    public Integer getGradeLevel() {
        return gradeLevel;
    }

    public boolean isTeacher() {
        return isTeacher;
    }
}