import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    // Components of the Login Page
    private JLabel labelUsername;
    private JLabel labelPassword;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JButton buttonLogin;
    private JLabel labelMessage;
    private JLabel headingLabel;

    // Sample credentials for different roles
    private String adminUsername = "admin";
    private String adminPassword = "admin123";

    private String teacherUsername = "teacher";
    private String teacherPassword = "teacher123";

    private String studentUsername = "student";
    private String studentPassword = "student123";

    private int studentId = 1;
    private String studentName = "John Doe"; 

    public Login() {
        // Frame settings
        setTitle("University ERP - Login");
        setSize(400, 350);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout
        setLayout(new BorderLayout(10, 10));

        // Heading Label
        headingLabel = new JLabel("LOGIN", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(headingLabel, BorderLayout.NORTH);

        // Input Panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        labelUsername = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(labelUsername, gbc);

        textFieldUsername = new JTextField(20);
        gbc.gridx = 1;
        panel.add(textFieldUsername, gbc);

        // Password
        labelPassword = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(labelPassword, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Add to center
        add(panel, BorderLayout.CENTER);

        // Message Label
        labelMessage = new JLabel("");
        labelMessage.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelMessage, BorderLayout.SOUTH);

        // Login Button
        buttonLogin = new JButton("Login");
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = textFieldUsername.getText();
                String enteredPassword = new String(passwordField.getPassword());

                if (enteredUsername.equals(adminUsername) && enteredPassword.equals(adminPassword)) {
                    labelMessage.setForeground(Color.GREEN);
                    labelMessage.setText("Login successful! Welcome, Admin");
                    JOptionPane.showMessageDialog(null, "Login Successful! Welcome Admin", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else if (enteredUsername.equals(teacherUsername) && enteredPassword.equals(teacherPassword)) {
                    labelMessage.setForeground(Color.GREEN);
                    labelMessage.setText("Login successful! Welcome, Teacher");
                    new TeacherDashboard(enteredUsername).setVisible(true);
                    dispose();
                } else if (enteredUsername.equals(studentUsername) && enteredPassword.equals(studentPassword)) {
                    labelMessage.setForeground(Color.GREEN);
                    labelMessage.setText("Login successful! Welcome, Student");
                    
                    // Open dashboard with studentName and studentId
                    new StudentDashboard(studentName, studentId).setVisible(true);
                    dispose();
                } else {
                    labelMessage.setForeground(Color.RED);
                    labelMessage.setText("Invalid username or password.");
                }
            }
        });

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonLogin);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
