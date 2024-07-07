package org.example.ui;

import org.example.api.ApiUtil;
import org.example.model.BorrowRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.List;

public class BorrowRecordPage extends JFrame {
    private JTable borrowRecordTable;
    private DefaultTableModel tableModel;

    public BorrowRecordPage(JFrame parent) {
        setTitle("Borrow Records");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        borrowRecordTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"ID", "User ID", "Equipment ID", "Quantity", "Borrowed At", "Returned At"}, 0);
        borrowRecordTable.setModel(tableModel);

        add(new JScrollPane(borrowRecordTable), "Center");

        loadBorrowRecords();

        setVisible(true);
    }

    private void loadBorrowRecords() {
        try {
            List<BorrowRecord> borrowRecordList = ApiUtil.getBorrowRecords();
            tableModel.setRowCount(0);
            for (BorrowRecord record : borrowRecordList) {
                tableModel.addRow(new Object[]{record.getId(), record.getUser_id(), record.getEquipment_id(), record.getQuantity(), record.getBorrowed_at(), record.getReturned_at()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
