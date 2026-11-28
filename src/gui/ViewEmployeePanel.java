package gui;
import model.*;
import service.*;
import storage.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel to view all employees in a JTable with calculated salaries.
 */
public class ViewEmployeePanel extends JPanel {

    private final MainDashboard dashboard;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {
        "ID", "Full Name", "Age", "Department", "Position", "Type", "Salary (EGP)"
    };

    public ViewEmployeePanel(MainDashboard dashboard) {
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

        JLabel title = new JLabel("All Employees");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> refreshTable());
        styleButton(refreshBtn, new Color(33, 97, 140));

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(refreshBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Right-align salary column
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);

        // Status label
        JLabel countLabel = new JLabel();
        countLabel.setBorder(BorderFactory.createEmptyBorder(0, 22, 10, 0));
        add(countLabel, BorderLayout.SOUTH);

        // Load on first show
        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Employee> list = dashboard.getEmployeeManager().getAllEmployees();
        for (Employee e : list) {
            tableModel.addRow(new Object[]{
                e.getEmp_ID(),
                e.getFullname(),
                e.getAge(),
                e.getDepartment(),
                e.getPosition(),
                e.getType(),
                String.format("%.2f", e.calculateSalary())
            });
        }
    }

    /** Returns the Employee currently selected in the table, or null. */
    public Employee getSelectedEmployee() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = (int) tableModel.getValueAt(row, 0);
        return dashboard.getEmployeeManager().searchById(id);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
    }
}
