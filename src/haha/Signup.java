package haha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Signup extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleDropdown;
    private JPanel panel_1;

    public Signup() {
        setTitle("Signup");
        setSize(897, 702); // Increased height to accommodate the drop down
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel_1 = new JPanel();
        panel_1.setForeground(new Color(128, 128, 128));
        panel_1.setBackground(new Color(214, 214, 214));
        getContentPane().add(panel_1);
        placeComponents(panel_1);
        
        JLabel lblNewLabel = new JLabel("Create Your Accout Here!");
        lblNewLabel.setFont(new Font("Monospaced", Font.PLAIN, 25));
        lblNewLabel.setForeground(new Color(0, 128, 192));
        lblNewLabel.setBounds(264, 47, 465, 50);
        panel_1.add(lblNewLabel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Load the image from the resource folder
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/herald.png"));
        Image img = logoIcon.getImage();
        Image scaledImg = img.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledImg);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(502, 134, 371, 268);
        panel.add(logoLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        usernameLabel.setForeground(new Color(64, 128, 128));
        usernameLabel.setBounds(27, 181, 219, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(256, 183, 236, 36);
        panel.add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        emailLabel.setForeground(new Color(64, 128, 128));
        emailLabel.setBounds(27, 236, 179, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(256, 238, 236, 36);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        passwordLabel.setForeground(new Color(64, 128, 128));
        passwordLabel.setBounds(27, 300, 179, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(256, 302, 236, 36);
        panel.add(passwordField);

        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setForeground(new Color(0, 0, 0));
        showPasswordCheckBox.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        showPasswordCheckBox.setBounds(256, 344, 236, 16);
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
        roleLabel.setBounds(38, 377, 179, 25);
        panel.add(roleLabel);

        String[] roles = {"teacher", "student", "admin"};
        roleDropdown = new JComboBox<>(roles);
        roleDropdown.setForeground(new Color(128, 128, 128));
        roleDropdown.setFont(new Font("Trebuchet MS", Font.PLAIN, 26));
        roleDropdown.setBounds(256, 366, 236, 36);
        panel.add(roleDropdown);

        JButton signupButton = new JButton("Signup");
        signupButton.setBackground(new Color(214, 214, 214));
        signupButton.setFont(new Font("Segoe UI Black", Font.BOLD, 24));
        signupButton.setForeground(new Color(64, 0, 128));
        signupButton.setBounds(314, 431, 128, 36);
        panel.add(signupButton);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleDropdown.getSelectedItem();

                if (queries.addUser(username, email, password, role)) {
                	if(role.equals("student")) {
                		queries.addStudent(username, email);
                	}
                    JOptionPane.showMessageDialog(null, "Signup successful! Please login.");
                    dispose(); // Close the current window
                    new Login(); // Open the Login window
                } else {
                    JOptionPane.showMessageDialog(null, "Signup failed. Please try again.");
                }
            }
        });

        JLabel haveAccountLabel = new JLabel("<html><u>Already have an account?</u></html>");
        haveAccountLabel.setForeground(new Color(255, 123, 123));
        haveAccountLabel.setFont(new Font("Microsoft Yi Baiti", Font.PLAIN, 20));
        haveAccountLabel.setBounds(293, 477, 200, 25);
        panel.add(haveAccountLabel);

        haveAccountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Close the current window
                new Login(); // Open the Login window
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                haveAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                haveAccountLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Signup();
            }
        });
    }
}
