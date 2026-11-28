package gui;
import model.*;
import storage.*;
import service.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;

/**
 * Main dashboard — shown after successful login.
 * Uses CardLayout to switch between all panels.
 */
public class MainDashboard extends JFrame {

    private final EmployeeManager employeeManager;
    private final DepartmentManager departmentManager;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private AddEmployeePanel addEmployeePanel;
    private ViewEmployeePanel viewEmployeePanel;
    private SearchPanel searchPanel;
    private UpdateEmployeePanel updateEmployeePanel;

    // Home panel live components
    private JLabel totalEmpLabel;
    private JLabel empBreakdownLabel;
    private JLabel deptCountLabel;
    private JLabel payrollLabel;
    private DefaultTableModel recentTableModel;

    // Panel name constants
    public static final String HOME       = "HOME";
    public static final String ADD_EMP    = "ADD_EMP";
    public static final String VIEW_EMP   = "VIEW_EMP";
    public static final String SEARCH_EMP = "SEARCH_EMP";
    public static final String UPDATE_EMP = "UPDATE_EMP";
    public static final String DEPT_MGMT  = "DEPT_MGMT";

    public MainDashboard(DataStorage storage) {
        this.employeeManager   = new EmployeeManager(storage);
        this.departmentManager = new DepartmentManager(storage);

        setTitle("HR System — Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 640);
        setMinimumSize(new Dimension(880, 580));
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // === Top bar ===
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(33, 97, 140));
        topBar.setPreferredSize(new Dimension(900, 50));

        JLabel title = new JLabel("  HR Employee Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(192, 57, 43));
        logoutBtn.setForeground(Color.red);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutBtn.addActionListener(e -> logout());

        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        logoutWrapper.setOpaque(false);
        logoutWrapper.add(logoutBtn);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(logoutWrapper, BorderLayout.EAST);

        // === Side nav ===
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(44, 62, 80));
        navPanel.setPreferredSize(new Dimension(170, 600));

        String[][] navItems = {
            {" Home",             HOME},
            {" Add Employee",     ADD_EMP},
            {" View Employees",   VIEW_EMP},
            {" Search",           SEARCH_EMP},
            {" Update Employee",  UPDATE_EMP},
            {" Departments",      DEPT_MGMT},
        };

        for (String[] item : navItems) {// add service to the left panel
            JButton btn = makeNavButton(item[0], item[1]);
            navPanel.add(btn);
            navPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        }

//CardLayout is a Swing layout that stacks several panels on top of each other like a deck of cards, and shows only one at a time.
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);

        addEmployeePanel    = new AddEmployeePanel(this);
        viewEmployeePanel   = new ViewEmployeePanel(this);
        searchPanel         = new SearchPanel(this);
        updateEmployeePanel = new UpdateEmployeePanel(this);

        cardPanel.add(buildHomePanel(), HOME);
        cardPanel.add(addEmployeePanel, ADD_EMP);
        cardPanel.add(viewEmployeePanel, VIEW_EMP);
        cardPanel.add(searchPanel, SEARCH_EMP);
        cardPanel.add(updateEmployeePanel, UPDATE_EMP);
        cardPanel.add(new DepartmentPanel(this), DEPT_MGMT);

        add(topBar,    BorderLayout.NORTH);
        add(navPanel,  BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    private JButton makeNavButton(String label, String card) {
        JButton btn = new JButton(label);
        btn.setMaximumSize(new Dimension(170, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(new Color(44, 62, 80));
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 8));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(e -> showPanel(card));
        return btn;
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // === Top Welcome + Summary Cards ===
        JPanel topSection = new JPanel(new BorderLayout(0, 14));
        topSection.setOpaque(false);

        JPanel welcomeBox = new JPanel();
        welcomeBox.setOpaque(false);
        welcomeBox.setLayout(new BoxLayout(welcomeBox, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome, Admin");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 21));
        welcome.setForeground(new Color(33, 97, 140));

        JLabel sub = new JLabel("Summary of current employee records, departments, and monthly payroll.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(Color.DARK_GRAY);

        welcomeBox.add(welcome);
        welcomeBox.add(Box.createVerticalStrut(4));
        welcomeBox.add(sub);
        topSection.add(welcomeBox, BorderLayout.NORTH);

        // Summary stats row (3 clean cards matching app palette)
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 14, 0));
        statsRow.setOpaque(false);

        totalEmpLabel     = new JLabel("0");
        empBreakdownLabel = new JLabel("0 Full-Time, 0 Part-Time");
        deptCountLabel    = new JLabel("0");
        payrollLabel      = new JLabel("0.00 EGP");

        statsRow.add(makeStatCard("Total Employees", totalEmpLabel, empBreakdownLabel,  Color.white));
        statsRow.add(makeStatCard("Departments", deptCountLabel, new JLabel("Active departments"), Color.white));
        statsRow.add(makeStatCard("Total Monthly Payroll", payrollLabel, new JLabel("Calculated from all employees"), Color.white));

        topSection.add(statsRow, BorderLayout.CENTER);
        panel.add(topSection, BorderLayout.NORTH);

        // === Center: Recent Employees Table ===
        String[] cols = {"ID", "Full Name", "Department", "Position", "Type", "Salary (EGP)"};
        recentTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable recentTable = new JTable(recentTableModel);
        recentTable.setRowHeight(26);
        recentTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        recentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        recentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentTable.setFillsViewportHeight(true);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        recentTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        JScrollPane tableScroll = new JScrollPane(recentTable);
        tableScroll.getViewport().setBackground(Color.WHITE);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 208, 214)),
                "Recent Employees"
        ));
        tableContainer.add(tableScroll, BorderLayout.CENTER);
        panel.add(tableContainer, BorderLayout.CENTER);

        // === Bottom: Simple Quick Shortcut Buttons ===
        JPanel actionsBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        actionsBox.setBackground(Color.WHITE);
        actionsBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 208, 214)),
                "Quick Shortcuts"
        ));

        actionsBox.add(makeQuickButton("Add Employee", ADD_EMP));
        actionsBox.add(makeQuickButton("View All Employees", VIEW_EMP));
        actionsBox.add(makeQuickButton("Search / Delete", SEARCH_EMP));
        actionsBox.add(makeQuickButton("Update Employee", UPDATE_EMP));
        actionsBox.add(makeQuickButton("Manage Departments", DEPT_MGMT));

        panel.add(actionsBox, BorderLayout.SOUTH);

        refreshHomeStats();
        return panel;
    }

    private JPanel makeStatCard(String title, JLabel valueLabel, JLabel noteLabel, Color accent) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 213, 220)),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                        new EmptyBorder(12, 14, 12, 14)
                )
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLbl.setForeground(Color.DARK_GRAY);

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        valueLabel.setForeground(new Color(44, 62, 80));

        noteLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        noteLabel.setForeground(Color.GRAY);

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(noteLabel);
        return card;
    }

    private JButton makeQuickButton(String label, String targetCard) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> showPanel(targetCard));
        return btn;
    }

    private void refreshHomeStats() {
        ArrayList<Employee> list = employeeManager.getAllEmployees();
        int ft = 0;
        int pt = 0;
        double totalSalary = 0.0;

        for (Employee e : list) {
            if (FullTimeEmployee.TYPE.equals(e.getType())) {
                ft++;
            } else {
                pt++;
            }
            totalSalary += e.calculateSalary();
        }

        totalEmpLabel.setText(String.valueOf(list.size()));
        empBreakdownLabel.setText(ft + " Full-Time, " + pt + " Part-Time");
        deptCountLabel.setText(String.valueOf(departmentManager.getAllDepartments().size()));
        payrollLabel.setText(String.format("%,.2f EGP", totalSalary));

        recentTableModel.setRowCount(0);
        int start = Math.max(0, list.size() - 6);
        for (int i = list.size() - 1; i >= start; i--) {
            Employee e = list.get(i);
            recentTableModel.addRow(new Object[]{
                e.getEmp_ID(),
                e.getFullname(),
                e.getDepartment() != null ? e.getDepartment().getDepartmentName() : "N/A",
                e.getPosition(),
                e.getType(),
                String.format("%.2f", e.calculateSalary())
            });
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm().setVisible(true);
        }
    }

    // --- Accessors for panels ---
    public void showPanel(String name) {
        if (HOME.equals(name)) {
            refreshHomeStats();
        } else if (ADD_EMP.equals(name)) {
            addEmployeePanel.refreshDepartments();
        } else if (VIEW_EMP.equals(name)) {
            viewEmployeePanel.refreshTable();
        } else if (SEARCH_EMP.equals(name)) {
            searchPanel.refreshDepartments();
        } else if (UPDATE_EMP.equals(name)) {
            updateEmployeePanel.refreshTable();
        }
        cardLayout.show(cardPanel, name);
    }

    public EmployeeManager getEmployeeManager()     { return employeeManager; }
    public DepartmentManager getDepartmentManager() { return departmentManager; }
}
