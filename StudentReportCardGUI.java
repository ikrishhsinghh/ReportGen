import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class Admin {
    static final String USERNAME = "admin";
    static final String PASSWORD = "admin123";
}

class Student implements Serializable {
    String college = "Sainath High School and Jr College";
    int rollNo, studentClass;
    String name;

    int subjectCount;
    String[] subjects;
    int[] internalMarks;
    int[] externalMarks;
    int[] totalMarks;
    boolean hasInternal;

    Student(int subjectCount, boolean hasInternal) {
        this.subjectCount = subjectCount;
        this.hasInternal = hasInternal;
        subjects = new String[subjectCount];
        internalMarks = new int[subjectCount];
        externalMarks = new int[subjectCount];
        totalMarks = new int[subjectCount];
    }

    void calculateTotals() {
        for (int i = 0; i < subjectCount; i++) {
            totalMarks[i] = internalMarks[i] + externalMarks[i];
        }
    }

    int totalObtained() {
        int sum = 0;
        for (int t : totalMarks) sum += t;
        return sum;
    }
}

public class StudentReportCardGUI extends JFrame {

    static final String FILE_NAME = "students.dat";
    static ArrayList<Student> students = new ArrayList<>();

    private boolean hasInternal;
    private boolean darkMode = false;

    private JPanel sidebar;
    private JPanel contentPanel;
    private JLabel headerTitle;

    // Professional Color Palette
    private Color primary = new Color(37, 99, 235);
    private Color sidebarColor = new Color(30, 41, 59);
    private Color lightBg = new Color(245, 247, 250);
    private Color darkBg = new Color(20, 20, 20);
    private Color cardLight = Color.WHITE;
    private Color cardDark = new Color(35, 35, 35);

    public StudentReportCardGUI(boolean hasInternal) {
        this.hasInternal = hasInternal;
        loadStudents();

        setTitle("Student Report Card Management System");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        add(createHeader(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);
        add(createDashboard(), BorderLayout.CENTER);
    }

    // ===== HEADER =====
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(primary);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        headerTitle = new JLabel("Student Report Card Management System");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerTitle.setForeground(Color.WHITE);

        JButton themeBtn = new JButton("Dark Mode");
        themeBtn.setFocusPainted(false);
        themeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
themeBtn.setBackground(Color.WHITE);
themeBtn.setBorder(new EmptyBorder(8, 15, 8, 15));
        themeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeBtn.addActionListener(e -> toggleTheme(themeBtn));

        header.add(headerTitle, BorderLayout.WEST);
        header.add(themeBtn, BorderLayout.EAST);

        return header;
    }

    // ===== SIDEBAR =====
   private JPanel createSidebar() {
    sidebar = new JPanel();
    sidebar.setLayout(new GridLayout(6, 1, 0, 10)); // changed from 7 to 6
    sidebar.setBackground(sidebarColor);
    sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
    sidebar.setPreferredSize(new Dimension(240, getHeight()));

    sidebar.add(createNavButton("Dashboard", e -> setContent(createDashboard())));
    sidebar.add(createNavButton("Add Student", e -> addStudent()));
    sidebar.add(createNavButton("Generate Report", e -> generateReport()));
    sidebar.add(createNavButton("Modify Report", e -> modifyReport()));
    sidebar.add(createNavButton("Class-wise List", e -> classWiseList()));
    sidebar.add(createNavButton("Save & Exit", e -> {
        saveStudents();
        System.exit(0);
    }));

    return sidebar;
}


    private JButton createNavButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        btn.setBackground(sidebarColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 15, 12, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(primary);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(sidebarColor);
            }
        });

        btn.addActionListener(action);
        return btn;
    }

    // ===== DASHBOARD (REAL CONTENT UI) =====
    // ===== CLEAN HOME SCREEN (NO CARDS) =====
private JPanel createDashboard() {
    contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBackground(darkMode ? darkBg : lightBg);
    contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

    JPanel center = new JPanel();
    center.setOpaque(false);
    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

    // Dynamic text color based on theme
    Color titleColor = darkMode ? Color.WHITE : Color.BLACK;
    Color subtitleColor = darkMode ? new Color(200, 200, 200) : Color.GRAY;

    JLabel title = new JLabel("Welcome to Student Report Card System");
    title.setFont(new Font("Segoe UI", Font.BOLD, 32));
    title.setForeground(titleColor);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel subtitle = new JLabel("Use the sidebar to manage students, reports, and records");
    subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
    subtitle.setForeground(subtitleColor);
    subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

    center.add(Box.createVerticalGlue());
    center.add(title);
    center.add(Box.createRigidArea(new Dimension(0, 20)));
    center.add(subtitle);
    center.add(Box.createVerticalGlue());

    contentPanel.add(center, BorderLayout.CENTER);
    return contentPanel;
}


    private JPanel createCard(String title, String desc) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(darkMode ? cardDark : cardLight);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel d = new JLabel("<html>" + desc + "</html>");
        d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        d.setForeground(Color.GRAY);

        card.add(t, BorderLayout.NORTH);
        card.add(d, BorderLayout.CENTER);
        return card;
    }

    private void setContent(JPanel panel) {
        getContentPane().remove(2);
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void toggleTheme(JButton btn) {
    darkMode = !darkMode;

    // Remove icons & use clean professional text
    btn.setText(darkMode ? "Light Mode" : "Dark Mode");

    // Update dashboard with correct text colors
    setContent(createDashboard());

    // Update sidebar text colors dynamically
    for (Component c : sidebar.getComponents()) {
        if (c instanceof JButton) {
            c.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        }
    }
}


    // ===== ORIGINAL LOGIC (UNCHANGED) =====
    void addStudent() {
        try {
            int roll = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Roll No:"));
            for (Student s : students) {
                if (s.rollNo == roll) {
                    JOptionPane.showMessageDialog(this, "Roll No already exists.");
                    return;
                }
            }

            String name = JOptionPane.showInputDialog(this, "Enter Name:");
            int cls = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Class (1-12):"));
            int subCount = Integer.parseInt(JOptionPane.showInputDialog(this, "Number of Subjects:"));

            Student s = new Student(subCount, hasInternal);
            s.rollNo = roll;
            s.name = name;
            s.studentClass = cls;

            for (int i = 0; i < subCount; i++)
                s.subjects[i] = JOptionPane.showInputDialog(this, "Subject " + (i + 1) + ":");

            if (hasInternal) {
                for (int i = 0; i < subCount; i++)
                    s.internalMarks[i] = Integer.parseInt(
                            JOptionPane.showInputDialog(this, "Internal marks (" + s.subjects[i] + "):"));
            }

            for (int i = 0; i < subCount; i++)
                s.externalMarks[i] = Integer.parseInt(
                        JOptionPane.showInputDialog(this, "Marks (" + s.subjects[i] + "):"));

            students.add(s);
            JOptionPane.showMessageDialog(this, "Student added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    void generateReport() {
        try {
            int roll = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Roll No:"));
            for (Student s : students) {
                if (s.rollNo == roll) {
                    s.calculateTotals();
                    showReportTable(s);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Student not found.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    void showReportTable(Student s) {
        String[] cols = s.hasInternal ?
                new String[]{"Subject", "Internal", "External", "Total"} :
                new String[]{"Subject", "Marks"};

        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (int i = 0; i < s.subjectCount; i++) {
            if (s.hasInternal)
                model.addRow(new Object[]{s.subjects[i], s.internalMarks[i], s.externalMarks[i], s.totalMarks[i]});
            else
                model.addRow(new Object[]{s.subjects[i], s.externalMarks[i]});
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        JOptionPane.showMessageDialog(this, new JScrollPane(table),
                "Report Card - " + s.name + " | Total: " + s.totalObtained(),
                JOptionPane.INFORMATION_MESSAGE);
    }


    void classWiseList() {
        try {
            int cls = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter class (1-12):"));
            String[] cols = {"Roll No", "Name", "Total Marks"};
            DefaultTableModel model = new DefaultTableModel(cols, 0);

            for (Student s : students) {
                if (s.studentClass == cls) {
                    s.calculateTotals();
                    model.addRow(new Object[]{s.rollNo, s.name, s.totalObtained()});
                }
            }

            JTable table = new JTable(model);
            table.setRowHeight(28);
            JOptionPane.showMessageDialog(this, new JScrollPane(table), "Class-wise Student List",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

   void modifyReport() {

    String rollStr = JOptionPane.showInputDialog(this, "Enter Roll No to Modify:");
    if (rollStr == null) return;

    try {
        int roll = Integer.parseInt(rollStr);

        // Find student
        Student found = null;
        for (Student s : students) {
            if (s.rollNo == roll) {
                found = s;
                break;
            }
        }

        if (found == null) {
            JOptionPane.showMessageDialog(this, "Student not found.");
            return;
        }

        // 🔥 IMPORTANT FIX: make it final reference for lambda
        final Student target = found;

        target.calculateTotals();

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(darkMode ? new Color(30, 30, 30) : Color.WHITE);

        // ===== TOP DETAILS =====
        JPanel detailsPanel = new JPanel(new GridLayout(2, 2, 15, 10));
        detailsPanel.setOpaque(false);

        JTextField nameField = new JTextField(target.name);
        JTextField classField = new JTextField(String.valueOf(target.studentClass));

        detailsPanel.add(new JLabel("Student Name:"));
        detailsPanel.add(nameField);
        detailsPanel.add(new JLabel("Class:"));
        detailsPanel.add(classField);

        // ===== TABLE =====
        String[] columns;
        if (target.hasInternal) {
            columns = new String[]{"Subject", "Internal Marks", "External Marks", "Total"};
        } else {
            columns = new String[]{"Subject", "Marks"};
        }

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                if (target.hasInternal) {
                    return col != 3; // Total not editable
                } else {
                    return true;
                }
            }
        };

        for (int i = 0; i < target.subjectCount; i++) {
            if (target.hasInternal) {
                model.addRow(new Object[]{
                        target.subjects[i],
                        target.internalMarks[i],
                        target.externalMarks[i],
                        target.totalMarks[i]
                });
            } else {
                model.addRow(new Object[]{
                        target.subjects[i],
                        target.externalMarks[i]
                });
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new TitledBorder("Edit Subjects & Marks"));

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setBackground(new Color(37, 99, 235));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(this, "Modify Student - Roll No: " + roll, true);
        dialog.setSize(750, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setContentPane(mainPanel);

        // ===== SAVE ACTION (NOW ERROR-FREE) =====
        saveBtn.addActionListener(e -> {
            try {
                target.name = nameField.getText().trim();
                target.studentClass = Integer.parseInt(classField.getText().trim());

                for (int i = 0; i < target.subjectCount; i++) {
                    target.subjects[i] = model.getValueAt(i, 0).toString();

                    if (target.hasInternal) {
                        target.internalMarks[i] = Integer.parseInt(model.getValueAt(i, 1).toString());
                        target.externalMarks[i] = Integer.parseInt(model.getValueAt(i, 2).toString());
                        target.totalMarks[i] = target.internalMarks[i] + target.externalMarks[i];
                        model.setValueAt(target.totalMarks[i], i, 3);
                    } else {
                        target.externalMarks[i] = Integer.parseInt(model.getValueAt(i, 1).toString());
                        target.totalMarks[i] = target.externalMarks[i];
                    }
                }

                JOptionPane.showMessageDialog(dialog, "Report updated successfully!");
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid data entered!");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Invalid Roll No!");
    }
}



    static void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (Exception ignored) {}
    }

    static void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (Exception e) {
            students = new ArrayList<>();
        }
    }

    static boolean adminLogin() {
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        Object[] msg = {"Username:", user, "Password:", pass};

        int opt = JOptionPane.showConfirmDialog(null, msg, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        return opt == JOptionPane.OK_OPTION &&
                user.getText().equals(Admin.USERNAME) &&
                new String(pass.getPassword()).equals(Admin.PASSWORD);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        if (!adminLogin()) {
            JOptionPane.showMessageDialog(null, "Invalid Login!");
            System.exit(0);
        }

        int pattern = JOptionPane.showConfirmDialog(null,
                "Was Internal & External pattern followed in Examination?",
                "Exam Pattern", JOptionPane.YES_NO_OPTION);

        boolean hasInternal = (pattern == JOptionPane.YES_OPTION);
        SwingUtilities.invokeLater(() -> new StudentReportCardGUI(hasInternal).setVisible(true));
    }
}
