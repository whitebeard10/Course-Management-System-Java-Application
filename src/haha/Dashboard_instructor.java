package haha;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dashboard_instructor extends JFrame {
    private JPanel sideNavPanel;
    private JPanel contentPanel;
    private JPanel contentPanel_1;
    private String instructorEmail;

    public Dashboard_instructor(String email) {
        instructorEmail = email;
        setTitle("Teacher Dashboard");
        setSize(905, 766);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());

        // Create a button for the left side of the top panel
        JButton menuButton = new JButton("MENU");
        menuButton.setBackground(new Color(214, 214, 192));
        menuButton.setPreferredSize(new Dimension(184, 40)); // Set preferred size
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
        sideNavPanel.setLayout(new GridLayout(4, 1));
        // Add buttons to sideNavPanel as before

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout()); // Set layout to BorderLayout

        getContentPane().add(sideNavPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        sideNavPanel = new JPanel();
        sideNavPanel.setLayout(new GridLayout(3, 1));

        JButton viewModulesButton = new JButton("View Assigned Modules");
        JButton viewStudentsButton = new JButton("View Students on Modules");
        JButton addMarksButton = new JButton("Add Marks to Modules");
        

        viewModulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showViewAssignedModulesForm();
            }
        });

        viewStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showViewStudentsOnModulesForm();
            }
        });

        addMarksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddMarksToModulesForm();
            }
        });


        sideNavPanel.add(viewModulesButton);
        sideNavPanel.add(viewStudentsButton);
        sideNavPanel.add(addMarksButton);

        contentPanel_1 = new JPanel(new BorderLayout()); // Set layout to BorderLayout

        getContentPane().add(sideNavPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel_1, BorderLayout.CENTER);

        JLabel lblNewLabel = new JLabel("Welcome to the Teacher Dashboard!");
        lblNewLabel.setFont(new Font("Segoe Print", Font.PLAIN, 22));
        lblNewLabel.setForeground(new Color(64, 128, 128));

        contentPanel_1.add(lblNewLabel, BorderLayout.CENTER); // Add label to the center of contentPanel_1

        setVisible(true);
    }

    private void showViewAssignedModulesForm() {
        // Remove all existing components from the content panel
        contentPanel_1.removeAll();

        // Retrieve enrolled modules for the instructor using instructorEmail
        ArrayList<String> enrolledModules = queries.getEnrolledModulesForInstructor(instructorEmail);

        // Create a panel to display the assigned modules
        JPanel modulePanel = new JPanel();
        modulePanel.setLayout(new BoxLayout(modulePanel, BoxLayout.Y_AXIS)); // Vertical layout
        modulePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Add enrolled modules to the panel
        for (String moduleName : enrolledModules) {
            JLabel moduleLabel = new JLabel("Module: " + moduleName);
            modulePanel.add(moduleLabel);
        }

        // Add the module panel to the content panel
        contentPanel_1.add(new JScrollPane(modulePanel), BorderLayout.CENTER);

        // Refresh the content panel
        contentPanel_1.revalidate();
        contentPanel_1.repaint();
    }


    

    private void showAddMarksToModulesForm() {
        // Remove all existing components from the content panel
        contentPanel_1.removeAll();

        // Create a form panel to hold the form fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // GridLayout with 2 columns
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Add form fields and labels to the form panel
        formPanel.add(new JLabel("Module ID:"));
        JTextField moduleIdField = new JTextField();
        formPanel.add(moduleIdField);

        formPanel.add(new JLabel("Student ID:"));
        JTextField studentIdField = new JTextField();
        formPanel.add(studentIdField);

        formPanel.add(new JLabel("Marks:"));
        JTextField marksField = new JTextField();
        formPanel.add(marksField);

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve the values entered by the user
                    int moduleId = Integer.parseInt(moduleIdField.getText());
                    int studentId = Integer.parseInt(studentIdField.getText());
                    int marks = Integer.parseInt(marksField.getText());

                    // Call the addMarksToModule method from SqlLogics class
                    if (queries.addMarksToModule(moduleId, studentId, marks)) {
                        JOptionPane.showMessageDialog(null, "Marks added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add marks. Please check the input data and try again.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input format. Please enter numeric values for Module ID, Student ID, and Marks.");
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

    private void showViewStudentsOnModulesForm() {
        // Remove all existing components from the content panel
        contentPanel_1.removeAll();

        // Retrieve students on modules for the instructor using instructorEmail
        ArrayList<String> studentsOnModules = queries.getStudentsOnModules(instructorEmail);

        // Create a panel to display the students on modules
        JPanel studentPanel = new JPanel(new GridLayout(0, 1));
        studentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Add students on modules to the panel
        for (String student : studentsOnModules) {
            JLabel studentLabel = new JLabel(student);
            studentPanel.add(studentLabel);
        }

        // Add the student panel to the content panel
        contentPanel_1.add(studentPanel, BorderLayout.CENTER);

        // Refresh the content panel
        contentPanel_1.revalidate();
        contentPanel_1.repaint();
    }
    
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                // For testing purposes
//                new Dashboard_instructor(email);
//            }
//        });
//    }
}
