import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CourseEnrollmentManagerFileDB {

    private final JTextField nameField = new JTextField();
    private final JTextField courseField = new JTextField();
    private final JTextField emailField = new JTextField();

    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "Name", "Course", "Email", "DateCreated", "DateUpdated"}, 0) {
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;   
        }
    };

    @SuppressWarnings("unused")
    private int nextId = 1;

    private final StudentDbRepository repository = new StudentDbRepository();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CourseEnrollmentManagerFileDB().createAndShowUI(null));
    }

    private void createAndShowUI(Student s) {
        JFrame frame = new JFrame("Course Enrollment Manager (File DB)");
        frame.setSize(950, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Course Enrollment Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(70, 130, 180));
        titleLabel.setForeground(Color.WHITE);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(240, 248, 255));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel formTitle = new JLabel("Add Student Information");
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel formContent = new JPanel(new GridLayout(6, 1, 6, 6));
        formContent.setOpaque(false);

        formContent.add(new JLabel("Student Name:"));
        formContent.add(nameField);
        formContent.add(new JLabel("Course:"));
        formContent.add(courseField);
        formContent.add(new JLabel("Email:"));
        formContent.add(emailField);

        JButton addButton = new JButton("Add Student");
        JButton clearButton = new JButton("Clear");
        JButton saveButton = new JButton("Save to CSV");
        JButton reloadButton = new JButton("Reload from CSV");
        addButton.setBackground(new Color(60, 179, 113));
        addButton.setForeground(Color.WHITE);

        clearButton.setBackground(new Color(220, 20, 60));
        clearButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        leftPanel.add(formTitle, BorderLayout.NORTH);
        leftPanel.add(formContent, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel tableTitle = new JLabel("Student List Overview");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JTable table = new JTable(model);
        table.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(255, 140, 0));
        deleteButton.setForeground(Color.WHITE);

        JButton editButton = new JButton("Edit Selected");
        editButton.setBackground(new Color(30, 144, 255));
        editButton.setForeground(Color.WHITE);

        rightPanel.add(tableTitle, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(saveButton);
        bottomPanel.add(reloadButton);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);    

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300); 
        splitPane.setResizeWeight(0.3);

        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> handleAdd(frame));
        clearButton.addActionListener(e -> clearFields());
        editButton.addActionListener(e -> handleEdit(table, frame));
        deleteButton.addActionListener(e -> handleEdit(table, frame));
        reloadButton.addActionListener(e -> {
            loadFromDatabase();
            JOptionPane.showMessageDialog(
                    frame, "CSV reloaded successfully!"
            );
        });
        saveButton.addActionListener(e -> {
            repository.insert(s);
            JOptionPane.showMessageDialog(
                frame, "Saved to CSV successfully!"
             );
        });
        
        frame.setVisible(true);
    }

    private void handleAdd(JFrame frame) {

    String name = nameField.getText().trim();
    String course = courseField.getText().trim();
    String email = emailField.getText().trim();

    if (!validateInput(frame, name, course, email)) return;

    String now = java.time.LocalDateTime.now().toString();

    Student s = new Student(0, name, course, email);
    s.setDateCreated(now);
    s.setDateUpdated(now);

    repository.insert(s);

    loadFromDatabase(); // 🔥 THIS IS WHAT MAKES TABLE UPDATE

    clearFields();
    }

    private void loadFromDatabase() {

    model.setRowCount(0);

    for (Student s : repository.load()) {

        model.addRow(new Object[]{
                s.getId(),
                s.getName(),
                s.getCourse(),
                s.getEmail(),
                s.getDateCreated(),
                s.getDateUpdated()
        });
    }
}

    private void handleEdit(JTable table, JFrame frame) {
    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(frame,
                "Select a row first!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    Student s = repository.load().get(selectedRow);

    JTextField nameEdit = new JTextField(s.getName());
    JTextField courseEdit = new JTextField(s.getCourse());
    JTextField emailEdit = new JTextField(s.getEmail());

    JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

    panel.add(new JLabel("Name:"));
    panel.add(nameEdit);

    panel.add(new JLabel("Course:"));
    panel.add(courseEdit);

    panel.add(new JLabel("Email:"));
    panel.add(emailEdit);

    int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Edit Student",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {

        String newName = nameEdit.getText().trim();
        String newCourse = courseEdit.getText().trim();
        String newEmail = emailEdit.getText().trim();

        if (!validateInput(frame, newName, newCourse, newEmail)) {
            return;
        }

        s.setName(newName);
        s.setCourse(newCourse);
        s.setEmail(newEmail);

        String now = java.time.LocalDateTime.now().toString();
        s.setDateUpdated(now);

        refreshTableFromList();
        repository.insert(s);

        JOptionPane.showMessageDialog(frame,
                "Student updated successfully!");
        }
    }

    private void refreshTableFromList() {
    model.setRowCount(0);
    for (Student s : repository.load()) {
        model.addRow(new Object[]{
            s.getId(),
            s.getName(),
            s.getCourse(),
            s.getEmail(),
            s.getDateCreated(),
            s.getDateUpdated()
        });
    }
    }

    private boolean validateInput(JFrame frame, String name, String course, String email) {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Name is required.");
            return false;
        }
        if (course.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Course is required.");
            return false;
        }
        if (!email.isEmpty() && (!email.contains("@") || !email.contains("."))) {
            JOptionPane.showMessageDialog(frame, "Invalid email.");
            return false;
        }
        return true;
    }

        private void clearFields() {
        nameField.setText("");
        courseField.setText("");
        emailField.setText("");
    }

} 