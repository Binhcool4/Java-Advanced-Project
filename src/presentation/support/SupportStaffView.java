package presentation.support;

import business.BookingService;
import model.Booking;
import model.enums.PreparationStatus;
import util.InputUtil;

import java.util.List;
import java.util.Scanner;

public class SupportStaffView {
    private final BookingService bookingService = new BookingService();
    private final Scanner sc = new Scanner(System.in);

    public void showMenu(int userId) {
        while (true) {
            System.out.println("\n========== SUPPORT STAFF MENU ==========");
            System.out.println("1. Xem công việc được phân công");
            System.out.println("2. Cập nhật trạng thái chuẩn bị");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");

            int choice = InputUtil.getInt();

            switch (choice) {
                case 1:
                    viewAssignedTasks(userId);
                    break;
                case 2:
                    updatePreparationStatus(userId);
                    break;
                case 0:
                    System.out.println("Đã đăng xuất!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewAssignedTasks(int userId) {
        List<Booking> bookings = bookingService.getBookingsBySupportStaff(userId);
        if (bookings.isEmpty()) {
            System.out.println("Không có công việc nào được phân công!");
            return;
        }
        System.out.println("\n--- Danh sách Công việc ---");
        for (Booking booking : bookings) {
            System.out.printf("ID: %d | Phòng: %d | Thời gian: %s - %s | Trạng thái: %s%n",
                    booking.getId(), booking.getRoomId(), booking.getStartTime(), booking.getEndTime(),
                    booking.getPreparationStatus() != null ? booking.getPreparationStatus() : "Chưa cập nhật");
        }
    }

    private void updatePreparationStatus(int userId) {
        System.out.print("ID booking cần cập nhật: ");
        int bookingId = InputUtil.getInt();

        System.out.println("Chọn trạng thái:");
        System.out.println("1. PREPARING");
        System.out.println("2. READY");
        System.out.println("3. MISSING_EQUIPMENT");
        System.out.print("Chọn: ");
        int statusChoice = InputUtil.getInt();
        String status;
        switch (statusChoice) {
            case 1:
                status = "PREPARING";
                break;
            case 2:
                status = "READY";
                break;
            case 3:
                status = "MISSING_EQUIPMENT";
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        if (bookingService.updatePreparationStatus(bookingId, status)) {
            System.out.println("Cập nhật thành công!");
        } else {
            System.out.println("Cập nhật thất bại!");
        }
    }
}
