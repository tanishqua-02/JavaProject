import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherDashboard extends JFrame {
    private JLabel welcomeLabel;
    private JButton[] dashboardButtons;
    private String[] buttonLabels = {
        "Manage Courses",
        "Upload Materials",
        "View Attendance",
        "Grade Assignments",
        "Student List",
        "Log Out"
    };

    public TeacherDashboard(String teacherName) {
        // Frame setup
        setTitle("Teacher Dashboard");
        setSize(620, 300);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + teacherName + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setBounds(0, 10, 600, 30);
        add(welcomeLabel);

        // Buttons
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
    }

    // Action handler for buttons
    class DashboardButtonListener implements ActionListener {
        private String buttonName;

        DashboardButtonListener(String name) {
            this.buttonName = name;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonName.equals("Log Out")) {
                dispose();
                new Login().setVisible(true);
            }
            else if (buttonName.equals("Student List")) {
                new StudentListTable().setVisible(true);
            } else if (buttonName.equals("Upload Materials")) {
                new UploadMaterials().setVisible(true);
            } else if (buttonName.equals("Grade Assignments")) {
                new GradeAssignment().setVisible(true);
            } else if (buttonName.equals("View Attendance")) {
                new ViewAttendance().setVisible(true);
            }
else {
                JOptionPane.showMessageDialog(null,
                    buttonName + " clicked!",
                    "Feature Access",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TeacherDashboard("Professor").setVisible(true));
    }
}
