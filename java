-- Création de la base de données
CREATE DATABASE ucc_payment_system
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'fr_FR.UTF-8'
    LC_CTYPE = 'fr_FR.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- Connexion à la base de données
\c ucc_payment_system;

-- Table des facultés
CREATE TABLE faculties (
    faculty_id SERIAL PRIMARY KEY,
    faculty_code VARCHAR(10) NOT NULL UNIQUE,
    faculty_name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Table des promotions
CREATE TABLE promotions (
    promotion_id SERIAL PRIMARY KEY,
    promotion_name VARCHAR(10) NOT NULL UNIQUE,
    CHECK (promotion_name IN ('L1', 'L2', 'L3'))
);

-- Table des étudiants
CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(128) NOT NULL,
    promotion_id INT REFERENCES promotions(promotion_id) ON DELETE SET NULL,
    faculty_id INT REFERENCES faculties(faculty_id) ON DELETE SET NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Table des méthodes de paiement
CREATE TABLE payment_methods (
    method_id SERIAL PRIMARY KEY,
    method_name VARCHAR(50) NOT NULL UNIQUE,
    method_type VARCHAR(20) NOT NULL CHECK (method_type IN ('mobile_money', 'physical'))
);

-- Table des opérateurs mobiles
CREATE TABLE mobile_operators (
    operator_id SERIAL PRIMARY KEY,
    operator_name VARCHAR(50) NOT NULL UNIQUE,
    method_id INT REFERENCES payment_methods(method_id) ON DELETE CASCADE
);

-- Table des paiements
CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    student_id INT REFERENCES students(student_id) ON DELETE CASCADE,
    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    method_id INT REFERENCES payment_methods(method_id) ON DELETE SET NULL,
    operator_id INT REFERENCES mobile_operators(operator_id) ON DELETE SET NULL,
    transaction_id VARCHAR(50) UNIQUE,
    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'completed', 'failed')) DEFAULT 'pending'
);

-- Table des vérifications de paiement
CREATE TABLE payment_verifications (
    verification_id SERIAL PRIMARY KEY,
    payment_id INT REFERENCES payments(payment_id) ON DELETE CASCADE,
    verification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified_by VARCHAR(100),
    verification_status VARCHAR(20) NOT NULL CHECK (verification_status IN ('verified', 'unverified', 'disputed')) DEFAULT 'unverified'
);

-- Table des rôles
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL UNIQUE CHECK (role_name IN ('student', 'admin'))
);

-- Table d'association étudiants-rôles
CREATE TABLE student_roles (
    student_id INT REFERENCES students(student_id) ON DELETE CASCADE,
    role_id INT REFERENCES roles(role_id) ON DELETE CASCADE,
    PRIMARY KEY (student_id, role_id)
);

-- Table des journaux d'activité
CREATE TABLE activity_logs (
    log_id SERIAL PRIMARY KEY,
    student_id INT REFERENCES students(student_id) ON DELETE SET NULL,
    action VARCHAR(100) NOT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details JSONB
);

-- Insertion des données de référence

-- Facultés
INSERT INTO faculties (faculty_code, faculty_name, description) VALUES
('FTH', 'Faculté de Théologie', 'Formation en théologie'),
('FDC', 'Faculté de Droit Canonique', 'Études de droit canonique'),
('FPH', 'Faculté de Philosophie', 'Philosophie et sciences humaines'),
('FED', 'Faculté d''Économie et Développement', 'Économie et développement'),
('FCS', 'Faculté de Communication Sociale', 'Communication sociale'),
('FDR', 'Faculté de Droit', 'Études juridiques'),
('FSPO', 'Faculté des Sciences Politiques', 'Sciences politiques'),
('FSI', 'Faculté des Sciences Informatiques', 'Informatique'),
('FM', 'Faculté de Médecine', 'Études médicales');

-- Promotions
INSERT INTO promotions (promotion_name) VALUES
('L1'), ('L2'), ('L3');

-- Méthodes de paiement
INSERT INTO payment_methods (method_name, method_type) VALUES
('Orange Money', 'mobile_money'),
('Airtel Money', 'mobile_money'),
('M-Pesa', 'mobile_money'),
('Africell Money', 'mobile_money'),  -- Correction du nom pour cohérence
('Paiement Physique', 'physical');

-- Opérateurs mobiles
INSERT INTO mobile_operators (operator_name, method_id) VALUES
('Orange', 1),
('Airtel', 2),
('Vodacom', 3),
('Africell', 4);

-- Rôles
INSERT INTO roles (role_name) VALUES
('student'),
('admin');
