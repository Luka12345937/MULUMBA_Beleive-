package ucc.payment.system.ui;

import ucc.payment.system.model.Student;
import ucc.payment.system.util.DialogUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

/**
 * Tableau de bord principal de l'application UCC Payment System.
 */
public class Dashboard extends BaseFrame {
    private final Student student;
    private static final Color PRIMARY_COLOR = new Color(0x2C3E50);
    private static final Color SECONDARY_COLOR = new Color(0x3498DB);
    private static final String[] SOCIAL_NETWORKS = {"facebook", "twitter", "instagram", "linkedin", "youtube", "whatsapp"};
    private static final String[] PAYMENT_METHODS = {"Orange Money", "Airtel Money", "M-Pesa", "Africell"};

    public Dashboard(Student student) {
        super("Tableau de bord - " + student.getName(), new Dimension(1000, 700));
        this.student = student;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(0, 80));

        // Logo
        JLabel logo = new JLabel(loadIcon("ucc_logo.png", 60));
        logo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        header.add(logo, BorderLayout.WEST);

        // User info
        header.add(createUserInfoPanel(), BorderLayout.CENTER);

        // Social media
        header.add(createSocialPanel(), BorderLayout.EAST);

        return header;
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PRIMARY_COLOR);
        
        panel.add(createLabel("Bienvenue, " + student.getName(), 18, Color.WHITE));
        panel.add(createLabel(
            "Promotion : " + student.getPromotion() + " | Filière : " + student.getFaculty(),
            14, new Color(0xBDC3C7)
        ));
        
        return panel;
    }

    private JPanel createSocialPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(PRIMARY_COLOR);
        
        for (String network : SOCIAL_NETWORKS) {
            panel.add(createSocialButton(network));
        }
        
        return panel;
    }

    private JComponent createMainContent() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(createPaymentSection());
        splitPane.setBottomComponent(createVerificationSection());
        splitPane.setDividerLocation(400);
        splitPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        return splitPane;
    }

    private JPanel createPaymentSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Paiement des Frais"));
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.addTab("Mobile Money", createMobileMoneyPanel());
        tabs.addTab("Paiement Physique", createPhysicalPaymentPanel());
        
        panel.add(tabs);
        return panel;
    }

    private JPanel createMobileMoneyPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 3, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        for (String method : PAYMENT_METHODS) {
            panel.add(createPaymentButton(method));
        }
        
        return panel;
    }

    private JPanel createPhysicalPaymentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel info = new JLabel("<html><center>"
            + "Veuillez vous rendre à la caisse de l'université<br>"
            + "avec votre matricule et une pièce d'identité."
            + "</center></html>");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalGlue());
        panel.add(info);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

    private JPanel createVerificationSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Vérification des Paiements"));
        
        JButton verifyBtn = new JButton("Vérifier les Paiements");
        verifyBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        verifyBtn.setBackground(SECONDARY_COLOR);
        verifyBtn.setForeground(Color.WHITE);
        verifyBtn.setPreferredSize(new Dimension(250, 50));
        verifyBtn.addActionListener(e -> DialogUtils.showInfo(this, "Fonctionnalité en développement"));
        
        panel.add(verifyBtn, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(PRIMARY_COLOR);
        footer.setPreferredSize(new Dimension(0, 40));
        
        JLabel copyright = createLabel("© 2023 UCC - Tous droits réservés", 12, Color.WHITE);
        footer.add(copyright);
        
        return footer;
    }

    private Border createTitledBorder(String title) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

    private JLabel createLabel(String text, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, size));
        label.setForeground(color);
        return label;
    }

    private JButton createPaymentButton(String operator) {
        JButton btn = new JButton("<html><center>" + operator + "<br><small>Payer via " + operator + "</small></center></html>");
        btn.setPreferredSize(new Dimension(200, 100));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0xBDC3C7));
        btn.setForeground(PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> handlePayment(operator));
        return btn;
    }

    private JButton createSocialButton(String network) {
        JButton btn = new JButton(loadIcon(network + ".png", 24));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> openSocialLink(network));
        return btn;
    }

    private ImageIcon loadIcon(String filename, int size) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/" + filename));
        Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void handlePayment(String operator) {
        DialogUtils.showInfo(this, "Redirection vers " + operator + "...");
    }

    private void openSocialLink(String network) {
        try {
            Desktop.getDesktop().browse(new URI("https://www." + network + ".com/UCC_Officiel"));
        } catch (Exception e) {
            DialogUtils.showError(this, "Impossible d'ouvrir le lien");
        }
    }
}