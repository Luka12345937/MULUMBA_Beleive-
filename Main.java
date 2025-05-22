package ucc.payment.system;

import javax.swing.*;
import java.awt.*;

/**
 * Point d'entrée principal de l'application de gestion des frais académiques UCC.
 * Initialise et configure l'interface utilisateur et lance l'application.
 */
public class Main {
    // Configuration de l'application
    private static final String APP_TITLE = "UCC Payment System";
    private static final String APP_VERSION = "1.0.0";
    private static final Dimension WINDOW_SIZE = new Dimension(800, 600);
    private static final String ICON_PATH = "/resources/ucc_logo.png";

    public static void main(String[] args) {
        setupSwingEnvironment();
        launchApplication();
    }

    /**
     * Configure l'environnement Swing pour l'application.
     */
    private static void setupSwingEnvironment() {
        try {
            // Configuration du look and feel système
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Configuration des styles par défaut
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
            UIManager.put("OptionPane.messageFont", defaultFont);
            UIManager.put("Button.font", defaultFont.deriveFont(Font.BOLD));
            UIManager.put("Panel.background", new Color(245, 245, 245));
        } catch (Exception e) {
            showError("Erreur de configuration de l'interface", e);
        }
    }

    /**
     * Lance l'application en créant et affichant la fenêtre de connexion.
     */
    private static void launchApplication() {
        SwingUtilities.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            
            UserManager userManager = new UserManager();
            LoginFrame loginFrame = new LoginFrame(userManager);
            
            configureMainFrame(loginFrame);
            loginFrame.setVisible(true);
        });
    }

    /**
     * Configure la fenêtre principale avec les paramètres par défaut.
     */
    private static void configureMainFrame(JFrame frame) {
        frame.setTitle(String.format("%s v%s", APP_TITLE, APP_VERSION));
        frame.setSize(WINDOW_SIZE);
        frame.setMinimumSize(WINDOW_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            Image icon = new ImageIcon(Main.class.getResource(ICON_PATH)).getImage();
            frame.setIconImage(icon);
        } catch (Exception e) {
            System.err.println("L'icône de l'application n'a pas pu être chargée");
        }
    }

    /**
     * Affiche une boîte de dialogue d'erreur.
     */
    private static void showError(String title, Throwable e) {
        JOptionPane.showMessageDialog(
            null,
            title + " : " + e.getMessage(),
            "Erreur",
            JOptionPane.ERROR_MESSAGE
        );
        e.printStackTrace();
    }
}