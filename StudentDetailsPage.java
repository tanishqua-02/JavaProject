import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentDetailsPage extends JFrame {

    JLabel labelTitle, labelName, labelId, labelDept, labelEmail, labelPhone,
           labelCourse, labelEnrollDate, labelAge, labelBirthDate;

    // Oracle DB credentials
    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    private static final String DB_USER = "hr";
    private static final String DB_PASSWORD = "hr";

    public StudentDetailsPage(int studentId) {
        setTitle("Student Details");
        setSize(400, 450);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Title
        labelTitle = new JLabel("Student Details");
        labelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitle.setBounds(120, 10, 200, 30);
        add(labelTitle);

        // Fields
        labelName = createLabel("Name: ", 60);
        labelId = createLabel("Student ID: ", 100);
        labelDept = createLabel("Department: ", 140);
        labelEmail = createLabel("Email: ", 180);
        labelPhone = createLabel("Phone No: ", 220);
        labelCourse = createLabel("Course: ", 260);
        labelEnrollDate = createLabel("Enrollment Date: ", 300);
        labelAge = createLabel("Age: ", 340);
        labelBirthDate = createLabel("Birthdate: ", 380);

        // Fetch data from DB
        fetchStudentDetails(studentId);

        setVisible(true);
    }

    private JLabel createLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(30, y, 350, 25);
        add(label);
        return label;
    }

    private void fetchStudentDetails(int studentId) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "SELECT * FROM students WHERE student_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, studentId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                labelName.setText("Name: " + rs.getString("name"));
                labelId.setText("Student ID: " + rs.getInt("student_id"));
                labelDept.setText("Department: " + rs.getString("department"));
                labelEmail.setText("Email: " + rs.getString("email"));
                labelPhone.setText("Phone No: " + rs.getString("phone"));
                labelCourse.setText("Course: " + rs.getString("course"));
                labelEnrollDate.setText("Enrollment Date: " + rs.getDate("enroll_date"));
                labelAge.setText("Age: " + rs.getInt("age"));
                labelBirthDate.setText("Birthdate: " + rs.getDate("birth_date"));
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.");
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new StudentDetailsPage(1); // Replace with valid student_id
    }
}
