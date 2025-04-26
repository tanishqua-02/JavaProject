import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Date;

public class StudentListTable extends JFrame {

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    private static final String DB_USER = "hr";
    private static final String DB_PASSWORD = "hr";

    public StudentListTable() {
        setTitle("Student List & Attendance");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add Attendance column
        String[] columnNames = {"Student ID", "Name", "Department", "Email", "Phone", "Course", "Enroll Date", "Age", "Birth Date", "Present"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 9 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Only attendance checkbox is editable
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton submitAttendanceBtn = new JButton("Submit Attendance");
        add(submitAttendanceBtn, BorderLayout.SOUTH);

        // Fetch and populate student data
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "SELECT * FROM students";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("course"),
                    rs.getDate("enroll_date"),
                    rs.getInt("age"),
                    rs.getDate("birth_date"),
                    false  // Default attendance is unchecked
                };
                model.addRow(row);
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Attendance submission logic
        submitAttendanceBtn.addActionListener(e -> {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                String insertQuery = "INSERT INTO attendance (student_id, attendance_date, status) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertQuery);

                Date today = new Date();
                java.sql.Date sqlDate = new java.sql.Date(today.getTime());

                for (int i = 0; i < model.getRowCount(); i++) {
                    int studentId = Integer.parseInt(model.getValueAt(i, 0).toString());
                    boolean isPresent = (Boolean) model.getValueAt(i, 9);

                    pstmt.setInt(1, studentId);
                    pstmt.setDate(2, sqlDate);
                    pstmt.setString(3, isPresent ? "Present" : "Absent");
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
                conn.close();

                JOptionPane.showMessageDialog(this, "Attendance submitted successfully!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error submitting attendance: " + ex.getMessage(), "Submission Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
