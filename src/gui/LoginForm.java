package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import storage.DataStorage;
import storage.FileHandler;
public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField; //the diff that it hides the text that is written by dots

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    public LoginForm() {
        setTitle("HR System — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 260);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
  
        JPanel headerPanel = new JPanel();
       headerPanel.setBackground(new Color(45, 85, 145)); 
        headerPanel.setPreferredSize(new Dimension(400, 60));
        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.white);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 18));
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(2, 6, 6, 6);
        JCheckBox showPasswordCheck = new JCheckBox("Show password");
        showPasswordCheck.setFont(new Font("SansSerif", Font.PLAIN, 12));
        showPasswordCheck.setForeground(new Color(44, 62, 80));
        showPasswordCheck.setOpaque(false);
        showPasswordCheck.setFocusPainted(false);
        final char defaultEcho = passwordField.getEchoChar();
        showPasswordCheck.addActionListener(e ->
                passwordField.setEchoChar(showPasswordCheck.isSelected() ? (char) 0 : defaultEcho)
        );
        formPanel.add(showPasswordCheck, gbc);


        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton loginBtn = new JButton("Login");
        JButton exitBtn  = new JButton("Exit");

        loginBtn.setBackground(new Color(33, 97, 140));
        loginBtn.setForeground(Color.black);
        loginBtn.setFocusPainted(false);

        loginBtn.addActionListener(e -> handleLogin());
        exitBtn.addActionListener(e -> System.exit(0));


        passwordField.addActionListener(e -> handleLogin());

        btnPanel.add(loginBtn);
        btnPanel.add(exitBtn);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            dispose();
            DataStorage storage = new FileHandler("employees.dat", "departments.dat");
            MainDashboard dashboard = new MainDashboard(storage);
            dashboard.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}
