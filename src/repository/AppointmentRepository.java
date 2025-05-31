package repository;

import models.*;
import models.enums.StatusEnum;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentRepository {

    private static final Logger logger = Logger.getLogger(AppointmentRepository.class.getName());
    private final PatientRepository patientRepository = new PatientRepository();
    private final DoctorRepository doctorRepository = new DoctorRepository();
    private final ExaminationTypeRepository examinationTypeRepository = new ExaminationTypeRepository();
    private final StatusRepository statusRepository = new StatusRepository();

    public void insertAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, examination_id, date_time, status_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointment.getPatient().getId());
            stmt.setInt(2, appointment.getDoctor().getId());
            stmt.setInt(3, appointment.getExaminationType().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(appointment.getDateTime()));
            stmt.setInt(5, appointment.getStatus().getId());

            stmt.executeUpdate();
            System.out.println("Часът беше записан успешно!");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при запис на час в базата данни", e);
        }
    }

    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        String sql = "SELECT * FROM appointments WHERE patient_id = ?";
        List<Appointment> appointments = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при извличане на часове по patient_id", e);
        }

        return appointments;
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        String sql = "SELECT * FROM appointments WHERE doctor_id = ?";
        List<Appointment> appointments = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при извличане на часове по doctor_id", e);
        }

        return appointments;
    }

    public void updateAppointmentDateTime(int appointmentId, LocalDateTime newDateTime) {
        String sql = "UPDATE appointments SET date_time = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(newDateTime));
            stmt.setInt(2, appointmentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Дата и час успешно обновени.");
            } else {
                System.out.println("Няма открит час с това ID.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при обновяване на дата/час за час", e);
        }
    }

    public void updateAppointmentStatus(int appointmentId, StatusEnum statusEnum) {
        Status status = new StatusRepository().getStatusByEnum(statusEnum);
        if (status == null) {
            System.out.println("Невалиден статус.");
            return;
        }

        String sql = "UPDATE appointments SET status_id = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, status.getId());
            stmt.setInt(2, appointmentId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Статусът на часа беше променен.");
            } else {
                System.out.println("Часът не беше намерен.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при обновяване на статус на час", e);
        }
    }

    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int patientId = rs.getInt("patient_id");
        int doctorId = rs.getInt("doctor_id");
        int examinationId = rs.getInt("examination_id");
        int statusId = rs.getInt("status_id");
        LocalDateTime dateTime = rs.getTimestamp("date_time").toLocalDateTime();

        Patient patient = patientRepository.getPatientById(patientId);
        Doctor doctor = doctorRepository.getDoctorById(doctorId);
        ExaminationType examination = examinationTypeRepository.getExaminationTypeById(examinationId);
        Status status = statusRepository.getStatusById(statusId);

        return new Appointment(id, patient, doctor, examination, dateTime, status);
    }

    public List<Appointment> getAppointmentsByDoctorIdAndStatus(int doctorId, StatusEnum statusEnum) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? AND status_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int statusId = new StatusRepository().getStatusByEnum(statusEnum).getId();
            stmt.setInt(1, doctorId);
            stmt.setInt(2, statusId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при извличане на часове по статус и доктор", e);
        }

        return appointments;
    }

    public List<Appointment> getAppointmentsForTodayByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT * FROM appointments WHERE doctor_id = ? AND date_time >= ? AND date_time < ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1);

            stmt.setInt(1, doctorId);
            stmt.setTimestamp(2, Timestamp.valueOf(startOfDay));
            stmt.setTimestamp(3, Timestamp.valueOf(endOfDay));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при извличане на днешните часове за лекар", e);
        }

        return appointments;
    }
    public boolean isDoctorAvailable(int doctorId, LocalDateTime dateTime) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND date_time = ? AND status_id != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            stmt.setTimestamp(2, Timestamp.valueOf(dateTime));
            stmt.setInt(3, new StatusRepository().getStatusByEnum(StatusEnum.CANCELED).getId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при проверка на заетостта на лекар", e);
        }
        return false;
    }
    public Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractAppointmentFromResultSet(rs);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при извличане на час по ID", e);
        }
        return null;
    }
}
