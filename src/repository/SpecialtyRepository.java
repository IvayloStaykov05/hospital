package repository;

import models.Specialty;
import models.enums.SpecialtyEnum;
import utils.DBConnection;

import java.sql.*;

public class SpecialtyRepository {

    public void insertSpecialty(Specialty specialty){
        String sql = "INSERT INTO specialties (name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, specialty.getSpecialtyEnum().getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean hasSpecialty() {
        String sql = "SELECT COUNT(*) FROM specialties";

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
    public void initializeSpecialties() {
        if (!hasSpecialty()) {
            System.out.println("Вмъкване на статуси в базата данни...");
            for (SpecialtyEnum specialtyEnum : SpecialtyEnum.values()) {
                insertSpecialty(new Specialty(0, specialtyEnum));
            }
            System.out.println("Статусите са добавени успешно!");
        } else {
            System.out.println("Таблицата 'statuses' вече съдържа записи.");
        }
    }
    public Specialty getSpecialtyById(int id){
        String sql = "SELECT * FROM specialties WHERE id = ?";

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                String name = rs.getString("name");
                for (SpecialtyEnum enumValue : SpecialtyEnum.values()){
                    if (enumValue.getName().equalsIgnoreCase(name)){
                        return new Specialty(id, enumValue);
                    }
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
