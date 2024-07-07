package org.example.ui;

import org.example.api.ApiUtil;
import org.example.model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    public LoginPage() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(signUpButton);

        add(panel);

        loginButton.addActionListener(new LoginAction());
        signUpButton.addActionListener(new SignUpAction());

        setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            try {
                User user = ApiUtil.login(username, password);
                if (user != null) {
                    dispose();
                    new AdminPage();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private class SignUpAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new SignUpPage();
        }
    }
}
