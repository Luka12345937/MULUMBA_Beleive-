package ucc.payment.system.ui;

import ucc.payment.system.UserManager;
import ucc.payment.system.model.Student;
import ucc.payment.system.util.DialogUtils;
import ucc.payment.system.util.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Predicate;

/**
 * Fenêtre d'inscription pour le système de paiement UCC.
 */
public class RegistrationFrame extends BaseFrame {
    private final UserManager userManager;
    
    // Composants UI
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> promotionCombo, facultyCombo;
    
    // Données statiques
    private static final String[] PROMOTIONS = {"L1", "L2", "L3"};
    private static final String[] FACULTIES = {
        "FTH", "FDC", "FPH", "FED", "FCS", 
        "FDR", "FSPO", "FSI", "Médecine"
    };

    public RegistrationFrame(UserManager userManager) {
        super("Inscription UCC", new Dimension(500, 500));
        this.userManager = userManager;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = setupConstraints();
        
        addTitle(gbc);
        addNameField(gbc);
        addEmailField(gbc);
        addPasswordField(gbc);
        addPromotionField(gbc);
        addFacultyField(gbc);
        addRegisterButton(gbc);
        addLoginLink(gbc);
    }

    private GridBagConstraints setupConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void addTitle(GridBagConstraints gbc) {
        JLabel title = new JLabel("Créer un compte");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0x2C3E50));
        
        gbc.gridy = 0;
        add(title, gbc);
    }

    private void addNameField(GridBagConstraints gbc) {
        nameField = createTextField(ValidationUtils::isValidName);
        gbc.gridy = 1;
        add(createInputPanel("Nom complet", nameField), gbc);
    }

    private void addEmailField(GridBagConstraints gbc) {
        emailField = createTextField(ValidationUtils::isValidEmail);
        gbc.gridy = 2;
        add(createInputPanel("Email UCC", emailField), gbc);
    }

    private void addPasswordField(GridBagConstraints gbc) {
        passwordField = new JPasswordField();
        passwordField.addFocusListener(new ValidationListener(ValidationUtils::isStrongPassword));
        
        gbc.gridy = 3;
        add(createInputPanel("Mot de passe", passwordField), gbc);
    }

    private void addPromotionField(GridBagConstraints gbc) {
        promotionCombo = new JComboBox<>(PROMOTIONS);
        styleComboBox(promotionCombo);
        
        gbc.gridy = 4;
        add(createInputPanel("Promotion", promotionCombo), gbc);
    }

    private void addFacultyField(GridBagConstraints gbc) {
        facultyCombo = new JComboBox<>(FACULTIES);
        styleComboBox(facultyCombo);
        
        gbc.gridy = 5;
        add(createInputPanel("Filière", facultyCombo), gbc);
    }

    private JTextField createTextField(Predicate<String> validator) {
        JTextField field = new JTextField(20);
        field.addFocusListener(new ValidationListener(validator));
        return field;
    }

    private JPanel createInputPanel(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? new Color(0x3498DB) : Color.WHITE);
                setForeground(isSelected ? Color.WHITE : new Color(0x2C3E50));
                return this;
            }
        });
    }

    private void addRegisterButton(GridBagConstraints gbc) {
        JButton btn = new JButton("S'inscrire");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0x27AE60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> registerUser());
        
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btn, gbc);
    }

    private void addLoginLink(GridBagConstraints gbc) {
        JLabel link = new JLabel("<html><u>Déjà inscrit? Se connecter ici</u></html>");
        link.setForeground(new Color(0x3498DB));
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openLogin();
            }
        });
        
        gbc.gridy = 7;
        add(link, gbc);
    }

    private void registerUser() {
        if (!validateInput()) {
            DialogUtils.showError(this, "Veuillez corriger les erreurs!");
            return;
        }

        Student student = new Student(
            nameField.getText().trim(),
            emailField.getText().trim(),
            new String(passwordField.getPassword()),
            (String) promotionCombo.getSelectedItem(),
            (String) facultyCombo.getSelectedItem()
        );

        userManager.addUser(student);
        DialogUtils.showInfo(this, "Inscription réussie!");
        openLogin();
        dispose();
    }

    private boolean validateInput() {
        return ValidationUtils.isValidName(nameField.getText())
            && ValidationUtils.isValidEmail(emailField.getText())
            && ValidationUtils.isStrongPassword(new String(passwordField.getPassword()));
    }

    private void openLogin() {
        new LoginFrame(userManager).setVisible(true);
    }

    private static class ValidationListener extends FocusAdapter {
        private final Predicate<String> validator;

        ValidationListener(Predicate<String> validator) {
            this.validator = validator;
        }

        @Override
        public void focusLost(FocusEvent e) {
            JComponent comp = (JComponent) e.getSource();
            String value = comp instanceof JTextField 
                ? ((JTextField) comp).getText() 
                : new String(((JPasswordField) comp).getPassword());
                
            Color borderColor = validator.test(value) ? new Color(0x27AE60) : Color.RED;
            comp.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        }
    }
}