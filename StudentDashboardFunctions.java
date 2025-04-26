import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.sql.*;

public class StudentDashboardFunctions {

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    private static final String DB_USER = "hr";
    private static final String DB_PASSWORD = "hr";

    public static void viewCourseMaterials() {
        JFrame frame = new JFrame("Course Materials");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(new String[]{"File Name"}, 0);
        JTable table = new JTable(model);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT file_name FROM materials";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("file_name")});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading materials: " + e.getMessage());
        }

        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }

    public static void viewAttendance(int studentId) {
        JFrame frame = new JFrame("Attendance Record");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Status"}, 0);
        JTable table = new JTable(model);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT attendance_date, status FROM attendance WHERE student_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{rs.getDate("attendance_date"), rs.getString("status")});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error fetching attendance: " + e.getMessage());
        }

        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }

    public static void uploadAssignment(int studentId) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 FileInputStream fis = new FileInputStream(selectedFile)) {

                PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO assignments (student_id, file_name, file_data) VALUES (?, ?, ?)");
                pstmt.setInt(1, studentId);
                pstmt.setString(2, selectedFile.getName());
                pstmt.setBinaryStream(3, fis, (int) selectedFile.length());
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Assignment uploaded successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error uploading assignment: " + ex.getMessage());
            }
        }
    }

    public static void viewGrades(int studentId) {
        JFrame frame = new JFrame("Grades");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Assignment", "Grade"}, 0);
        JTable table = new JTable(model);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT file_name, grade FROM assignments WHERE student_id = ?");
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("file_name"), rs.getString("grade")});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error fetching grades: " + e.getMessage());
        }

        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }

    public static void viewFeeDetails(int studentId) {
        JFrame frame = new JFrame("Fee Details");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM fees WHERE student_id = ?");
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String info = "Total Fee: " + rs.getDouble("total_fee") +
                              "\nPaid: " + rs.getDouble("paid") +
                              "\nDue: " + rs.getDouble("due");
                textArea.setText(info);
            } else {
                textArea.setText("No fee details found.");
            }
        } catch (Exception e) {
            textArea.setText("Error fetching fee details: " + e.getMessage());
        }

        frame.add(new JScrollPane(textArea));
        frame.setVisible(true);
    }
}
