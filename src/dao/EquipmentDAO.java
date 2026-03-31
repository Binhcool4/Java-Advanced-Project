package dao;

import model.Equipment;
import java.util.List;

public interface EquipmentDAO {
    List<Equipment> findAll();
    List<Equipment> searchByName(String name);
    boolean insert(Equipment equipment);
    boolean updateQuantity(int id, int quantity);
    boolean delete(int id);
}
