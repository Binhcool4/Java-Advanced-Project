package dao.impl;

import dao.EquipmentDAO;
import model.Equipment;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAOImpl implements EquipmentDAO {

    @Override
    public List<Equipment> findAll() {
        List<Equipment> equipments = new ArrayList<>();
        String sql = "SELECT * FROM equipments";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setId(rs.getInt("id"));
                equipment.setName(rs.getString("name"));
                equipment.setQuantity(rs.getInt("quantity"));
                equipments.add(equipment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return equipments;
    }

    @Override
    public boolean updateQuantity(int id, int quantity) {
        String sql = "UPDATE equipments SET quantity=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insert(Equipment equipment) {
        String sql = "INSERT INTO equipments(name, quantity) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipment.getName());
            ps.setInt(2, equipment.getQuantity());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
