package haha;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard_student extends JFrame {
    private JPanel sideNavPanel;
    private JPanel contentPanel;
    private Student student;

    public Dashboard_student(Student student) {
        this.student = student;

        setTitle("Student Dashboard");
        setSize(905, 766);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());

        // Create a button for the left side of the top panel
        JButton menuButton = new JButton("MENU");
        menuButton.setBackground(new Color(214, 214, 192));
        menuButton.setPreferredSize(new Dimension(162, 40)); // Set preferred size
        topPanel.add(menuButton, BorderLayout.WEST);

        // Create a label for the center of the top panel
        JLabel titleLabel = new JLabel("Course Management System");
        titleLabel.setForeground(new Color(0, 128, 128));
        titleLabel.setBackground(new Color(214, 214, 192));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set font and size
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text
        topPanel.add(titleLabel, BorderLayout.CENTER);

        getContentPane().add(topPanel, BorderLayout.NORTH); // Add top panel to the frame

        // Initialize sideNavPanel and contentPanel as before
        sideNavPanel = new JPanel();
        sideNavPanel.setLayout(new GridLayout(7, 1));
        // Add buttons to sideNavPanel as before

        contentPanel = new JPanel();
        contentPanel.setLayout(null); // Set layout to null

        getContentPane().add(sideNavPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        sideNavPanel = new JPanel();
        sideNavPanel.setLayout(new GridLayout(7, 1));

        JButton ViewCourseButton = new JButton("View Courses");
        JButton chooseCoursesButton = new JButton("Choose Courses");
        JButton viewEnrolledCoursesButton = new JButton("View Enrolled Courses");
        JButton showEnrolledModulesButton = new JButton("View Instructors"); // New button added


        
        ViewCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ShowViewCourseButton(student);
            }
        });
        
        chooseCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChooseCoursesForm(student);
            }
        });

        viewEnrolledCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showViewEnrolledCoursesForm(student);
            }
        });
        
        showEnrolledModulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEnrolledModulesForStudent(student);
            }
        });

        sideNavPanel.add(ViewCourseButton);
        sideNavPanel.add(chooseCoursesButton);
        sideNavPanel.add(viewEnrolledCoursesButton);
        sideNavPanel.add(showEnrolledModulesButton);

        contentPanel = new JPanel();

        getContentPane().add(sideNavPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblWelcome = new JLabel("Welcome " + student.getStudentName() + "!");
        lblWelcome.setFont(new Font("Segoe Print", Font.PLAIN, 22));
        lblWelcome.setForeground(new Color(64, 128, 128));
        lblWelcome.setBounds(300, 150, 300, 38);
        contentPanel.add(lblWelcome);

        setVisible(true);
    }

    private void ShowViewCourseButton(Student student) {
    	contentPanel.removeAll();
        contentPanel.setLayout(new GridLayout(0, 1, 10, 10)); // Set layout to grid with vertical spacing

        ArrayList<String> courseList = queries.viewCourses(); // Retrieve course list from database

        for (String courseInfo : courseList) {
            JPanel coursePanel = createCoursePanel(courseInfo); // Create a panel for each course
            
            contentPanel.add(coursePanel); // Add course panel to content panel
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showChooseCoursesForm(Student student) {
        contentPanel.removeAll();

        JLabel lblCourseId = new JLabel("Enter Course ID:");
        lblCourseId.setBounds(50, 50, 120, 25);
        contentPanel.add(lblCourseId);

        JTextField txtCourseId = new JTextField();
        txtCourseId.setBounds(180, 50, 150, 25);
        contentPanel.add(txtCourseId);

        JButton chooseButton = new JButton("Choose");
        chooseButton.setBounds(180, 100, 100, 25);
        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseIdStr = txtCourseId.getText();
                if (courseIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a course ID.");
                    return;
                }

                try {
                    int courseId = Integer.parseInt(courseIdStr);
                    queries sqlLogicsInstance = new queries();
                    String status = sqlLogicsInstance.getStatus(courseId);

                    if (status != null && status.equalsIgnoreCase("active")) {
                        // Call the enrollCourse method from SqlLogics
                        boolean enrolled = queries.enrollCourse(student.getStudentName(), courseId);
                        if (enrolled) {
                            JOptionPane.showMessageDialog(null, "Course enrolled successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to enroll in the course.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "This course is not active.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid course ID. Please enter a valid integer.");
                }
            }
        });
        contentPanel.add(chooseButton);

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    // Helper method to create a panel for each course
    private JPanel createCoursePanel(String courseInfo) {
        JPanel panel = new JPanel(new BorderLayout());
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1); // Create border for the panel
        panel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10))); // Add padding

        JLabel label = new JLabel(courseInfo);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }


    private void showViewEnrolledCoursesForm(Student student) {
        contentPanel.removeAll();

        JLabel titleLabel = new JLabel("Enrolled Courses and Modules:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBounds(20, 20, 300, 25);
        contentPanel.add(titleLabel);

        // Get enrolled course IDs for the student
        ArrayList<Integer> enrolledCourseIds = queries.getEnrolledCourseIds(student.getStudentName());

        // Display enrolled courses
        for (Integer courseId : enrolledCourseIds) {
            String courseName = queries.getCourseName(courseId);
            JLabel courseLabel = new JLabel("Course: " + courseName);
            courseLabel.setBounds(20, 60 + (enrolledCourseIds.indexOf(courseId) * 40), 300, 25);
            contentPanel.add(courseLabel);

            // Get enrolled module IDs for the student and the current course
            ArrayList<Integer> enrolledModuleIds = queries.getEnrolledModuleIds(student.getStudentName());

            // Display enrolled modules for the current course
            for (Integer moduleId : enrolledModuleIds) {
                String moduleName = queries.getModuleName(moduleId);
                JLabel moduleLabel = new JLabel("Module: " + moduleName);
                moduleLabel.setBounds(40, 80 + (enrolledCourseIds.indexOf(courseId) * 40) + (enrolledModuleIds.indexOf(moduleId) * 20), 300, 25);
                contentPanel.add(moduleLabel);
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

 // Method to display enrolled modules for the student
    private void showEnrolledModulesForStudent(Student student) {
        contentPanel.removeAll(); // Clear existing content

        // Retrieve enrolled modules for the student
        HashMap<String, ArrayList<String>> enrolledModulesMap = queries.getEnrolledModulesForStudent(student.getStudentName());

        // Display enrolled modules and their corresponding instructors
        for (String moduleName : enrolledModulesMap.keySet()) {
            JLabel moduleLabel = new JLabel("Module: " + moduleName);
            moduleLabel.setBounds(20, 60, 300, 25);
            contentPanel.add(moduleLabel);

            ArrayList<String> instructors = enrolledModulesMap.get(moduleName);
            for (String instructor : instructors) {
                JLabel instructorLabel = new JLabel("Instructor: " + instructor);
                instructorLabel.setBounds(40, 80, 300, 25);
                contentPanel.add(instructorLabel);
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Add error handling for student object
                if (student == null) {
                    JOptionPane.showMessageDialog(null, "You must log in with student credentials first.");
                    return;
                }
                new Dashboard_student(student);
            }
        });
    }
}


/*In this  Dashboard_student class, the main method
 *  is unnecessary since it's not meant to be the 
 *  entry point of your application. The main method 
 *  is typically used as the entry point for standalone 
 *  applications, but in this case, your Dashboard_student 
 *  class is meant to be instantiated and displayed after 
 *  the user logs in successfully from the login page.
 */