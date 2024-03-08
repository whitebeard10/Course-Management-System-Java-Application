package haha;

import java.util.ArrayList;

public class Course {
    private int courseId;
    private String courseName;
    private ArrayList<Module> modules;

    // Constructor
    public Course(int courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.modules = new ArrayList<>();
    }
    
    // Getters and setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // Other methods
    public void addModule(Module module) {
        modules.add(module);
    }

    public void removeModule(Module module) {
        modules.remove(module);
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    // Override toString method for better representation
    @Override
    public String toString() {
        return "Course ID: " + courseId + ", Name: " + courseName;
    }
}
