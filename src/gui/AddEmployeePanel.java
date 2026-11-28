package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.*;
import service.*;
import storage.*;

/**
 * Panel for adding new employees (Full-Time or Part-Time).
 * Dynamically shows salary fields based on selected employee type.
 */
public class AddEmployeePanel extends JPanel {

    private final MainDashboard dashboard;

    // Common fields
    private JTextField nameField, ageField, positionField;
    private JComboBox<String> typeCombo, deptCombo;

    // Full-Time fields
    private JTextField basicSalaryField, bonusField;
    private JPanel fullTimePanel;

    // Part-Time fields
    private JTextField hourlyRateField, hoursWorkedField;
    private JPanel partTimePanel;

    public AddEmployeePanel(MainDashboard dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241));
        buildUI();
    }

    private void buildUI() {
        // Title + Refresh button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        JLabel title = new JLabel("Add New Employee");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshDepartments());
        styleButton(refreshBtn, new Color(33, 97, 140));

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(refreshBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // ID
        addRow(form, gbc, row++, "Employee ID:", new JLabel("Assigned automatically"));

        // Name
        addRow(form, gbc, row++, "Full Name:", nameField = new JTextField(20));

        // Age
        addRow(form, gbc, row++, "Age:", ageField = new JTextField(20));

        // Department combo
        deptCombo = new JComboBox<>(dashboard.getDepartmentManager().getDepartmentNames());
        addRow(form, gbc, row++, "Department:", deptCombo);

        // Position
        addRow(form, gbc, row++, "Position:", positionField = new JTextField(20));

        // Employee type
        typeCombo = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});
        typeCombo.addActionListener(e -> toggleSalaryFields());
        addRow(form, gbc, row++, "Employee Type:", typeCombo);

        // --- Full-Time panel ---
        fullTimePanel = new JPanel(new GridBagLayout());
        fullTimePanel.setOpaque(false);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4, 8, 4, 8);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.anchor = GridBagConstraints.WEST;
        basicSalaryField = new JTextField(20);
        bonusField = new JTextField(20);
        addRow(fullTimePanel, gbc2, 0, "Basic Salary (EGP):", basicSalaryField);
        addRow(fullTimePanel, gbc2, 1, "Bonus (EGP):", bonusField);

        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        form.add(fullTimePanel, gbc);
        gbc.gridwidth = 1;

        // --- Part-Time panel ---
        partTimePanel = new JPanel(new GridBagLayout());
        partTimePanel.setOpaque(false);
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(4, 8, 4, 8);
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.anchor = GridBagConstraints.WEST;
        hourlyRateField  = new JTextField(20);
        hoursWorkedField = new JTextField(20);
        addRow(partTimePanel, gbc3, 0, "Hourly Rate (EGP):", hourlyRateField);
        addRow(partTimePanel, gbc3, 1, "Hours Worked / Month:", hoursWorkedField);

        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        form.add(partTimePanel, gbc);
        gbc.gridwidth = 1;

        partTimePanel.setVisible(false); // default show Full-Time

        // Scroll pane for form
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setOpaque(false);
        JButton saveBtn  = new JButton("Save Employee");
        saveBtn.setForeground(Color.BLACK);
        JButton clearBtn = new JButton("Clear");

        styleButton(saveBtn, new Color(33, 97, 140));
        clearBtn.addActionListener(e -> clearFields());
        saveBtn.addActionListener(e -> saveEmployee());

        btnPanel.add(saveBtn);
        btnPanel.add(clearBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void toggleSalaryFields() {
        boolean isFull = typeCombo.getSelectedItem().equals("Full-Time");
        fullTimePanel.setVisible(isFull);
        partTimePanel.setVisible(!isFull);
        revalidate(); repaint();
    }

    public void refreshDepartments() {
        String previous = (String) deptCombo.getSelectedItem();
        String[] depts = dashboard.getDepartmentManager().getDepartmentNames();
        deptCombo.setModel(new DefaultComboBoxModel<>(depts));
        if (previous != null) {
            for (String d : depts) {
                if (d.equals(previous)) {
                    deptCombo.setSelectedItem(previous);
                    break;
                }
            }
        }
    }

    private void saveEmployee() {
        try {
            String name     = nameField.getText().trim();
            String ageText  = ageField.getText().trim();
            String position = positionField.getText().trim();
            String dept     = (String) deptCombo.getSelectedItem();
            String type     = (String) typeCombo.getSelectedItem();

            if (name.isEmpty() || ageText.isEmpty() || position.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (deptCombo.getItemCount() == 0 || dept == null) {
                JOptionPane.showMessageDialog(this, "No departments available. Please add a department first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Department department = dashboard.getDepartmentManager().getDepartmentByName(dept);
            if (department == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid department.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageText);

            if (age <= 0) { JOptionPane.showMessageDialog(this, "Age must be a positive integer.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }

            if (type.equals("Full-Time")) {
                String basicText = basicSalaryField.getText().trim();
                String bonusText = bonusField.getText().trim();
                if (basicText.isEmpty() || bonusText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Basic salary and bonus are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double basic = Double.parseDouble(basicText);
                double bonus = Double.parseDouble(bonusText);
                if (basic < 0 || bonus < 0) { JOptionPane.showMessageDialog(this, "Salary values must be non-negative.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
                dashboard.getEmployeeManager().addFullTimeEmployee(name, age, position, department, basic, bonus);
            } else {
                String rateText  = hourlyRateField.getText().trim();
                String hoursText = hoursWorkedField.getText().trim();
                if (rateText.isEmpty() || hoursText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Hourly rate and hours worked are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double rate  = Double.parseDouble(rateText);
                double hours = Double.parseDouble(hoursText);
                if (rate < 0 || hours < 0) { JOptionPane.showMessageDialog(this, "Rate and hours must be non-negative.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
                dashboard.getEmployeeManager().addPartTimeEmployee(name, age, position, department, rate, hours);
            }

            JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID, Age, and salary fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText(""); ageField.setText("");
        positionField.setText(""); basicSalaryField.setText(""); bonusField.setText("");
        hourlyRateField.setText(""); hoursWorkedField.setText("");
        if (deptCombo.getItemCount() > 0) {
            deptCombo.setSelectedIndex(0);
        }
        typeCombo.setSelectedIndex(0);
        toggleSalaryFields();
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
    }
}