import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class MarkAttendance extends JFrame {

    JComboBox<String> classDropdown;
    JPanel studentPanel;
    Map<Integer, JRadioButton[]> attendanceMap = new HashMap<>();

    public MarkAttendance() {
        setTitle("Mark Attendance");
        setSize(600, 500);
        setLayout(new BorderLayout());

        // Top: Class dropdown
        JPanel topPanel = new JPanel();
        classDropdown = new JComboBox<>();
        topPanel.add(new JLabel("Select Class:"));
        topPanel.add(classDropdown);
        JButton loadBtn = new JButton("Load Students");
        topPanel.add(loadBtn);
        add(topPanel, BorderLayout.NORTH);

        // Center: Students list
        studentPanel = new JPanel();
        studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(studentPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: Submit button
        JButton submitBtn = new JButton("Submit Attendance");
        add(submitBtn, BorderLayout.SOUTH);

        loadClasses();

        loadBtn.addActionListener(e -> loadStudents());

        submitBtn.addActionListener(e -> submitAttendance());

        setVisible(true);
    }

    private void loadClasses() {
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT DISTINCT class FROM students");
            while (rs.next()) {
                classDropdown.addItem(rs.getString("class"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadStudents() {
        studentPanel.removeAll();
        attendanceMap.clear();
        String selectedClass = (String) classDropdown.getSelectedItem();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM students WHERE class = ?");
            pst.setString(1, selectedClass);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String roll = rs.getString("roll_no");

                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.add(new JLabel(roll + " - " + name));

                JRadioButton present = new JRadioButton("Present");
                JRadioButton absent = new JRadioButton("Absent");
                ButtonGroup group = new ButtonGroup();
                group.add(present);
                group.add(absent);

                row.add(present);
                row.add(absent);
                studentPanel.add(row);

                attendanceMap.put(id, new JRadioButton[]{present, absent});
            }

            studentPanel.revalidate();
            studentPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void submitAttendance() {
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            for (Map.Entry<Integer, JRadioButton[]> entry : attendanceMap.entrySet()) {
                int studentId = entry.getKey();
                String status = entry.getValue()[0].isSelected() ? "Present" : "Absent";

                pst.setInt(1, studentId);
                pst.setDate(2, currentDate);
                pst.setString(3, status);
                pst.addBatch();
            }

            pst.executeBatch();
            JOptionPane.showMessageDialog(this, "✅ Attendance submitted!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MarkAttendance();
    }
}
