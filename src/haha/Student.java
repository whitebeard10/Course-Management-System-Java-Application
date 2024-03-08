package haha;


import java.util.ArrayList;

public class Student {
    private int studentId;
    private String studentName;
    private ArrayList<Course> enrolledCourses;

    // Constructor
    public Student(int studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.enrolledCourses = new ArrayList<>();
    }
    
    // Getters and setters
    public int getStudentId() {
        return studentId;
    }
    public String getEmail() {
        return email;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    // Other methods
    public void enrollCourse(Course course) {
        enrolledCourses.add(course);
    }

    public void unenrollCourse(Course course) {
        enrolledCourses.remove(course);
    }

    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }
        private String email;
        private String level; // Add level attribute

        public Student(String email, String level) {
            this.email = email;
            this.level = level;
        }

        // Getter and setter methods for level attribute
        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
 

    // Override toString method for better representation
    @Override
    public String toString() {
        return "Student ID: " + studentId + ", Name: " + studentName;
    }
}
