import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard(String username, String role) {
        setTitle("Dashboard - " + role.toUpperCase());
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome " + username + " (" + role + ")", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();

        // Role-based buttons
        if (role.equals("admin")) {
            JButton addStudentBtn = new JButton("Add Student");
            buttonPanel.add(addStudentBtn);
            addStudentBtn.addActionListener(e -> new AddStudent());


            JButton viewStudentsBtn = new JButton("View Students");
            buttonPanel.add(viewStudentsBtn);

            // Add listeners here if needed later
        } else if (role.equals("teacher")) {
            JButton markAttendanceBtn = new JButton("Mark Attendance");
            buttonPanel.add(markAttendanceBtn);
            markAttendanceBtn.addActionListener(e -> new MarkAttendance());

        }

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
