import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewAttendance extends JFrame {

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    private static final String DB_USER = "hr";
    private static final String DB_PASSWORD = "hr";

    public ViewAttendance() {
        setTitle("View Attendance Records");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Column names for the table
        String[] columnNames = {"Attendance ID", "Student ID", "Student Name", "Attendance Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch attendance data with student names
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "SELECT a.attendance_id, a.student_id, s.name, a.attendance_date, a.status " +
                           "FROM attendance a JOIN students s ON a.student_id = s.student_id " +
                           "ORDER BY a.attendance_date DESC, s.name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("attendance_id"),
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getDate("attendance_date"),
                    rs.getString("status")
                };
                model.addRow(row);
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading attendance data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
