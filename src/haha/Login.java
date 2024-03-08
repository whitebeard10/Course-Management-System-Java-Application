package haha;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleDropdown;
    private JPanel panel_1;

    public Login() {
        setTitle("Login");
        setSize(897, 702); // Increased height to accommodate the dropdown
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel_1 = new JPanel();
        panel_1.setForeground(new Color(128, 128, 128));
        panel_1.setBackground(new Color(214, 214, 214));
        getContentPane().add(panel_1);
        placeComponents(panel_1);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {

        // Load the image from the resource folder
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
        panel_1.setLayout(null);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(356, 10, 360, 150);
        panel.add(logoLabel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        emailLabel.setForeground(new Color(64, 128, 128));
        emailLabel.setBounds(227, 181, 164, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(439, 170, 236, 36);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        passwordLabel.setForeground(new Color(64, 128, 128));
        passwordLabel.setBounds(227, 247, 179, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(439, 236, 236, 36);
        panel.add(passwordField);
        
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setForeground(new Color(0, 0, 0));
        showPasswordCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        showPasswordCheckBox.setBounds(439, 278, 176, 16);
        panel.add(showPasswordCheckBox);

        showPasswordCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    passwordField.setEchoChar((char) 0); // Show password
                } else {
                    passwordField.setEchoChar('*'); // Hide password
                }
            }
        });

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(new Color(64, 128, 128));
        roleLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        roleLabel.setBounds(227, 308, 179, 25);
        panel.add(roleLabel);

        String[] roles = {"teacher", "student", "admin"};
        roleDropdown = new JComboBox<>(roles);
        roleDropdown.setForeground(new Color(128, 128, 128));
        roleDropdown.setFont(new Font("Trebuchet MS", Font.PLAIN, 26));
        roleDropdown.setBounds(439, 302, 236, 36);
        panel.add(roleDropdown);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(214, 214, 214));
        loginButton.setFont(new Font("Segoe UI Black", Font.BOLD, 24));
        loginButton.setForeground(new Color(64, 0, 128));
        loginButton.setBounds(487, 378, 128, 36);
        panel.add(loginButton);
        JLabel dontHaveAccountLabel = new JLabel("Don't have an account?");
        dontHaveAccountLabel.setForeground(new Color(255, 123, 123));
        dontHaveAccountLabel.setFont(new Font("Microsoft Yi Baiti", Font.PLAIN, 20));
        dontHaveAccountLabel.setBounds(474, 425, 170, 25);
        panel.add(dontHaveAccountLabel);

        dontHaveAccountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Close the current window
                new Signup(); // Open the Signup window
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                dontHaveAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dontHaveAccountLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleDropdown.getSelectedItem();

                // Perform login validation using SqlLogics class
                if (queries.validateLogin(email, password, role)) {
                    Student student = null;
                    if (role.equals("student")) {
                        student = queries.getStudentDetails(email); // Retrieve student details from the database
                    }

                    // Redirect to dashboard based on role
                    if (role.equals("teacher")) {
                        dispose();
                        new Dashboard_instructor(email); // Pass instructor email to Dashboard_teacher constructor
                        System.out.println(email);
                    } else if (role.equals("student")) {
                        dispose();
                        new Dashboard_student(student); // Pass Student object to Dashboard_student constructor
                    } else if (role.equals("admin")) {
                        dispose();
                        new Dashboard_admin();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email, password, or role. Please try again.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login();
            }
        });
    }
}
