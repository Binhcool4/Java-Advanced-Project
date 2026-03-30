package dao;

import model.Booking;
import model.BookingDetail;
import java.util.List;

public interface BookingDAO {
    boolean insert(Booking booking);
    boolean insertBookingDetail(BookingDetail detail);
    List<Booking> findByUserId(int userId);
    List<Booking> findAll();
    boolean updateStatus(int id, String status);
    boolean isRoomAvailable(int roomId, java.time.LocalDateTime start, java.time.LocalDateTime end);
}
