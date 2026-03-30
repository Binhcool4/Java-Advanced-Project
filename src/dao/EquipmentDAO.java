package dao;

import model.Equipment;
import java.util.List;

public interface EquipmentDAO {
    List<Equipment> findAll();
    boolean insert(Equipment equipment);
    boolean updateQuantity(int id, int quantity);
}
