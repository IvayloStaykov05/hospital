package repository;

import models.Status;
import models.enums.StatusEnum;
import utils.DBConnection;

import java.sql.*;

public class StatusRepository {

    public void insertStatus(Status status) {
        String sql = "INSERT INTO statuses (name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.getStatusEnum().getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
}
