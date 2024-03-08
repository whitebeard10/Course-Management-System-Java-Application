package haha;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class Dashboard_admin extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel sideNavPanel;
    private JPanel contentPanel;
    private JPanel contentPanel_1;

    public Dashboard_admin() {
        setTitle("Admin Dashboard");
        setSize(905, 766);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Create a button for the left side of the top panel
        JButton menuButton = new JButton("MENU");
        menuButton.setBackground(new Color(214, 214, 192));
        menuButton.setPreferredSize(new Dimension(152, 40)); // Set preferred size
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
        sideNavPanel.setLayout(new GridLayout(8, 1));

        JButton addCourseButton = new JButton("Add Course");
        JButton addModuleButton = new JButton("Add Module");
        JButton addInstructorButton = new JButton("Add Instructor");
        JButton deleteInstructorButton = new JButton("Delete Instructor");
        JButton generateResultSlipButton = new JButton("Generate Result Slip");
        JButton viewCoursesButton = new JButton("View Courses");
        JButton cancelCourseButton = new JButton("Cancel Course");
        JButton deleteCourseButton = new JButton("Delete Course");

        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCourseForm();
            }
        });

        addModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddModuleForm();
            }
        });

        addInstructorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddInstructorForm();
            }
        });
        
        deleteInstructorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	showRemoveInstructorFromModulesForm();
            }
        });

        generateResultSlipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGenerateResultSlipForm();
            }
        });

        viewCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showViewCoursesForm();
            }
        });

        cancelCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCancelCourseForm();
            }
        });

        deleteCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteCourseForm();
            }
        });

        sideNavPanel.add(addCourseButton);
        sideNavPanel.add(addModuleButton);
        sideNavPanel.add(addInstructorButton);
        sideNavPanel.add(deleteInstructorButton);
        sideNavPanel.add(generateResultSlipButton);
        sideNavPanel.add(viewCoursesButton);
        sideNavPanel.add(cancelCourseButton);
        sideNavPanel.add(deleteCourseButton);

        contentPanel_1 = new JPanel();
        contentPanel_1.setAlignmentX(Component.LEFT_ALIGNMENT);

        getContentPane().add(sideNavPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel_1, BorderLayout.CENTER);
        contentPanel_1.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Welcome to the Admin Dashboard!");
        lblNewLabel.setFont(new Font("Segoe Print", Font.PLAIN, 22));
        lblNewLabel.setForeground(new Color(64, 128, 128));
        lblNewLabel.setBounds(199, 323, 432, 38);
        contentPanel_1.add(lblNewLabel);

        setVisible(true);
    }

    private void showAddCourseForm() {
        // Implement functionality to show Add Course form in the content panel
        contentPanel_1.removeAll();
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("Course Name:"));
        JTextField courseNameField = new JTextField();
        formPanel.add(courseNameField);
        formPanel.add(new JLabel("Course Description:"));
        JTextArea courseDescriptionArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(courseDescriptionArea);
        formPanel.add(scrollPane);
        
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle form submission logic here
                String courseName = courseNameField.getText();
                String courseDescription = courseDescriptionArea.getText();
                if (courseName.isEmpty() || courseDescription.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                } else {
                    // Insert course into Course table
                    if (queries.addCourse(courseName, courseDescription)) {
                        JOptionPane.showMessageDialog(null, "Course added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add course.");
                    }
                }
            }
        });
        formPanel.add(submitButton);
        
        contentPanel_1.add(formPanel, BorderLayout.CENTER);
        contentPanel_1.revalidate();
        contentPanel_1.repaint();
    }



    private void showAddModuleForm() {
        // Remove all existing components from the content panel
        contentPanel_1.removeAll();

        // Create a form panel to hold the form fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // GridLayout with 2 columns
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Add form fields and labels to the form panel
        formPanel.add(new JLabel("Module Name:"));
        JTextField moduleNameField = new JTextField();
        formPanel.add(moduleNameField);

        formPanel.add(new JLabel("Course ID:"));
        JTextField courseIdField = new JTextField();
        formPanel.add(courseIdField);

        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        JTextField startDateField = new JTextField();
        formPanel.add(startDateField);

        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        JTextField endDateField = new JTextField();
        formPanel.add(endDateField);

        formPanel.add(new JLabel("Level:"));
        JTextField levelField = new JTextField();
        formPanel.add(levelField);

        formPanel.add(new JLabel("Is Optional (true/false):"));
        JTextField isOptionalField = new JTextField();
        formPanel.add(isOptionalField);

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve the values entered by the user
                    String moduleName = moduleNameField.getText();
                    int courseId = Integer.parseInt(courseIdField.getText());
                    String startDate = startDateField.getText();
                    String endDate = endDateField.getText();
                    int level = Integer.parseInt(levelField.getText());
                    boolean isOptional = Boolean.parseBoolean(isOptionalField.getText());

                    // Call the addModule method from SqlLogics class
                    if (queries.addModule(moduleName, courseId, startDate, endDate, level, isOptional)) {
                        JOptionPane.showMessageDialog(null, "Module added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add module. Please check the input data and try again.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input format. Please enter numeric values for Course ID and Level.");
                }
            }
        });
        formPanel.add(submitButton); // Add the submit button to the form panel

        // Add the form panel to the content panel
        contentPanel_1.add(formPanel, BorderLayout.CENTER);

        // Refresh the content panel to reflect the changes
        contentPanel_1.revalidate();
        contentPanel_1.repaint();
    }



    private void showAddInstructorForm() {
        // Remove all existing components from the content panel
        contentPanel_1.removeAll();

        // Create a form panel to hold the form fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // GridLayout with 2 columns
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Add form fields and labels to the form panel
        formPanel.add(new JLabel("Instructor Name:"));
        JTextField instructorNameField = new JTextField();
        formPanel.add(instructorNameField);

        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone Number:"));
        JTextField phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Qualification:"));
        JTextField qualificationField = new JTextField();
        formPanel.add(qualificationField);

        // Add a field for assigning modules
        formPanel.add(new JLabel("Assign Modules (Module IDs separated by commas):"));
        JTextField assignModulesField = new JTextField("Input the module IDs separated by commas");
        formPanel.add(assignModulesField);

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve the values entered by the user
                    String instructorName = instructorNameField.getText();
                    String email = emailField.getText();
                    String phone = phoneField.getText();
                    String qualification = qualificationField.getText();
                    String assignModules = assignModulesField.getText();

                    // Call the addInstructor method from SqlLogics class
                    if (queries.addInstructor(instructorName, email, phone, qualification, assignModules)) {
                        JOptionPane.showMessageDialog(null, "Instructor added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add instructor. Please check the input data and try again.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input format.");
                }
            }
        });
        formPanel.add(submitButton); // Add the submit button to the form panel

        // Add the form panel to the content panel
        contentPanel_1.add(formPanel, BorderLayout.CENTER);

        // Refresh the content panel to reflect the changes
        contentPanel_1.revalidate();
        contentPanel_1.repaint();
    }



    private void showGenerateResultSlipForm() {
        // Remove all existing components from the content panel
        contentPanel_1.removeAll();

        // Create a form panel to hold the form fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // GridLayout with 2 columns
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Add form fields and labels to the form panel
        formPanel.add(new JLabel("Student ID:"));
        JTextField studentIDField = new JTextField();
        formPanel.add(studentIDField);

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve the student ID entered by the user
                    int studentID = Integer.parseInt(studentIDField.getText());

                    // Call the generateResultSlip method from SqlLogics class
                    String resultSlip = queries.generateResultSlip(studentID);
                    if (resultSlip != null) {
                        // Display the result slip in a dialog box
                        JOptionPane.showMessageDialog(null, resultSlip, "Result Slip for Student ID " + studentID, JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No result slip found for the provided student ID.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input format. Please enter a numeric student ID.");
                }
            }
        });
        formPanel.add(submitButton); // Add the submit button to the form panel

        // Add the form panel to the content panel
        contentPanel_1.add(formPanel, BorderLayout.CENTER);

        // Refresh the content panel to reflect the changes
        contentPanel_1.revalidate();
        contentPanel_1.repaint();
    }


    private void showViewCoursesForm() {
        contentPanel_1.removeAll();
        contentPanel_1.setLayout(new GridLayout(0, 1, 10, 10)); // Set layout to grid with vertical spacing

        ArrayList<String> courseList = queries.viewCourses(); // Retrieve course list from database

        for (String courseInfo : courseList) {
            JPanel coursePanel = createCoursePanel(courseInfo); // Create a panel for each course
            
            contentPanel_1.add(coursePanel); // Add course panel to content panel
        }

        contentPanel_1.revalidate();
        contentPanel_1.repaint();
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





    private void showCancelCourseForm() {
        // Remove all existing components from the content panel
        contentPanel_1.removeAll();

        // Create a form panel to hold the form fields
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // GridLayout with 2 columns
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Add form fields and labels to the form panel
        formPanel.add(new JLabel("Course ID:"));
        JTextField courseIdField = new JTextField();
        formPanel.add(courseIdField);

        // Add the form panel to the content panel
        contentPanel_1.add(formPanel, BorderLayout.CENTER);

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve the value entered by the user
                    int courseId = Integer.parseInt(courseIdField.getText());

                    // Implement the cancelCourse method from SqlLogics class
                    if (queries.cancelCourse(courseId)) {
                        JOptionPane.showMessageDialog(null, "Course canceled successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to cancel course. Please check the input data and try again.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input format. Please enter a numeric value for Course ID.");
                }
            }
        });
        formPanel.add(submitButton);

        contentPanel_1.revalidate();
        contentPanel_1.repaint();
    }
    
    private void showRemoveInstructorFromModulesForm() {
        // Remove all existing components from the content panel
    	contentPanel_1.removeAll();

        // Create a form panel to hold the form fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // GridLayout with 2 columns
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Add form fields and labels to the form panel
        formPanel.add(new JLabel("Module ID:"));
        JTextField moduleIdField = new JTextField();
        formPanel.add(moduleIdField);

        formPanel.add(new JLabel("Instructor Name:"));
        JTextField instructorNameField = new JTextField();
        formPanel.add(instructorNameField);

        // Create a submit button
        JButton submitButton = new JButton("Remove Instructor from Module");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve the values entered by the user
                    int moduleId = Integer.parseInt(moduleIdField.getText());
                    String instructorName = instructorNameField.getText();

                    // Call the removeInstructorFromModule method from SqlLogics class
                    if (queries.removeInstructorFromModule(moduleId, instructorName)) {
                        JOptionPane.showMessageDialog(null, "Instructor removed from module successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to remove instructor from module. Please check the input data and try again.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input format for Module ID.");
                }
            }
        });
        formPanel.add(submitButton); // Add the submit button to the form panel

        // Add the form panel to the content panel
        contentPanel_1.add(formPanel, BorderLayout.CENTER);

        // Refresh the content panel to reflect the changes
        contentPanel_1.revalidate();
        contentPanel_1.repaint();
    }

    private void showDeleteCourseForm() {
        // Remove all existing components from the content panel
        contentPanel_1.removeAll();

        // Create a form panel to hold the form fields
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // GridLayout with 2 columns
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Add form fields and labels to the form panel
        formPanel.add(new JLabel("Course ID:"));
        JTextField courseIdField = new JTextField();
        formPanel.add(courseIdField);

        // Add the form panel to the content panel
        contentPanel_1.add(formPanel, BorderLayout.CENTER);

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve the value entered by the user
                    int courseId = Integer.parseInt(courseIdField.getText());

                    // Implement the deleteCourse method from SqlLogics class
                    if (queries.deleteCourse(courseId)) {
                        JOptionPane.showMessageDialog(null, "Course deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete course. Please check the input data and try again.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input format. Please enter a numeric value for Course ID.");
                }
            }
        });
        formPanel.add(submitButton);

        contentPanel_1.revalidate();
        contentPanel_1.repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Dashboard_admin();
            }
        });
    }
}
