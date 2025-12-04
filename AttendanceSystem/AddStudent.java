import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddStudent extends JFrame {

    JTextField nameField, rollNoField, classField;

    public AddStudent() {
        setTitle("Add Student");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new JLabel("Student Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Roll Number:"));
        rollNoField = new JTextField();
        add(rollNoField);

        add(new JLabel("Class:"));
        classField = new JTextField();
        add(classField);

        JButton addButton = new JButton("Add Student");
        add(addButton);

        JLabel statusLabel = new JLabel("");
        add(statusLabel);

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String roll = rollNoField.getText();
            String sclass = classField.getText();

            if (name.isEmpty() || roll.isEmpty() || sclass.isEmpty()) {
                statusLabel.setText("⚠️ Fill all fields.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO students (name, roll_no, class) VALUES (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, roll);
                pst.setString(3, sclass);

                int result = pst.executeUpdate();
                if (result > 0) {
                    statusLabel.setText("✅ Student added!");
                    nameField.setText("");
                    rollNoField.setText("");
                    classField.setText("");
                } else {
                    statusLabel.setText("❌ Error!");
                }
            } catch (SQLException ex) {
                statusLabel.setText("❌ DB Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new AddStudent();
    }
}
