import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GradeAssignment extends JFrame {
    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    private static final String DB_USER = "hr";
    private static final String DB_PASSWORD = "hr";

    public GradeAssignment() {
        setTitle("Grade Assignments");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table for showing assignments
        DefaultTableModel model = new DefaultTableModel(new String[]{"Student Name", "Assignment Title", "Upload Date", "Grade"}, 0);
        JTable assignmentsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(assignmentsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load assignments into the table
        loadAssignments(model);

        // Grade button for grading selected assignments
        JButton gradeButton = new JButton("Grade Selected Assignment");
        add(gradeButton, BorderLayout.SOUTH);

        gradeButton.addActionListener(e -> {
            int selectedRow = assignmentsTable.getSelectedRow();
            if (selectedRow != -1) {
                String studentName = (String) assignmentsTable.getValueAt(selectedRow, 0);
                String assignmentTitle = (String) assignmentsTable.getValueAt(selectedRow, 1);
                String grade = JOptionPane.showInputDialog(this, "Enter grade for " + studentName + "'s assignment (" + assignmentTitle + "):");
                
                if (grade != null && !grade.isEmpty()) {
                    int assignmentId = (int) assignmentsTable.getValueAt(selectedRow, 3);
                    gradeAssignment(assignmentId, grade);
                    JOptionPane.showMessageDialog(this, "Grade submitted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a valid grade.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an assignment to grade.");
            }
        });
    }

    private void loadAssignments(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT a.student_id, a.file_name, a.upload_date, a.assignment_id " +
                           "FROM assignments a JOIN students s ON a.student_id = s.student_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String studentName = getStudentName(studentId);
                String assignmentTitle = rs.getString("file_name");
                Date uploadDate = rs.getDate("upload_date");

                model.addRow(new Object[]{studentName, assignmentTitle, uploadDate, rs.getInt("assignment_id")});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading assignments: " + e.getMessage());
        }
    }

    private String getStudentName(int studentId) {
        String studentName = "";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT name FROM students WHERE student_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                studentName = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentName;
    }

    private void gradeAssignment(int assignmentId, String grade) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE assignments SET grade = ? WHERE assignment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, grade);
            pstmt.setInt(2, assignmentId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error grading assignment: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradeAssignment().setVisible(true));
    }
}
