package org.example.ui;

import org.example.api.ApiUtil;
import org.example.model.Equipment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class AdminPage extends JFrame {
    private JTable equipmentTable;
    private JButton editQuantityButton;
    private JButton addNewButton;
    private JButton deleteButton;
    private JButton checkRecordButton;
    private JButton logoutButton;
    private DefaultTableModel tableModel;

    public AdminPage() {
        setTitle("Admin Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        equipmentTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Quantity"}, 0);
        equipmentTable.setModel(tableModel);

        editQuantityButton = new JButton("Edit Quantity");
        addNewButton = new JButton("Add New");
        deleteButton = new JButton("Delete");
        checkRecordButton = new JButton("Check Record");
        logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editQuantityButton);
        buttonPanel.add(addNewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkRecordButton);
        buttonPanel.add(logoutButton);

        add(new JScrollPane(equipmentTable), "Center");
        add(buttonPanel, "South");

        loadEquipmentData();

        editQuantityButton.addActionListener(new EditQuantityAction());
        addNewButton.addActionListener(new AddNewAction());
        deleteButton.addActionListener(new DeleteAction());
        checkRecordButton.addActionListener(new CheckRecordAction());
        logoutButton.addActionListener(new LogoutAction());

        setVisible(true);
    }

    protected void loadEquipmentData() {
        try {
            List<Equipment> equipmentList = ApiUtil.getEquipment();
            tableModel.setRowCount(0);
            for (Equipment equipment : equipmentList) {
                tableModel.addRow(new Object[]{equipment.getId(), equipment.getName(), equipment.getQuantity()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EditQuantityAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new EditQuantityPage(AdminPage.this);
        }
    }

    private class AddNewAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new AddNewEquipmentPage(AdminPage.this);
        }
    }

    private class DeleteAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = equipmentTable.getSelectedRow();
            if (selectedRow != -1) {
                int equipmentId = (int) tableModel.getValueAt(selectedRow, 0);
                try {
                    ApiUtil.deleteEquipment(equipmentId);
                    loadEquipmentData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(AdminPage.this, "Please select an equipment to delete");
            }
        }
    }

    private class CheckRecordAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new BorrowRecordPage(AdminPage.this);
        }
    }

    private class LogoutAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new LoginPage();
        }
    }
}
