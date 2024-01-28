import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Netbankinterface {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Netbankinterface window = new Netbankinterface();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Netbankinterface() {
        initialize();
       // showDashboard();
    }

    private void initialize() {
        frame = new JFrame("NetBanking Interface");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 50, 80, 25);
        frame.getContentPane().add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(140, 50, 200, 25);
        frame.getContentPane().add(usernameField);
        usernameField.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 90, 80, 25);
        frame.getContentPane().add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 90, 200, 25);
        frame.getContentPane().add(passwordField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 130, 100, 25);
        frame.getContentPane().add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add authentication logic here
                // For simplicity, let's assume the login is successful
                showDashboard();
            }
        });
    }

    private void showDashboard() {
        frame.getContentPane().removeAll();
        frame.repaint();

        JLabel lblWelcome = new JLabel("Welcome to NetBanking!");
        lblWelcome.setBounds(120, 50, 200, 25);
        frame.getContentPane().add(lblWelcome);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(150, 90, 100, 25);
        frame.getContentPane().add(btnLogout);

        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add logout logic here
                // For simplicity, go back to login screen
                initialize();
            }
        });
    }
}
