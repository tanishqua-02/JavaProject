import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TeacherDetailsPage extends JFrame {

    // Labels to display teacher details
    JLabel labelTitle, labelName, labelId, labelDept, labelEmail, labelPhone,
           labelCourse, labelJoiningDate, labelAge, labelBirthDate;

    // Replace with your actual Oracle DB connection details
    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XE"; // or ORCL
    private static final String DB_USER = "hr";     // your DB username
    private static final String DB_PASSWORD = "hr"; // your DB password

    public TeacherDetailsPage(int teacherId) {
        setTitle("Teacher Details");
        setSize(400, 450);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Title
        labelTitle = new JLabel("Teacher Details");
        labelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitle.setBounds(120, 10, 200, 30);
        add(labelTitle);

        // Fields
        labelName = createLabel("Name: ", 60);
        labelId = createLabel("Teacher ID: ", 100);
        labelDept = createLabel("Department: ", 140);
        labelEmail = createLabel("Email: ", 180);
        labelPhone = createLabel("Phone No: ", 220);
        labelCourse = createLabel("Course: ", 260);
        labelJoiningDate = createLabel("Date of Joining: ", 300);
        labelAge = createLabel("Age: ", 340);
        labelBirthDate = createLabel("Birthdate: ", 380);

        // Fetch data from DB
        fetchTeacherDetails(teacherId);

        setVisible(true);
    }

    private JLabel createLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(30, y, 350, 25);
        add(label);
        return label;
    }

    private void fetchTeacherDetails(int teacherId) {
        try {
            // Load Oracle JDBC driver (not required for JDK 11+, but safe)
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to database
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "SELECT * FROM teachers WHERE teacher_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, teacherId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                labelName.setText("Name: " + rs.getString("name"));
                labelId.setText("Teacher ID: " + rs.getInt("teacher_id"));
                labelDept.setText("Department: " + rs.getString("department"));
                labelEmail.setText("Email: " + rs.getString("email"));
                labelPhone.setText("Phone No: " + rs.getString("phone"));
                labelCourse.setText("Course: " + rs.getString("course"));
                labelJoiningDate.setText("Date of Joining: " + rs.getDate("joining_date"));
                labelAge.setText("Age: " + rs.getInt("age"));
                labelBirthDate.setText("Birthdate: " + rs.getDate("birth_date"));
            } else {
                JOptionPane.showMessageDialog(this, "Teacher not found.");
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new TeacherDetailsPage(12345); // Replace with valid teacher_id
    }
}
