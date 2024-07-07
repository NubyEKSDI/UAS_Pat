package org.example.ui;

import org.example.api.ApiUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EditQuantityPage extends JFrame {
    private JTextField equipmentIdField;
    private JTextField quantityField;
    private JButton addButton;
    private JButton reduceButton;

    public EditQuantityPage(AdminPage adminPage) {
        setTitle("Edit Quantity");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        equipmentIdField = new JTextField(15);
        quantityField = new JTextField(15);
        addButton = new JButton("Add");
        reduceButton = new JButton("Reduce");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Equipment ID:"));
        panel.add(equipmentIdField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(addButton);
        panel.add(reduceButton);

        add(panel);

        addButton.addActionListener(new AddQuantityAction(adminPage));
        reduceButton.addActionListener(new ReduceQuantityAction(adminPage));

        setVisible(true);
    }

    private class AddQuantityAction implements ActionListener {
        private final AdminPage adminPage;

        public AddQuantityAction(AdminPage adminPage) {
            this.adminPage = adminPage;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int equipmentId = Integer.parseInt(equipmentIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            try {
                ApiUtil.updateEquipmentQuantity(equipmentId, quantity, true);
                adminPage.loadEquipmentData();
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private class ReduceQuantityAction implements ActionListener {
        private final AdminPage adminPage;

        public ReduceQuantityAction(AdminPage adminPage) {
            this.adminPage = adminPage;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int equipmentId = Integer.parseInt(equipmentIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            try {
                ApiUtil.updateEquipmentQuantity(equipmentId, quantity, false);
                adminPage.loadEquipmentData();
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
