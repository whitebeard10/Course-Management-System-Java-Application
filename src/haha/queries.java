package haha;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class queries {

    public static boolean validateLogin(String email, String password, String role) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT * FROM User WHERE email = ? AND password = ? AND role = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, role);
            ResultSet result = statement.executeQuery();
            boolean isValid = result.next();
            connection.close();
            return isValid;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean addUser(String username, String email, String password, String role) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "INSERT INTO User (username, email, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, role);
            int rowsInserted = statement.executeUpdate();

            // If the role is "student", add the username and email to the student table
            if (role.equalsIgnoreCase("student")) {
                sql = "INSERT INTO student (student_name, email) VALUES (?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, email);
                int rowsInsertedStudent = statement.executeUpdate();
                if (rowsInsertedStudent <= 0) {
                    // Rollback the user insertion if student insertion fails
                    connection.rollback();
                    return false;
                }
            }

            connection.close();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public static boolean addCourse(String courseName, String courseDescription) {
        try {
        	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
        	String sql = "INSERT INTO Course (course_name, course_description) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, courseName);
            statement.setString(2, courseDescription);
            int rowsInserted = statement.executeUpdate();
            connection.close();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean addModule(String moduleName, int courseID, String startDate, String endDate, int level, boolean isOptional) {
        try {
        	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
        	String sql = "INSERT INTO Module (module_name, course_id, start_date, end_date, level, is_optional) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, moduleName);
            statement.setInt(2, courseID);
            statement.setString(3, startDate);
            statement.setString(4, endDate);
            statement.setInt(5, level);
            statement.setBoolean(6, isOptional);
            int rowsInserted = statement.executeUpdate();
            connection.close();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean removeInstructorFromModule(int moduleId, String instructorName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "DELETE FROM instructormodules WHERE module_id = ? AND instructor_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, moduleId);
            statement.setString(2, instructorName);
            int rowsDeleted = statement.executeUpdate();
            connection.close();
            return rowsDeleted > 0; // Return true if at least one row was deleted
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static boolean addInstructor(String instructorName, String email, String phone, String qualification, String assignModules) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");

            // Insert instructor into Instructor table
            String insertInstructorSql = "INSERT INTO instructor (instructor_name, email, phone, qualification) VALUES (?, ?, ?, ?)";
            PreparedStatement insertInstructorStatement = connection.prepareStatement(insertInstructorSql);
            insertInstructorStatement.setString(1, instructorName);
            insertInstructorStatement.setString(2, email);
            insertInstructorStatement.setString(3, phone);
            insertInstructorStatement.setString(4, qualification);
            insertInstructorStatement.executeUpdate();

            // Retrieve the module IDs from the input string
            String[] moduleIds = assignModules.split(",");
            for (String moduleId : moduleIds) {
                // Insert instructor and module IDs into course_instructor_assignment table
                String insertCourseInstructorAssignmentSql = "INSERT INTO course_instructor_assignment (course_id, instructor_id, module_id) " +
                                                             "SELECT c.course_id, (SELECT instructor_id FROM instructor WHERE instructor_name = ?), ? " +
                                                             "FROM course c INNER JOIN module m ON c.course_id = m.course_id WHERE m.module_id = ?";
                PreparedStatement insertCourseInstructorAssignmentStatement = connection.prepareStatement(insertCourseInstructorAssignmentSql);
                insertCourseInstructorAssignmentStatement.setString(1, instructorName);
                insertCourseInstructorAssignmentStatement.setInt(2, Integer.parseInt(moduleId.trim()));
                insertCourseInstructorAssignmentStatement.setInt(3, Integer.parseInt(moduleId.trim()));
                insertCourseInstructorAssignmentStatement.executeUpdate();
            }

            connection.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }



    public static String generateResultSlip(int studentID) {
        StringBuilder resultSlipBuilder = new StringBuilder("Result Slip for Student ID " + studentID + ":\n");

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT Module.module_name, Enrollment.marks FROM Module INNER JOIN Enrollment ON Module.module_id = Enrollment.module_id WHERE Enrollment.student_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, studentID);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String moduleName = result.getString("module_name");
                double marks = result.getDouble("marks");
                resultSlipBuilder.append(moduleName).append(": ").append(marks).append("\n");
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return resultSlipBuilder.toString();
    }



 // Method to view all courses and return as a formatted string
    public static ArrayList<String> viewCourses() {
        ArrayList<String> courseList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT * FROM Course";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int courseId = result.getInt("course_id");
                String courseName = result.getString("course_name");
                String courseDescription = result.getString("course_description");
                String status = result.getString("status");
                String courseInfo = "Course ID: \n" + courseId + "\n Name: " + courseName + "\n Description: " + courseDescription + "\n Status: " + status;
                courseList.add(courseInfo);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courseList;
    }
    
    public String getStatus(int id) {
        String status = null; // Initialize status to null or provide a default value

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT status FROM Course WHERE course_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id); // Set the value of the parameter
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                status = result.getString("status");
                return status;
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception or return a default value
        }

        return status; // Return the status (or null) outside the try block
    }

    
    

    public static ArrayList<Course> getAvailableCourses() {
        ArrayList<Course> courses = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "")) {
            String query = "SELECT * FROM course";
            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    int courseId = resultSet.getInt("course_id");
                    String courseName = resultSet.getString("course_name");
                    Course course = new Course(courseId, courseName);
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }
    
        private static final String DB_URL = "jdbc:mysql://localhost:3306/cms";
        private static final String DB_USER = "root";
        private static final String DB_PASSWORD = "";

        public static void updateStudentLevel(String email, String level) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String updateQuery = "UPDATE student SET level = ? WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setString(1, level);
                    preparedStatement.setString(2, email);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle any SQL errors
            }
        }
    
    
    public static Student getStudentDetails(String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Student student = null;

        try {
            // Establish connection to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");

            // Prepare SQL statement to retrieve student details based on email
            String sql = "SELECT * FROM student WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            // Execute query
            rs = stmt.executeQuery();

            // Check if student exists
            if (rs.next()) {
                // Retrieve student details from the result set
                int studentId = rs.getInt("student_id");
                String studentName = rs.getString("student_name");

                // Create Student object
                student = new Student(studentId, studentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close connections
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return student;
    }
    
    public static boolean addStudent(String username, String email) {
        try {
        	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String query = "INSERT INTO student (student_name, email) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, email);
            int rowsInserted = statement.executeUpdate();
            statement.close();
            connection.close();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to cancel a course
    public static boolean cancelCourse(int courseID) {
        try {
        	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
        	String sql = "UPDATE Course SET status = 'cancelled' WHERE course_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, courseID);
            int rowsUpdated = statement.executeUpdate();
            connection.close();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Method to delete a course
    public static boolean deleteCourse(int courseID) {
        try {
        	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
        	String sql = "DELETE FROM Course WHERE course_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, courseID);
            int rowsDeleted = statement.executeUpdate();
            connection.close();
            return rowsDeleted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean enrollCourse(String studentEmail, int courseId) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "UPDATE Student SET course_enrolled = CONCAT_WS(',', course_enrolled, ?) WHERE student_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, courseId);
            statement.setString(2, studentEmail);
            int rowsUpdated = statement.executeUpdate();
            connection.close();
            return rowsUpdated > 0; // Return true if at least one row was updated
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception or return false indicating failure
            return false;
        }
    }
    
//    public static ArrayList<Integer> getEnrolledCourseIds(String studentName) {
//        ArrayList<Integer> enrolledCourseIds = new ArrayList<>();
//        try {
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
//            String sql = "SELECT course_enrolled FROM Student WHERE student_name = ?";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, studentName);
//            ResultSet result = statement.executeQuery();
//            while (result.next()) {
//                enrolledCourseIds.add(result.getInt("course_enrolled"));
//            }
//            connection.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return enrolledCourseIds;
//    }

//    public static String getCourseName(int courseId) {
//        String courseName = "";
//        try {
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
//            String sql = "SELECT course_name FROM Course WHERE course_id = ?";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, courseId);
//            ResultSet result = statement.executeQuery();
//            if (result.next()) {
//                courseName = result.getString("course_name");
//            }
//            connection.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return courseName;
//    }

    public static ArrayList<String> getEnrolledModulesForInstructor(String instructorEmail) {
        ArrayList<String> enrolledModules = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT m.module_name " +
                         "FROM module m " +
                         "INNER JOIN course_instructor_assignment cia ON m.module_id = cia.module_id " +
                         "INNER JOIN instructor i ON cia.instructor_id = i.instructor_id " +
                         "WHERE i.email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, instructorEmail);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String moduleName = result.getString("module_name");
                enrolledModules.add(moduleName);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enrolledModules;
    }



    
    public static ArrayList<String> getAssignedModules(String instructorName) {
        ArrayList<String> assignedModules = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT m.module_name FROM instructormodules im " +
                         "JOIN module m ON im.module_id = m.module_id " +
                         "WHERE im.instructor_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, instructorName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String moduleName = result.getString("module_name");
                assignedModules.add(moduleName);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return assignedModules;
    }

 // Method to get enrolled module IDs for a student
    public static ArrayList<Integer> getEnrolledModuleIds(String studentName) {
        ArrayList<Integer> enrolledModuleIds = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT module_id FROM studentmodule WHERE student_id = (SELECT student_id FROM student WHERE student_name = ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, studentName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int moduleId = result.getInt("module_id");
                enrolledModuleIds.add(moduleId);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enrolledModuleIds;
    }

//    // Method to get enrolled course IDs for a student
//    public static ArrayList<Integer> getEnrolledCourseIds(String studentName) {
//        ArrayList<Integer> enrolledCourseIds = new ArrayList<>();
//        try {
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
//            String sql = "SELECT course_id FROM module WHERE module_id IN (SELECT module_id FROM studentmodule WHERE student_id = (SELECT student_id FROM student WHERE student_name = ?))";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, studentName);
//            ResultSet result = statement.executeQuery();
//            while (result.next()) {
//                int courseId = result.getInt("course_id");
//                enrolledCourseIds.add(courseId);
//            }
//            connection.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return enrolledCourseIds;
//    }
    
    public static ArrayList<String> getStudentsOnModules(String instructorEmail) {
        ArrayList<String> studentsOnModules = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT s.student_name, m.module_name " +
                         "FROM student s " +
                         "INNER JOIN studentmodule sm ON s.student_id = sm.student_id " +
                         "INNER JOIN module m ON sm.module_id = m.module_id " +
                         "INNER JOIN course_instructor_assignment cia ON m.module_id = cia.module_id " +
                         "INNER JOIN instructor i ON cia.instructor_id = i.instructor_id " +
                         "WHERE i.email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, instructorEmail);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String studentName = result.getString("student_name");
                String moduleName = result.getString("module_name");
                String studentOnModule = studentName + " - " + moduleName;
                studentsOnModules.add(studentOnModule);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return studentsOnModules;
    }

    // Add marks to a module for a student
    public static boolean addMarksToModule(int moduleId, int studentId, int marks) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "INSERT INTO Enrollment (module_id, student_id, marks) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, moduleId);
            statement.setInt(2, studentId);
            statement.setInt(3, marks);
            int rowsInserted = statement.executeUpdate();
            connection.close();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static ArrayList<Integer> getEnrolledCourseIds(String studentName) {
        ArrayList<Integer> enrolledCourseIds = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT course_enrolled FROM Student WHERE student_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, studentName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                enrolledCourseIds.add(result.getInt("course_enrolled"));
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enrolledCourseIds;
    }

    public static String getCourseName(int courseId) {
        String courseName = "";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT course_name FROM Course WHERE course_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, courseId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                courseName = result.getString("course_name");
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courseName;
    }

    public static ArrayList<Integer> getEnrolledModuleIds(String studentName, int courseId) {
        ArrayList<Integer> enrolledModuleIds = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT module_id FROM StudentModule WHERE student_id IN (SELECT student_id FROM Student WHERE student_name = ?) AND course_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, studentName);
            statement.setInt(2, courseId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                enrolledModuleIds.add(result.getInt("module_id"));
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enrolledModuleIds;
    }

    public static String getModuleName(int moduleId) {
        String moduleName = "";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT module_name FROM Module WHERE module_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, moduleId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                moduleName = result.getString("module_name");
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return moduleName;
    }
    
 // Method to retrieve and return enrolled modules for a student
    public static HashMap<String, ArrayList<String>> getEnrolledModulesForStudent(String studentName) {
        HashMap<String, ArrayList<String>> enrolledModulesMap = new HashMap<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
            String sql = "SELECT m.module_name, i.instructor_name " +
                         "FROM student s " +
                         "INNER JOIN studentmodule sm ON s.student_id = sm.student_id " +
                         "INNER JOIN module m ON sm.module_id = m.module_id " +
                         "INNER JOIN course_instructor_assignment cia ON m.module_id = cia.module_id " +
                         "INNER JOIN instructor i ON cia.instructor_id = i.instructor_id " +
                         "WHERE s.student_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, studentName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String moduleName = result.getString("module_name");
                String instructorName = result.getString("instructor_name");
                if (!enrolledModulesMap.containsKey(moduleName)) {
                    enrolledModulesMap.put(moduleName, new ArrayList<>());
                }
                enrolledModulesMap.get(moduleName).add(instructorName);
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enrolledModulesMap;
    }


}
