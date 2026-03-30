package business;

import dao.EquipmentDAO;
import dao.impl.EquipmentDAOImpl;

public class EquipmentService {

    private EquipmentDAO equipmentDAO = new EquipmentDAOImpl();

    public boolean updateQuantity(int id, int quantity) {
        return equipmentDAO.updateQuantity(id, quantity);
    }
}