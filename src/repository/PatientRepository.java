package repository;

import models.Patient;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {
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
            System.out.println("Пациента е добавен успешно.");

        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return patients;
    }
}