package business;

import dao.RoomDAO;
import dao.impl.RoomDAOImpl;
import model.Room;

import java.util.List;

public class RoomService {

    private RoomDAO roomDAO = new RoomDAOImpl();

    public boolean addRoom(String name, int capacity) {
        Room r = new Room();
        r.setName(name);
        r.setCapacity(capacity);
        r.setStatus("AVAILABLE");
        return roomDAO.insert(r);
    }

    public List<Room> getAll() {
        return roomDAO.findAll();
    }

    public boolean update(int id, String name, int capacity) {
        return roomDAO.update(id, name, capacity);
    }

    public boolean delete(int id) {
        return roomDAO.delete(id);
    }
}