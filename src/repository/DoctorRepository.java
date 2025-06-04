package repository;

import models.Doctor;
import models.Specialty;
import utils.PasswordUtil;
import utils.DBConnection;
import utils.LoggerConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoctorRepository {

    private static final Logger logger = Logger.getLogger(DoctorRepository.class.getName());
    static {
        LoggerConfig.configureLogger(logger);
    }
    private Scanner scanner = new Scanner(String.valueOf(System.out));
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
                String password = rs.getString("password");
                return new Doctor(id, firstName, lastName, email, phoneNumber, specialty, password);
            }
        }
        catch(SQLException e){
            logger.log(Level.SEVERE, "Грешка при търсене на лекар по ID: " + id, e);
        }
     return null;
    }

    public void insertDoctor(Doctor doctor){
        String sql = "INSERT INTO doctors (first_name, last_name, email, phone, specialty_id, password) VALUES (?, ?, ?, ?, ?, ?)";

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
            String hashedPassword = PasswordUtil.hashPassword(doctor.getPassword());
            stmt.setString(6, hashedPassword);
            stmt.executeUpdate();

            logger.info("Лекарят беше успешно добавен: " + doctor.getFirstName() + " " + doctor.getLastName());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при добавяне на лекар: " + doctor.getFirstName(), e);
        }
    }

    public Doctor findByIdNameAndPassword(int doctorId, String firstName, String password) {
        String sql = "SELECT * FROM doctors WHERE id = ? AND first_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            stmt.setString(2, firstName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                if (PasswordUtil.checkPassword(password, storedHashedPassword)) {
                    return getDoctorById(doctorId);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при проверка на идентификационни данни на лекар", e);
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
            logger.log(Level.SEVERE, "Грешка при зареждане на всички лекари", e);
        }

        return doctors;
    }

    public void deleteDoctorById(int id) {
        String sql = "DELETE FROM doctors WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info("Лекарят с ID " + id + " беше изтрит.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при изтриване на лекар с ID: " + id, e);
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

            System.out.print("Нова парола (оставете празно ако не желаете промяна): ");
            String passwordInput = scanner.nextLine();

            if (!passwordInput.isBlank()) {
                doctor.setPassword(PasswordUtil.hashPassword(passwordInput));
            } else {
                doctor.setPassword(getDoctorById(doctor.getId()).getPassword());
            }
            stmt.setInt(7, doctor.getId());

            stmt.executeUpdate();
            logger.info("Лекарят с ID " + doctor.getId() + " беше успешно обновен.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Грешка при обновяване на лекар с ID: " + doctor.getId(), e);
        }
    }
}
