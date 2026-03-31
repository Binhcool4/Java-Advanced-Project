package presentation.admin;

import business.AdminService;
import business.BookingService;
import business.UserService;
import model.Booking;
import model.Room;
import model.Equipment;
import model.User;
import util.InputUtil;

import java.util.List;
import java.util.Scanner;

public class AdminView {
    private final AdminService adminService = new AdminService();
    private final BookingService bookingService = new BookingService();
    private final UserService userService = new UserService();
    private final Scanner sc = new Scanner(System.in);

    public void showMenu(User user) {
        while (true) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. Quản lý Phòng họp");
            System.out.println("2. Quản lý Thiết bị");
            System.out.println("3. Quản lý Người dùng");
            System.out.println("4. Quản lý Đặt phòng");
            System.out.println("5. Xem hồ sơ cá nhân");
            System.out.println("6. Cập nhật hồ sơ cá nhân");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");

            int choice = InputUtil.getInt();

            switch (choice) {
                case 1:
                    roomManagement();
                    break;
                case 2:
                    equipmentManagement();
                    break;
                case 3:
                    userManagement();
                    break;
                case 4:
                    bookingManagement();
                    break;
                case 5:
                    viewProfile(user);
                    break;
                case 6:
                    updateProfile(user);
                    break;
                case 0:
                    System.out.println("Đã đăng xuất!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    //CASE 1: Quan ly phong hop
    private void roomManagement() {
        while (true) {
            System.out.println("\n--- Quản lý Phòng họp ---");
            System.out.println("1. Xem tất cả phòng");
            System.out.println("2. Tìm kiếm phòng theo tên");
            System.out.println("3. Thêm phòng");
            System.out.println("4. Sửa phòng");
            System.out.println("5. Xóa phòng");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            int choice = InputUtil.getInt();

            switch (choice) {
                case 1:
                    viewAllRooms();
                    break;
                case 2:
                    searchRoomsByName();
                    break;
                case 3:
                    addRoom();
                    break;
                case 4:
                    editRoom();
                    break;
                case 5:
                    deleteRoom();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    //Xem tất cả phòng
    private void viewAllRooms() {
        List<Room> rooms = adminService.getAllRooms();
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("Không có phòng nào!");
            return;
        }
        System.out.println("\n--- Danh sách Phòng ---");
        for (Room room : rooms) {
            System.out.printf("ID: %d | Tên: %s | Sức chứa: %d | Trạng thái: %s%n",
                    room.getId(), room.getName(), room.getCapacity(), room.getStatus());
        }
    }

    //Tìm kiếm phòng theo tên
    private void searchRoomsByName() {
        System.out.print("Nhập tên phòng cần tìm: ");
        String name = sc.nextLine();

        List<Room> rooms = adminService.searchRoomsByName(name);
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("Không tìm thấy phòng nào với tên này!");
            return;
        }
        System.out.println("\n--- Kết quả tìm kiếm ---");
        for (Room room : rooms) {
            System.out.printf("ID: %d | Tên: %s | Sức chứa: %d | Trạng thái: %s%n",
                    room.getId(), room.getName(), room.getCapacity(), room.getStatus());
        }
    }

    //Thêm phòng
    private void addRoom() {
        System.out.print("Tên phòng: ");
        String name = sc.nextLine();
        System.out.print("Sức chứa: ");
        int capacity = InputUtil.getInt();

        if (adminService.addRoom(name, capacity)) {
            System.out.println("Thêm phòng thành công!");
        } else {
            System.out.println("Thêm phòng thất bại!");
        }
    }

    //Sửa phòng
    private void editRoom() {
        System.out.print("ID phòng cần sửa: ");
        int id = InputUtil.getInt();
        System.out.print("Tên mới: ");
        String name = sc.nextLine();
        System.out.print("Sức chứa mới: ");
        int capacity = InputUtil.getInt();

        if (adminService.updateRoom(id, name, capacity)) {
            System.out.println("Sửa phòng thành công!");
        } else {
            System.out.println("Sửa phòng thất bại!");
        }
    }

    //Xóa phòng
    private void deleteRoom() {
        System.out.print("ID phòng cần xóa: ");
        int id = InputUtil.getInt();

        System.out.print("Bạn có chắc chắn muốn xóa phòng này? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!"y".equals(confirm) && !"yes".equals(confirm)) {
            System.out.println("Đã hủy xóa phòng.");
            return;
        }

        if (adminService.deleteRoom(id)) {
            System.out.println("Xóa phòng thành công!");
        } else {
            System.out.println("Xóa phòng thất bại!");
        }
    }

    //CASE 2: Quan ly thiet bi
    private void equipmentManagement() {
        while (true) {
            System.out.println("\n--- Quản lý Thiết bị ---");
            System.out.println("1. Xem tất cả thiết bị");
            System.out.println("2. Tìm kiếm thiết bị theo tên");
            System.out.println("3. Thêm thiết bị");
            System.out.println("4. Cập nhật số lượng thiết bị");
            System.out.println("5. Xóa thiết bị");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            int choice = InputUtil.getInt();

            switch (choice) {
                case 1:
                    viewAllEquipments();
                    break;
                case 2:
                    searchEquipmentsByName();
                    break;
                case 3:
                    addEquipment();
                    break;
                case 4:
                    updateEquipmentQuantity();
                    break;
                case 5:
                    deleteEquipment();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    //Xem tất cả thiết bị
    private void viewAllEquipments() {
        List<Equipment> equipments = adminService.getAllEquipments();
        if (equipments == null || equipments.isEmpty()) {
            System.out.println("Không có thiết bị nào!");
            return;
        }
        System.out.println("\n--- Danh sách Thiết bị ---");
        for (Equipment eq : equipments) {
            System.out.printf("ID: %d | Tên: %s | Số lượng: %d%n",
                    eq.getId(), eq.getName(), eq.getQuantity());
        }
    }

    //Tìm kiếm thiết bị theo tên
    private void searchEquipmentsByName() {
        System.out.print("Nhập tên thiết bị cần tìm: ");
        String name = sc.nextLine();

        List<Equipment> equipments = adminService.searchEquipmentsByName(name);
        if (equipments == null || equipments.isEmpty()) {
            System.out.println("Không tìm thấy thiết bị nào với tên này!");
            return;
        }
        System.out.println("\n--- Kết quả tìm kiếm ---");
        for (Equipment eq : equipments) {
            System.out.printf("ID: %d | Tên: %s | Số lượng: %d%n",
                    eq.getId(), eq.getName(), eq.getQuantity());
        }
    }

    //Them thiet bi
    private void addEquipment() {
        System.out.print("Tên thiết bị: ");
        String name = sc.nextLine();
        System.out.print("Số lượng: ");
        int quantity = InputUtil.getInt();

        if (adminService.addEquipment(name, quantity)) {
            System.out.println("Thêm thiết bị thành công!");
        } else {
            System.out.println("Thêm thiết bị thất bại!");
        }
    }

    //Cap nhat thiet bi
    private void updateEquipmentQuantity() {
        System.out.print("ID thiết bị: ");
        int equipmentId = InputUtil.getInt();
        System.out.print("Số lượng mới: ");
        int quantity = InputUtil.getInt();

        if (adminService.updateEquipmentQuantity(equipmentId, quantity)) {
            System.out.println("Cập nhật thành công!");
        } else {
            System.out.println("Cập nhật thất bại!");
        }
    }

    //Xóa thiết bị
    private void deleteEquipment() {
        System.out.print("ID thiết bị cần xóa: ");
        int id = InputUtil.getInt();

        System.out.print("Bạn có chắc chắn muốn xóa thiết bị này? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!"y".equals(confirm) && !"yes".equals(confirm)) {
            System.out.println("Đã hủy xóa thiết bị.");
            return;
        }

        if (adminService.deleteEquipment(id)) {
            System.out.println("Xóa thiết bị thành công!");
        } else {
            System.out.println("Xóa thiết bị thất bại!");
        }
    }

    //CASE 3: Quan ly nguoi dung
    private void userManagement() {
        while (true) {
            System.out.println("\n--- Quản lý Người dùng ---");
            System.out.println("1. Tạo tài khoản Support Staff");
            System.out.println("2. Tạo tài khoản Employee");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            int choice = InputUtil.getInt();

            switch (choice) {
                case 1:
                    createSupportStaff();
                    break;
                case 2:
                    createEmployee();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    //Tạo tài khoản Support
    private void createSupportStaff() {
        System.out.print("Tên: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Số điện thoại: ");
        String phone = sc.nextLine();
        System.out.print("Mật khẩu: ");
        String password = sc.nextLine();

        if (adminService.createSupportStaff(name, email, phone, password)) {
            System.out.println("Tạo tài khoản Support Staff thành công!");
        } else {
            System.out.println("Tạo tài khoản thất bại!");
        }
    }

    //Tạo tài khoản Employee
    private void createEmployee() {
        System.out.print("Tên: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Số điện thoại: ");
        String phone = sc.nextLine();
        System.out.print("Mật khẩu: ");
        String password = sc.nextLine();

        if (adminService.createEmployee(name, email, phone, password)) {
            System.out.println("Tạo tài khoản Employee thành công!");
        } else {
            System.out.println("Tạo tài khoản thất bại!");
        }
    }

    //CASE 4: Quan ly dat phong
    private void bookingManagement() {
        while (true) {
            System.out.println("\n--- Quản lý Đặt phòng ---");
            System.out.println("1. Xem yêu cầu đặt phòng");
            System.out.println("2. Phê duyệt yêu cầu");
            System.out.println("3. Từ chối yêu cầu");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            int choice = InputUtil.getInt();

            switch (choice) {
                case 1:
                    viewPendingBookings();
                    break;
                case 2:
                    approveBooking();
                    break;
                case 3:
                    rejectBooking();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void viewPendingBookings() {
        List<Booking> bookings = bookingService.getPendingBookings();
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("Không có yêu cầu đặt phòng nào!");
            return;
        }
        System.out.println("\n--- Danh sách Yêu cầu Đặt phòng ---");
        for (Booking booking : bookings) {
            System.out.printf("ID: %d | Người đặt: %d | Phòng: %d | Thời gian: %s - %s%n",
                    booking.getId(), booking.getUserId(), booking.getRoomId(), booking.getStartTime(), booking.getEndTime());
        }
    }

    private void approveBooking() {
        System.out.print("ID yêu cầu cần phê duyệt: ");
        int bookingId = InputUtil.getInt();

        // List support staff
        List<User> supportStaff = userService.getSupportStaff();
        if (supportStaff.isEmpty()) {
            System.out.println("Không có Support Staff nào!");
            return;
        }
        System.out.println("Danh sách Support Staff:");
        for (User staff : supportStaff) {
            System.out.printf("ID: %d | Tên: %s%n", staff.getId(), staff.getName());
        }
        System.out.print("Chọn ID Support Staff: ");
        int staffId = InputUtil.getInt();

        if (bookingService.approveBooking(bookingId, staffId)) {
            System.out.println("Phê duyệt thành công!");
        } else {
            System.out.println("Phê duyệt thất bại!");
        }
    }

    private void rejectBooking() {
        System.out.print("ID yêu cầu cần từ chối: ");
        int bookingId = InputUtil.getInt();

        if (bookingService.rejectBooking(bookingId)) {
            System.out.println("Từ chối thành công!");
        } else {
            System.out.println("Từ chối thất bại!");
        }
    }

    //CASE 5: Xem ho so ca nhan
    private void viewProfile(User user) {
        System.out.println("\n--- Hồ sơ cá nhân ---");
        System.out.printf("ID: %d | Tên: %s | Email: %s | Số điện thoại: %s%n",
                user.getId(), user.getName(), user.getEmail(), user.getPhone());
    }

    //CASE 6: Cap nhat ho so ca nhan
    private void updateProfile(User user) {
        System.out.println("\n--- Cập nhật hồ sơ cá nhân ---");
        System.out.print("Tên mới: ");
        String newName = sc.nextLine();
        System.out.print("Email mới: ");
        String newEmail = sc.nextLine();
        System.out.print("Số điện thoại mới: ");
        String newPhone = sc.nextLine();

        if (adminService.updateAdminProfile(user.getId(), newName, newEmail, newPhone)) {
            System.out.println("Cập nhật hồ sơ thành công!");
        } else {
            System.out.println("Cập nhật hồ sơ thất bại!");
        }
    }
}
