package business;

import dao.BookingDAO;
import dao.EquipmentDAO;
import dao.RoomDAO;
import dao.impl.BookingDAOImpl;
import dao.impl.EquipmentDAOImpl;
import dao.impl.RoomDAOImpl;
import model.Booking;
import model.BookingDetail;
import model.Equipment;
import model.Room;
import model.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAOImpl();
    private final RoomDAO roomDAO = new RoomDAOImpl();
    private final EquipmentDAO equipmentDAO = new EquipmentDAOImpl();

    /**
     * Get all available rooms
     */
    public List<Room> getAvailableRooms() {
        return roomDAO.findAll().stream()
                .filter(room -> "AVAILABLE".equals(room.getStatus()))
                .toList();
    }

    /**
     * Check if room is available for the given time slot
     */
    public boolean isRoomAvailable(int roomId, LocalDateTime start, LocalDateTime end) {
        return bookingDAO.isRoomAvailable(roomId, start, end);
    }

    /**
     * Create a new booking with equipment requests
     */
    public boolean createBooking(int userId, int roomId, LocalDateTime start, LocalDateTime end, List<BookingDetail> equipmentRequests) {
        // Check time conflict
        if (!isRoomAvailable(roomId, start, end)) {
            return false;
        }

        // Check equipment availability
        for (BookingDetail detail : equipmentRequests) {
            Equipment eq = equipmentDAO.findAll().stream()
                    .filter(e -> e.getId() == detail.getEquipmentId())
                    .findFirst().orElse(null);
            if (eq == null || eq.getQuantity() < detail.getQuantity()) {
                return false;
            }
        }

        // Create booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setStatus(BookingStatus.PENDING);

        if (bookingDAO.insert(booking)) {
            // Insert equipment details
            for (BookingDetail detail : equipmentRequests) {
                detail.setBookingId(booking.getId());
                if (!bookingDAO.insertBookingDetail(detail)) {
                    return false; // Rollback needed, but simplified
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Get bookings by user
     */
    public List<Booking> getBookingsByUser(int userId) {
        return bookingDAO.findByUserId(userId);
    }

    /**
     * Get all bookings (for admin/support)
     */
    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }

    /**
     * Update booking status
     */
    public boolean updateBookingStatus(int bookingId, String status) {
        return bookingDAO.updateStatus(bookingId, status);
    }

    /**
     * Get all equipment
     */
    public List<Equipment> getAllEquipment() {
        return equipmentDAO.findAll();
    }
}
