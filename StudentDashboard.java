import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDashboard extends JFrame {
    private JLabel welcomeLabel;
    private JButton[] dashboardButtons;
    private String[] buttonLabels = {
        "View Grades",
        "View Course Materials",
        "View Attendance",
        "Fee Details",
        "Personal Info",
        "Upload Assignments"
    };

    private int studentId;
    private String studentName;

    public StudentDashboard(String studentName, int studentId) {
        this.studentName = studentName;
        this.studentId = studentId;

        // Frame settings
        setTitle("Student Dashboard");
        setSize(620, 300);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Using absolute positioning

        // Welcome Label
        welcomeLabel = new JLabel("Welcome, " + studentName + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setBounds(0, 10, 600, 30);
        add(welcomeLabel);

        // Dashboard buttons
        dashboardButtons = new JButton[6];
        int x = 40, y = 60;
        int width = 160, height = 50;
        int gapX = 200, gapY = 70;

        for (int i = 0; i < buttonLabels.length; i++) {
            dashboardButtons[i] = new JButton(buttonLabels[i]);
            dashboardButtons[i].setBounds(x, y, width, height);
            dashboardButtons[i].setFont(new Font("Arial", Font.PLAIN, 13));
            dashboardButtons[i].addActionListener(new DashboardButtonListener(buttonLabels[i]));
            add(dashboardButtons[i]);

            x += gapX;
            if ((i + 1) % 3 == 0) {
                x = 40;
                y += gapY;
            }
        }

        // Log Out button
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setBounds(250, 210, 100, 30);
        logoutButton.addActionListener(e -> {
            dispose(); // Close current window
            new Login().setVisible(true); // Open Login window again
        });
        add(logoutButton);
    }

    // Listener class
    class DashboardButtonListener implements ActionListener {
        private String buttonName;

        DashboardButtonListener(String name) {
            this.buttonName = name;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (buttonName) {
                case "View Grades":
                    StudentDashboardFunctions.viewGrades(studentId);
                    break;
                case "View Course Materials":
                    StudentDashboardFunctions.viewCourseMaterials();
                    break;
                case "View Attendance":
                    StudentDashboardFunctions.viewAttendance(studentId);
                    break;
                case "Fee Details":
                    StudentDashboardFunctions.viewFeeDetails(studentId);
                    break;
                case "Upload Assignments":
                    StudentDashboardFunctions.uploadAssignment(studentId);
                    break;
                case "Personal Info":
                    new StudentDetailsPage(studentId);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, buttonName + " clicked!");
            }
        }
    }

    // Main method for test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentDashboard("Student", 1).setVisible(true);
        });
    }
}
