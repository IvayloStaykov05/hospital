package repository;

import models.ExaminationType;
import models.enums.ExaminationTypeEnum;
import utils.DBConnection;
import utils.LoggerConfig;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ExaminationTypeRepository {

    private static final Logger logger = Logger.getLogger(ExaminationTypeRepository.class.getName());
    static {
        LoggerConfig.configureLogger(logger);
    }

    public void insertExaminationType(ExaminationType examinationType){
        String sql = "INSERT INTO examinations_type (name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, examinationType.getExaminationTypeEnum().getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при добавяне на тип преглед", e);
        }
    }
    public boolean hasExaminationType() {
        String sql = "SELECT COUNT(*) FROM examinations_type";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при проверка за типове преглед", e);
        }
        return false;
    }
    public void initializeExaminationType() {
        if (!hasExaminationType()) {
            System.out.println("Вмъкване на статуси в базата данни...");
            for (ExaminationTypeEnum examinationTypeEnum : ExaminationTypeEnum.values()) {
                insertExaminationType(new ExaminationType(0, examinationTypeEnum));
            }
            System.out.println("Статусите са добавени успешно!");
        } else {
            System.out.println("Таблицата 'statuses' вече съдържа записи.");
        }
    }
    public ExaminationType getExaminationTypeById(int id) {
        String sql = "SELECT * FROM examinations_type WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                for (ExaminationTypeEnum type : ExaminationTypeEnum.values()) {
                    if (type.getName().equalsIgnoreCase(name)) {
                        return new ExaminationType(id, type);
                    }
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при извличане на тип преглед по ID", e);
        }

        return null;
    }
}
