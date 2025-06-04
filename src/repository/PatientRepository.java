package repository;

import models.Patient;
import utils.DBConnection;
import utils.LoggerConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientRepository {

    private static final Logger logger = Logger.getLogger(PatientRepository.class.getName());
    static {
        LoggerConfig.configureLogger(logger);
    }

    public void addPatient(Patient patient) {
        String sql = "INSERT INTO patients(first_name, last_name, age, phone_number, email) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setInt(3, patient.getAge());
            stmt.setString(4, patient.getPhoneNumber());
            stmt.setString(5, patient.getEmail());

            stmt.executeUpdate();
            logger.info("Пациентът е добавен успешно: " + patient.getFirstName() + " " + patient.getLastName());


        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при добавяне на пациент", e);
        }
    }
    public List<Patient> getAllPatients(){
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getInt("age")
                ));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при зареждане на всички пациенти", e);
        }

        return patients;
    }
    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM patients WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Patient(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getInt("age")
                );
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при извличане на пациент по ID: " + id, e);
        }

        return null;
    }

    public Patient findByIdAndFirstName(int id, String firstName) {
        String sql = "SELECT * FROM patients WHERE id = ? AND first_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, firstName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Patient(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getInt("age")
                );
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при намиране на пациент по ID и първо име", e);
        }

        return null;
    }
    public void insertPatient(Patient patient) {
        String sql = "INSERT INTO patients (first_name, last_name, age, email, phone_number) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setInt(3, patient.getAge());
            stmt.setString(4, patient.getEmail());
            stmt.setString(5, patient.getPhoneNumber());

            stmt.executeUpdate();
            logger.info("Пациентът беше успешно добавен: " + patient.getFirstName());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при записване на пациент в базата", e);
        }
    }
}