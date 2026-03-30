package dao.impl;

import dao.BookingDAO;
import model.Booking;
import model.BookingDetail;
import model.enums.BookingStatus;
import model.enums.PreparationStatus;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public boolean insert(Booking booking) {
        String sql = "INSERT INTO bookings(user_id, room_id, start_time, end_time, status, support_staff_id, preparation_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getRoomId());
            ps.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            ps.setString(5, booking.getStatus().name());
            if (booking.getSupportStaffId() != null) {
                ps.setInt(6, booking.getSupportStaffId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            if (booking.getPreparationStatus() != null) {
                ps.setString(7, booking.getPreparationStatus().name());
            } else {
                ps.setNull(7, Types.VARCHAR);
            }
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    booking.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertBookingDetail(BookingDetail detail) {
        String sql = "INSERT INTO booking_details(booking_id, equipment_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getBookingId());
            ps.setInt(2, detail.getEquipmentId());
            ps.setInt(3, detail.getQuantity());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Booking> findByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isRoomAvailable(int roomId, LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE room_id = ? AND status != 'REJECTED' AND " +
                     "((start_time < ? AND end_time > ?) OR (start_time < ? AND end_time > ?) OR (start_time >= ? AND end_time <= ?))";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setTimestamp(2, Timestamp.valueOf(end));
            ps.setTimestamp(3, Timestamp.valueOf(start));
            ps.setTimestamp(4, Timestamp.valueOf(start));
            ps.setTimestamp(5, Timestamp.valueOf(end));
            ps.setTimestamp(6, Timestamp.valueOf(start));
            ps.setTimestamp(7, Timestamp.valueOf(end));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateSupportStaff(int bookingId, Integer supportStaffId) {
        String sql = "UPDATE bookings SET support_staff_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (supportStaffId != null) {
                ps.setInt(1, supportStaffId);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updatePreparationStatus(int bookingId, String preparationStatus) {
        String sql = "UPDATE bookings SET preparation_status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, preparationStatus);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Booking> findBySupportStaffId(int supportStaffId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE support_staff_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supportStaffId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        booking.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        int supportStaffId = rs.getInt("support_staff_id");
        if (rs.wasNull()) {
            booking.setSupportStaffId(null);
        } else {
            booking.setSupportStaffId(supportStaffId);
        }
        String prepStatus = rs.getString("preparation_status");
        if (prepStatus != null) {
            booking.setPreparationStatus(model.enums.PreparationStatus.valueOf(prepStatus));
        }
        return booking;
    }
}
