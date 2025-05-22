package ucc.payment.system.ui;

import ucc.payment.system.UserManager;
import ucc.payment.system.model.Student;
import ucc.payment.system.util.DialogUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Fenêtre de connexion pour le système de paiement UCC.
 * Gère l'authentification des utilisateurs.
 */
public class LoginFrame extends BaseFrame {
    private final UserManager userManager;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame(UserManager userManager) {
        super("Connexion UCC", new Dimension(400, 350));
        this.userManager = userManager;
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = createDefaultConstraints();

        addTitle(gbc);
        addEmailField(gbc);
        addPasswordField(gbc);
        addLoginButton(gbc);
        addRegisterLink(gbc);
    }

    private GridBagConstraints createDefaultConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        return gbc;
    }

    private void addTitle(GridBagConstraints gbc) {
        JLabel title = new JLabel("Connexion Secure");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0x2C3E50));
        
        gbc.gridy = 0;
        add(title, gbc);
    }

    private void addEmailField(GridBagConstraints gbc) {
        emailField = createTextField();
        emailField.addKeyListener(new EnterKeyListener(passwordField));
        
        gbc.gridy = 1;
        add(createInputPanel("Email", emailField), gbc);
    }

    private void addPasswordField(GridBagConstraints gbc) {
        passwordField = new JPasswordField();
        passwordField.addKeyListener(new EnterKeyListener(this::handleLogin));
        
        gbc.gridy = 2;
        add(createInputPanel("Mot de passe", passwordField), gbc);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setMargin(new Insets(10, 10, 10, 10));
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

    private void addLoginButton(GridBagConstraints gbc) {
        JButton btn = new JButton("Connexion");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0x3498DB));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> handleLogin());
        
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        add(btn, gbc);
    }

    private void addRegisterLink(GridBagConstraints gbc) {
        JLabel link = new JLabel("<html><u>Pas encore inscrit? S'inscrire ici</u></html>");
        link.setForeground(new Color(0x3498DB));
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openRegistration();
            }
        });
        
        gbc.gridy = 4;
        add(link, gbc);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            DialogUtils.showError(this, "Veuillez remplir tous les champs!");
            return;
        }

        Student student = userManager.authenticate(email, password);
        if (student == null) {
            DialogUtils.showError(this, "Identifiants incorrects!");
            return;
        }

        openDashboard(student);
        dispose();
    }

    private void openRegistration() {
        new RegistrationFrame(userManager).setVisible(true);
        dispose();
    }

    private void openDashboard(Student student) {
        new Dashboard(student).setVisible(true);
    }

    private static class EnterKeyListener extends KeyAdapter {
        private final Runnable action;

        EnterKeyListener(JComponent nextField) {
            this(() -> nextField.requestFocusInWindow());
        }

        EnterKeyListener(Runnable action) {
            this.action = action;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                action.run();
            }
        }
    }
}