package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    private  JTextField username;
    private JPasswordField password;////the diff that it hides the text that is written by dots
    private static final String validUsername="admin";
    private static final String validPassword="admin";
    public LoginForm(){
        setTitle("Employee Management System — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 260);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();   
    }
    private void buildUI(){
        JPanel headerPanel=new JPanel();
        headerPanel.setBackground(new Color(45, 85, 145)); 
        headerPanel.setPreferredSize(new Dimension(400, 60));
        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.white);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 18));
        headerPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);//gap around each item (6 pixels)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        username = new JTextField(15);
        formPanel.add(username, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        password = new JPasswordField(15);
        formPanel.add(password, gbc);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton loginBtn = new JButton("Login");
        JButton exitBtn  = new JButton("Exit");

        loginBtn.setBackground(new Color(33, 97, 140));
        loginBtn.setForeground(Color.black);
        loginBtn.setFocusPainted(false);
        
        exitBtn.addActionListener(e -> System.exit(0));
        //loginBtn.addActionListener(e -> handleLogin());
        exitBtn.addActionListener(e -> System.exit(0));
        //password.addActionListener(e -> handleLogin());
        btnPanel.add(loginBtn);
        btnPanel.add(exitBtn);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
}
