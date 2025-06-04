package repository;

import models.Status;
import models.enums.StatusEnum;
import utils.DBConnection;
import utils.LoggerConfig;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatusRepository {

    private static final Logger logger = Logger.getLogger(StatusRepository.class.getName());
    static {
        LoggerConfig.configureLogger(logger);
    }

    public void insertStatus(Status status) {
        String sql = "INSERT INTO statuses (name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.getStatusEnum().getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при добавяне на статус", e);
        }
    }

    public boolean hasStatuses() {
        String sql = "SELECT COUNT(*) FROM statuses";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при проверка на съществуващи статуси", e);
        }
        return false;
    }

    public void initializeStatuses() {
        if (!hasStatuses()) {
            System.out.println("Вмъкване на статуси в базата данни...");
            for (StatusEnum statusEnum : StatusEnum.values()) {
                insertStatus(new Status(0, statusEnum));
            }
            System.out.println("Статусите са добавени успешно!");
        } else {
            System.out.println("Таблицата 'statuses' вече съдържа записи.");
        }
    }

    public Status getStatusById(int id) {
        String sql = "SELECT * FROM statuses WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                for (StatusEnum se : StatusEnum.values()) {
                    if (se.getName().equalsIgnoreCase(name)) {
                        return new Status(id, se);
                    }
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при извличане на статус по ID", e);
        }

        return null;
    }

    public Status getStatusByEnum(StatusEnum statusEnum) {
        String sql = "SELECT * FROM statuses WHERE name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statusEnum.getName());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                return new Status(id, statusEnum);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Грешка при извличане на статус по Enum", e);
        }

        return null;
    }
}
