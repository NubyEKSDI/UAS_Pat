package org.example.ui;

import org.example.api.ApiUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AddNewEquipmentPage extends JFrame {
    private JTextField nameField;
    private JTextField quantityField;
    private JButton submitButton;

    public AddNewEquipmentPage(AdminPage adminPage) {
        setTitle("Add New Equipment");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        nameField = new JTextField(15);
        quantityField = new JTextField(15);
        submitButton = new JButton("Submit");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(submitButton);

        add(panel);

        submitButton.addActionListener(new SubmitAction(adminPage));

        setVisible(true);
    }

    private class SubmitAction implements ActionListener {
        private final AdminPage adminPage;

        public SubmitAction(AdminPage adminPage) {
            this.adminPage = adminPage;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            try {
                ApiUtil.addEquipment(name, quantity);
                adminPage.loadEquipmentData();
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
