package gui;
import model.*;
import service.*;
import storage.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel for managing departments: add, delete, view.
 */
public class DepartmentPanel extends JPanel {

    private final MainDashboard dashboard;

    private JTextField idField, nameField;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {"ID", "Department Name"};

    public DepartmentPanel(MainDashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241));
        buildUI();
    }

    private void buildUI() {
        JLabel title = new JLabel("Department Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        add(title, BorderLayout.NORTH);

        // Add form
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Department"));

        formPanel.add(new JLabel("Department ID:"));
        idField = new JTextField(8);
        formPanel.add(idField);

        formPanel.add(new JLabel("Department Name:"));
        nameField = new JTextField(16);
        formPanel.add(nameField);

        JButton addBtn = new JButton("Add Department");
        addBtn.addActionListener(e -> addDepartment());
        styleButton(addBtn, new Color(39, 174, 96));
        formPanel.add(addBtn);

        add(formPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        // Delete button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 8));
        bottomPanel.setOpaque(false);
        JButton deleteBtn = new JButton("🗑 Delete Selected");
        deleteBtn.addActionListener(e -> deleteDepartment());
        styleButton(deleteBtn, new Color(192, 57, 43));
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void addDepartment() {
        String idText = idField.getText().trim();
        String name   = nameField.getText().trim();

        if (idText.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both ID and Name are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            if (dashboard.getDepartmentManager().isDuplicate(id)) {
                JOptionPane.showMessageDialog(this, "Department ID " + id + " already exists.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dashboard.getDepartmentManager().isNameDuplicate(name)) {
                JOptionPane.showMessageDialog(this, "Department name '" + name + "' already exists (case-insensitive).", "Duplicate Name", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dashboard.getDepartmentManager().addDepartment(id, name);
            JOptionPane.showMessageDialog(this, "Department added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            idField.setText(""); nameField.setText("");
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Department ID must be an integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDepartment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a department to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id     = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete department '" + name + "' (ID: " + id + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            dashboard.getDepartmentManager().deleteDepartment(id);
            JOptionPane.showMessageDialog(this, "Department deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Department d : dashboard.getDepartmentManager().getAllDepartments()) {
            tableModel.addRow(new Object[]{d.getDepartmentId(), d.getDepartmentName()});
        }
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
    }
}
