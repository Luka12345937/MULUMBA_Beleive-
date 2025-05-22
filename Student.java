package ucc.payment.system.model;

import java.util.regex.Pattern;

/**
 * Représente un étudiant de l'UCC avec validation des données.
 */
public class Student {
    // Modèles de validation
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@ucc\\.edu$");
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$");
    private static final Pattern NAME_REGEX = Pattern.compile("^[a-zA-Z\\s]{3,50}$");

    // Données de l'étudiant
    private final String name;
    private final String email;
    private final String passwordHash;
    private final String promotion;
    private final String faculty;

    public Student(String name, String email, String password, String promotion, String faculty) {
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.passwordHash = hashPassword(validatePassword(password));
        this.promotion = promotion;
        this.faculty = faculty;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getPromotion() { return promotion; }
    public String getFaculty() { return faculty; }

    // Méthodes de validation
    private String validateName(String name) {
        if (!NAME_REGEX.matcher(name.trim()).matches()) {
            throw new IllegalArgumentException("Nom invalide (3-50 lettres seulement)");
        }
        return name.trim();
    }

    private String validateEmail(String email) {
        if (!EMAIL_REGEX.matcher(email.trim().toLowerCase()).matches()) {
            throw new IllegalArgumentException("Email UCC invalide (ex: prenom.nom@ucc.edu)");
        }
        return email.trim().toLowerCase();
    }

    private String validatePassword(String password) {
        if (!PASSWORD_REGEX.matcher(password).matches()) {
            throw new IllegalArgumentException(
                "Mot de passe doit contenir:\n" +
                "- 8 caractères minimum\n" +
                "- 1 majuscule, 1 minuscule, 1 chiffre"
            );
        }
        return password;
    }

    // Hashage du mot de passe (simplifié)
    private String hashPassword(String password) {
        // En production, utiliser BCrypt ou Argon2
        return Integer.toString(password.hashCode()); // Exemple simplifié
    }

    @Override
    public String toString() {
        return String.format(
            "Étudiant[%s, %s, %s, %s]", 
            name, email, promotion, faculty
        );
    }
}