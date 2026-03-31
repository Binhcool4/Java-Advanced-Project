package business;

import dao.RoomDAO;
import dao.EquipmentDAO;
import dao.UserDAO;
import dao.impl.RoomDAOImpl;
import dao.impl.EquipmentDAOImpl;
import dao.impl.UserDAOImpl;
import model.Room;
import model.Equipment;
import model.User;
import model.enums.Role;
import util.PasswordHash;

import java.util.List;

public class AdminService {
    private final RoomDAO roomDAO = new RoomDAOImpl();
    private final EquipmentDAO equipmentDAO = new EquipmentDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    // ============== Quản lý Phòng họp ==============
    public boolean addRoom(String name, int capacity) {
        Room room = new Room();
        room.setName(name);
        room.setCapacity(capacity);
        room.setStatus("AVAILABLE");
        return roomDAO.insert(room);
    }

    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }

    public List<Room> searchRoomsByName(String name) {
        return roomDAO.searchByName(name);
    }

    public boolean updateRoom(int id, String name, int capacity) {
        return roomDAO.update(id, name, capacity);
    }

    public boolean deleteRoom(int id) {
        return roomDAO.delete(id);
    }

    // ============== Quản lý Thiết bị ==============
    public boolean addEquipment(String name, int quantity) {
        Equipment equipment = new Equipment();
        equipment.setName(name);
        equipment.setQuantity(quantity);
        return equipmentDAO.insert(equipment);
    }

    public List<Equipment> getAllEquipments() {
        return equipmentDAO.findAll();
    }

    public List<Equipment> searchEquipmentsByName(String name) {
        return equipmentDAO.searchByName(name);
    }

    public boolean updateEquipmentQuantity(int equipmentId, int quantity) {
        return equipmentDAO.updateQuantity(equipmentId, quantity);
    }

    public boolean deleteEquipment(int id) {
        return equipmentDAO.delete(id);
    }

    // ============== Quản lý Người dùng ==============
    public boolean createSupportStaff(String name, String email, String phone, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(PasswordHash.hashPassword(password));
        user.setRole(Role.SUPPORT);
        return userDAO.insert(user);
    }

    public boolean createEmployee(String name, String email, String phone, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(PasswordHash.hashPassword(password));
        user.setRole(Role.EMPLOYEE);
        return userDAO.insert(user);
    }

    // ============== Hồ sơ cá nhân ==============
    public boolean updateAdminProfile(int userId, String name, String email, String phone) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        return userDAO.update(user);
    }
}
