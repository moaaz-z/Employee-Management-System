package gui;
import model.*;
import service.*;
import storage.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel to update an existing employee's information.
 * User selects an employee from a table, edits fields, then saves.
 */
public class UpdateEmployeePanel extends JPanel {

    private final MainDashboard dashboard;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;

    // Edit form
    private JTextField nameField, ageField, positionField;
    private JComboBox<String> deptCombo, typeCombo;

    // Salary fields
    private JTextField basicSalaryField, bonusField, hourlyRateField, hoursWorkedField;
    private JPanel fullTimePanel, partTimePanel;

    private int selectedId = -1;  // tracks which employee is loaded

    private static final String[] COLUMNS = {
        "ID", "Full Name", "Department", "Type", "Salary (EGP)"
    };

    public UpdateEmployeePanel(MainDashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241));
        buildUI();
    }

    private void buildUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        JLabel title = new JLabel("Update Employee");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        JButton topRefreshBtn = new JButton("Refresh");
        topRefreshBtn.addActionListener(e -> refreshTable());
        styleButton(topRefreshBtn, new Color(33, 97, 140));

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(topRefreshBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Split: table on left, form on right
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(360);
        split.setBorder(null);

        // --- Table side ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JButton refreshBtn = new JButton("Refresh List");
        refreshBtn.addActionListener(e -> refreshTable());
        styleButton(refreshBtn, new Color(33, 97, 140));
        tablePanel.add(refreshBtn, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedEmployee();
        });

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        split.setLeftComponent(tablePanel);

        // --- Edit form side ---
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setOpaque(false);
        formWrapper.setBorder(BorderFactory.createTitledBorder("Edit Employee Details"));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        nameField     = new JTextField(18);
        ageField      = new JTextField(18);
        positionField = new JTextField(18);
        deptCombo     = new JComboBox<>(dashboard.getDepartmentManager().getDepartmentNames());
        typeCombo     = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});
        typeCombo.addActionListener(e -> toggleSalaryFields());

        addRow(form, gbc, row++, "Full Name:",     nameField);
        addRow(form, gbc, row++, "Age:",           ageField);
        addRow(form, gbc, row++, "Position:",      positionField);
        addRow(form, gbc, row++, "Department:",    deptCombo);
        addRow(form, gbc, row++, "Employee Type:", typeCombo);

        // Full-Time panel
        fullTimePanel = new JPanel(new GridBagLayout());
        fullTimePanel.setOpaque(false);
        GridBagConstraints g2 = new GridBagConstraints();
        g2.insets = new Insets(3, 8, 3, 8);
        g2.fill = GridBagConstraints.HORIZONTAL;
        basicSalaryField = new JTextField(18);
        bonusField       = new JTextField(18);
        addRow(fullTimePanel, g2, 0, "Basic Salary:", basicSalaryField);
        addRow(fullTimePanel, g2, 1, "Bonus:",        bonusField);

        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        form.add(fullTimePanel, gbc);
        gbc.gridwidth = 1;

        // Part-Time panel
        partTimePanel = new JPanel(new GridBagLayout());
        partTimePanel.setOpaque(false);
        GridBagConstraints g3 = new GridBagConstraints();
        g3.insets = new Insets(3, 8, 3, 8);
        g3.fill = GridBagConstraints.HORIZONTAL;
        hourlyRateField  = new JTextField(18);
        hoursWorkedField = new JTextField(18);
        addRow(partTimePanel, g3, 0, "Hourly Rate:",   hourlyRateField);
        addRow(partTimePanel, g3, 1, "Hours Worked:",  hoursWorkedField);

        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        form.add(partTimePanel, gbc);
        gbc.gridwidth = 1;

        partTimePanel.setVisible(false);

        formWrapper.add(new JScrollPane(form), BorderLayout.CENTER);

        // Save button
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> saveChanges());
        styleButton(saveBtn, new Color(33, 97, 140));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(saveBtn);
        formWrapper.add(btnPanel, BorderLayout.SOUTH);

        split.setRightComponent(formWrapper);
        add(split, BorderLayout.CENTER);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        deptCombo.setModel(new DefaultComboBoxModel<>(dashboard.getDepartmentManager().getDepartmentNames()));
        for (Employee e : dashboard.getEmployeeManager().getEmployees()) {
            tableModel.addRow(new Object[]{
                e.getEmp_ID(), e.getFullname(), e.getDepartment().getDepartmentName(),
                e.getType(), String.format("%.2f", e.calculateSalary())
            });
        }
        selectedId = -1;
        clearForm();
    }

    private void loadSelectedEmployee() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        Employee e = dashboard.getEmployeeManager().searchById(id);
        if (e == null) return;
        selectedId = id;

        nameField.setText(e.getFullname());
        ageField.setText(String.valueOf(e.getAge()));
        positionField.setText(e.getPosition());
        deptCombo.setSelectedItem(e.getDepartment().getDepartmentName());
        typeCombo.setSelectedItem(e instanceof FullTimeEmployee ? "Full-Time" : "Part-Time");

        if (e instanceof FullTimeEmployee fte) {
            basicSalaryField.setText(String.valueOf(fte.getBasicSalary()));
            bonusField.setText(String.valueOf(fte.getBonus()));
        } else if (e instanceof PartTimeEmployee pte) {
            hourlyRateField.setText(String.valueOf(pte.getHourlyRate()));
            hoursWorkedField.setText(String.valueOf(pte.getHoursWorked()));
        }
        toggleSalaryFields();
    }

    private void toggleSalaryFields() {
        boolean isFull = "Full-Time".equals(typeCombo.getSelectedItem());
        fullTimePanel.setVisible(isFull);
        partTimePanel.setVisible(!isFull);
        revalidate(); repaint();
    }

    private void saveChanges() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee from the list first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String name     = nameField.getText().trim();
            String ageText  = ageField.getText().trim();
            String position = positionField.getText().trim();
            String dept     = (String) deptCombo.getSelectedItem();
            String type     = (String) typeCombo.getSelectedItem();

            if (name.isEmpty() || ageText.isEmpty() || position.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, Age, and Position cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageText);
            if (age <= 0) { JOptionPane.showMessageDialog(this, "Age must be a positive integer.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }

            Department department = dashboard.getDepartmentManager().getDepartmentByName(dept);
            if (department == null) { JOptionPane.showMessageDialog(this, "Please select a valid department.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }

            Employee updated;
            if (type.equals("Full-Time")) {
                double basic = Double.parseDouble(basicSalaryField.getText().trim());
                double bonus = Double.parseDouble(bonusField.getText().trim());
                if (basic < 0 || bonus < 0) { JOptionPane.showMessageDialog(this, "Salary values must be non-negative.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
                updated = new FullTimeEmployee(selectedId, name, age, position, department, basic, bonus);
            } else {
                double rate  = Double.parseDouble(hourlyRateField.getText().trim());
                double hours = Double.parseDouble(hoursWorkedField.getText().trim());
                if (rate < 0 || hours < 0) { JOptionPane.showMessageDialog(this, "Rate and hours must be non-negative.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
                updated = new PartTimeEmployee(selectedId, name, age, position, department, rate, hours);
            }

            dashboard.getEmployeeManager().updateEmployee(selectedId, updated);
            JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText(""); ageField.setText(""); positionField.setText("");
        basicSalaryField.setText(""); bonusField.setText("");
        hourlyRateField.setText(""); hoursWorkedField.setText("");
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        panel.add(field, gbc);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
    }
}