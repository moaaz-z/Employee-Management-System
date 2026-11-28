package gui;
import model.*;
import service.*;
import storage.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel for searching employees by ID or filtering by department.
 * Also provides Delete functionality from search results.
 */
public class SearchPanel extends JPanel {

    private final MainDashboard dashboard;

    private JTextField idField;
    private JComboBox<String> deptCombo;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {
        "ID", "Full Name", "Age", "Department", "Position", "Type", "Salary (EGP)"
    };

    public SearchPanel(MainDashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241));
        buildUI();
    }

    private void buildUI() {
        // Title
        JLabel title = new JLabel("Search Employees");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        add(title, BorderLayout.NORTH);

        // Search controls
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Options"));

        searchPanel.add(new JLabel("Search by ID:"));
        idField = new JTextField(10);
        searchPanel.add(idField);
        JButton searchIdBtn = new JButton("Search");
        searchIdBtn.addActionListener(e -> searchById());
        styleButton(searchIdBtn, new Color(33, 97, 140));
        searchPanel.add(searchIdBtn);

        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(new JLabel("Filter by Department:"));

        String[] depts = buildDeptArray();
        deptCombo = new JComboBox<>(depts);
        searchPanel.add(deptCombo);
        JButton filterBtn = new JButton("Filter");
        filterBtn.addActionListener(e -> filterByDept());
        styleButton(filterBtn, new Color(39, 174, 96));
        searchPanel.add(filterBtn);

        JButton showAllBtn = new JButton("Show All");
        showAllBtn.addActionListener(e -> showAll());
        searchPanel.add(showAllBtn);

        add(searchPanel, BorderLayout.NORTH);

        // Results table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        // Delete button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 8));
        bottomPanel.setOpaque(false);
        JButton deleteBtn = new JButton("🗑 Delete Selected");
        deleteBtn.addActionListener(e -> deleteSelected());
        styleButton(deleteBtn, new Color(192, 57, 43));
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        showAll();
    }

    private String[] buildDeptArray() {
        String[] depts = dashboard.getDepartmentManager().getDepartmentNames();
        String[] all = new String[depts.length + 1];
        all[0] = "-- All Departments --";
        System.arraycopy(depts, 0, all, 1, depts.length);
        return all;
    }

    private void searchById() {
        String text = idField.getText().trim();
        if (text.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter an employee ID.", "Input Required", JOptionPane.WARNING_MESSAGE); return; }
        try {
            int id = Integer.parseInt(text);
            Employee e = dashboard.getEmployeeManager().searchById(id);
            tableModel.setRowCount(0);
            if (e == null) {
                JOptionPane.showMessageDialog(this, "No employee found with ID " + id + ".", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            } else {
                addRow(e);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID must be an integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterByDept() {
        String selected = (String) deptCombo.getSelectedItem();
        if (selected == null || selected.startsWith("--")) { showAll(); return; }
        ArrayList<Employee> results = dashboard.getEmployeeManager().filterByDepartment(selected);
        tableModel.setRowCount(0);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees in department: " + selected, "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
        for (Employee e : results) addRow(e);
    }

    public void refreshDepartments() {
        String previous = (String) deptCombo.getSelectedItem();
        String[] depts = buildDeptArray();
        deptCombo.setModel(new DefaultComboBoxModel<>(depts));
        if (previous != null) {
            deptCombo.setSelectedItem(previous);
        }
    }

    private void showAll() {
        tableModel.setRowCount(0);
        // Refresh dept combo
        deptCombo.setModel(new DefaultComboBoxModel<>(buildDeptArray()));
        for (Employee e : dashboard.getEmployeeManager().getAllEmployees()) addRow(e);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete employee ID " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            dashboard.getEmployeeManager().deleteEmployee(id);
            JOptionPane.showMessageDialog(this, "Employee deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            showAll();
        }
    }

    private void addRow(Employee e) {
        tableModel.addRow(new Object[]{
            e.getEmp_ID(), e.getFullname(), e.getAge(),
            e.getDepartment(), e.getPosition(), e.getType(),
            String.format("%.2f", e.calculateSalary())
        });
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
    }
}
