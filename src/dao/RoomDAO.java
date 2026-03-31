package dao;

import model.Room;
import java.util.List;

public interface RoomDAO {
    boolean insert(Room room);
    List<Room> findAll();
    List<Room> searchByName(String name);
    boolean update(int id, String name, int capacity);
    boolean delete(int id);
}
