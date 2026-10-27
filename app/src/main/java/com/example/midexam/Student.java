package com.example.midexam;

public class Student {
    private final String id;
    private final String name;
    private final boolean enrolled;

    public Student(String id, String name, boolean enrolled) {
        this.id = id;
        this.name = name;
        this.enrolled = enrolled;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isEnrolled() { return enrolled; }


    @Override
    public String toString() {
        return "ID: " + id + "\nName: " + name + "\nEnrolled: " + (enrolled ? "Yes" : "No");
    }
}