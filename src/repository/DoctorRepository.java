package repository;

import models.Doctor;
import models.Specialty;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepository {
    public Doctor getDoctorById(int id){
        String sql = "SELECT * FROM doctors WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone");
                int specialtyId = rs.getInt("specialty_id");

                SpecialtyRepository specialtyRepository= new SpecialtyRepository();
                Specialty specialty= null;

                if (specialtyId!=0){
                    specialty = specialtyRepository.getSpecialtyById(specialtyId);

                }
                return new Doctor(id, firstName, lastName, email, phoneNumber, specialty);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
     return null;
    }

    public void insertDoctor(Doctor doctor){
        String sql= "INSERT INTO doctors (first_name, last_name, email, phone, specialty_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, doctor.getFirstName());
            stmt.setString(2, doctor.getLastName());
            stmt.setString(3, doctor.getEmail());
            stmt.setString(4, doctor.getPhoneNumber());

            if (doctor.getSpecialty() != null){
                stmt.setInt(5, doctor.getSpecialty().getId());
            }else{
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.executeUpdate();
            System.out.println("Доктора е добавен успешно!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Doctor findByIdNameAndPassword(int doctorId, String firstName, String password) {
        String sql = "SELECT * FROM doctors WHERE id = ? AND first_name = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            stmt.setString(2, firstName);
            stmt.setString(3, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return getDoctorById(doctorId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                doctors.add(getDoctorById(rs.getInt("id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    public void deleteDoctorById(int id) {
        String sql = "DELETE FROM doctors WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctors SET first_name = ?, last_name = ?, email = ?, phone = ?, specialty_id = ?, password = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctor.getFirstName());
            stmt.setString(2, doctor.getLastName());
            stmt.setString(3, doctor.getEmail());
            stmt.setString(4, doctor.getPhoneNumber());

            if (doctor.getSpecialty() != null) {
                stmt.setInt(5, doctor.getSpecialty().getId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            stmt.setString(6, doctor.getPassword());
            stmt.setInt(7, doctor.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
