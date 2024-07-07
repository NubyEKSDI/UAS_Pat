package org.example.ui;

import org.example.api.ApiUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SignUpPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JButton loginButton;

    public SignUpPage() {
        setTitle("Sign Up");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        signUpButton = new JButton("Sign Up");
        loginButton = new JButton("Login");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(signUpButton);
        panel.add(loginButton);

        add(panel);

        signUpButton.addActionListener(new SignUpAction());
        loginButton.addActionListener(new LoginAction());

        setVisible(true);
    }

    private class SignUpAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            try {
                ApiUtil.signUp(username, password);
                JOptionPane.showMessageDialog(null, "User registered successfully");
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(null, "User registration failed");
            }
        }
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new LoginPage();
        }
    }
}
