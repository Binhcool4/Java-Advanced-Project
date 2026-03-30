package presentation.employee;

import business.BookingService;
import model.Booking;
import model.BookingDetail;
import model.Equipment;
import model.Room;
import model.User;
import util.InputUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeView {

    private final BookingService bookingService = new BookingService();
    private final Scanner sc = new Scanner(System.in);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void showMenu(User user) {
        while (true) {
            System.out.println("\n========== EMPLOYEE MENU ==========");
            System.out.println("1. Xem danh sách phòng trống");
            System.out.println("2. Đặt phòng họp");
            System.out.println("3. Xem lịch đặt của tôi");
            System.out.println("0. Đăng xuất");
            System.out.print("Lựa chọn: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        viewAvailableRooms();
                        break;
                    case 2:
                        bookRoom(user);
                        break;
                    case 3:
                        viewMyBookings(user);
                        break;
                    case 0:
                        System.out.println("Đăng xuất thành công!");
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
            }
        }
    }

    private void viewAvailableRooms() {
        System.out.println("\n========== DANH SÁCH PHÒNG TRỐNG ==========");
        List<Room> rooms = bookingService.getAvailableRooms();
        if (rooms.isEmpty()) {
            System.out.println("Không có phòng trống nào.");
            return;
        }

        System.out.printf("%-5s %-20s %-10s%n", "ID", "Tên phòng", "Sức chứa");
        System.out.println("--------------------------------------------");
        for (Room room : rooms) {
            System.out.printf("%-5d %-20s %-10d%n", room.getId(), room.getName(), room.getCapacity());
        }
    }

    private void bookRoom(User user) {
        System.out.println("\n========== ĐẶT PHÒNG HỌP ==========");

        // Select room
        viewAvailableRooms();
        System.out.print("Nhập ID phòng muốn đặt: ");
        int roomId = InputUtil.getInt(sc);
        if (roomId <= 0) return;

        // Check if room exists and available
        List<Room> availableRooms = bookingService.getAvailableRooms();
        Room selectedRoom = availableRooms.stream()
                .filter(r -> r.getId() == roomId)
                .findFirst().orElse(null);
        if (selectedRoom == null) {
            System.out.println("Phòng không hợp lệ hoặc không khả dụng!");
            return;
        }

        // Input date and time
        System.out.print("Nhập ngày (dd/MM/yyyy): ");
        String dateStr = sc.nextLine().trim();
        if (dateStr.isEmpty()) {
            System.out.println("Ngày không được để trống!");
            return;
        }

        System.out.print("Nhập giờ bắt đầu (HH:mm): ");
        String startTimeStr = sc.nextLine().trim();
        if (startTimeStr.isEmpty()) {
            System.out.println("Giờ bắt đầu không được để trống!");
            return;
        }

        System.out.print("Nhập giờ kết thúc (HH:mm): ");
        String endTimeStr = sc.nextLine().trim();
        if (endTimeStr.isEmpty()) {
            System.out.println("Giờ kết thúc không được để trống!");
            return;
        }

        LocalDateTime startTime, endTime;
        try {
            startTime = LocalDateTime.parse(dateStr + " " + startTimeStr, formatter);
            endTime = LocalDateTime.parse(dateStr + " " + endTimeStr, formatter);
        } catch (Exception e) {
            System.out.println("Định dạng ngày giờ không hợp lệ! Sử dụng dd/MM/yyyy HH:mm");
            return;
        }

        if (!endTime.isAfter(startTime)) {
            System.out.println("Giờ kết thúc phải sau giờ bắt đầu!");
            return;
        }

        // Check availability
        if (!bookingService.isRoomAvailable(roomId, startTime, endTime)) {
            System.out.println("Phòng đã được đặt trong khoảng thời gian này!");
            return;
        }

        // Select equipment
        List<BookingDetail> equipmentRequests = selectEquipment();

        // Confirm booking
        System.out.println("\n========== XÁC NHẬN ĐẶT PHÒNG ==========");
        System.out.println("Phòng: " + selectedRoom.getName());
        System.out.println("Thời gian: " + startTime.format(formatter) + " - " + endTime.format(formatter));
        if (!equipmentRequests.isEmpty()) {
            System.out.println("Thiết bị yêu cầu:");
            for (BookingDetail detail : equipmentRequests) {
                Equipment eq = bookingService.getAllEquipment().stream()
                        .filter(e -> e.getId() == detail.getEquipmentId())
                        .findFirst().orElse(null);
                if (eq != null) {
                    System.out.println("- " + eq.getName() + ": " + detail.getQuantity());
                }
            }
        }
        System.out.print("Xác nhận đặt phòng? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!"y".equals(confirm) && !"yes".equals(confirm)) {
            System.out.println("Đã hủy đặt phòng.");
            return;
        }

        // Create booking
        boolean success = bookingService.createBooking(user.getId(), roomId, startTime, endTime, equipmentRequests);
        if (success) {
            System.out.println("Đặt phòng thành công! Đang chờ phê duyệt.");
        } else {
            System.out.println("Đặt phòng thất bại! Vui lòng thử lại.");
        }
    }

    private List<BookingDetail> selectEquipment() {
        List<BookingDetail> requests = new ArrayList<>();
        List<Equipment> equipments = bookingService.getAllEquipment();

        if (equipments.isEmpty()) {
            return requests;
        }

        System.out.println("\n========== CHỌN THIẾT BỊ ==========");
        System.out.printf("%-5s %-20s %-10s%n", "ID", "Tên thiết bị", "Số lượng có sẵn");
        System.out.println("--------------------------------------------");
        for (Equipment eq : equipments) {
            System.out.printf("%-5d %-20s %-10d%n", eq.getId(), eq.getName(), eq.getQuantity());
        }

        while (true) {
            System.out.print("Nhập ID thiết bị (0 để kết thúc): ");
            int eqId = InputUtil.getInt(sc);
            if (eqId == 0) break;
            if (eqId < 0) continue;

            Equipment eq = equipments.stream()
                    .filter(e -> e.getId() == eqId)
                    .findFirst().orElse(null);
            if (eq == null) {
                System.out.println("ID thiết bị không hợp lệ!");
                continue;
            }

            System.out.print("Nhập số lượng cần: ");
            int quantity = InputUtil.getInt(sc);
            if (quantity <= 0) {
                System.out.println("Số lượng phải > 0!");
                continue;
            }
            if (quantity > eq.getQuantity()) {
                System.out.println("Số lượng yêu cầu vượt quá số lượng có sẵn!");
                continue;
            }

            BookingDetail detail = new BookingDetail();
            detail.setEquipmentId(eqId);
            detail.setQuantity(quantity);
            requests.add(detail);

            System.out.print("Thêm thiết bị khác? (y/n): ");
            String more = sc.nextLine().trim().toLowerCase();
            if (!"y".equals(more) && !"yes".equals(more)) {
                break;
            }
        }

        return requests;
    }

    private void viewMyBookings(User user) {
        System.out.println("\n========== LỊCH ĐẶT CỦA TÔI ==========");
        List<Booking> bookings = bookingService.getBookingsByUser(user.getId());
        if (bookings.isEmpty()) {
            System.out.println("Bạn chưa có lịch đặt nào.");
            return;
        }

        System.out.printf("%-5s %-15s %-20s %-20s %-10s %-15s%n", "ID", "Phòng", "Bắt đầu", "Kết thúc", "Trạng thái", "Chuẩn bị");
        System.out.println("--------------------------------------------------------------------------------------------");
        for (Booking booking : bookings) {
            Room room = bookingService.getAvailableRooms().stream()
                    .filter(r -> r.getId() == booking.getRoomId())
                    .findFirst().orElse(null);
            String roomName = room != null ? room.getName() : "Unknown";
            String prepStatus = booking.getPreparationStatus() != null ? booking.getPreparationStatus().toString() : "N/A";
            System.out.printf("%-5d %-15s %-20s %-20s %-10s %-15s%n",
                booking.getId(),
                roomName,
                booking.getStartTime().format(formatter),
                booking.getEndTime().format(formatter),
                booking.getStatus(),
                prepStatus
            );
        }
    }
}
