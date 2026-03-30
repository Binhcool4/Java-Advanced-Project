package presentation.admin;

import business.AdminService;
import model.Room;
import model.Equipment;
import util.InputUtil;

import java.util.List;
import java.util.Scanner;

public class AdminView {
    private final AdminService adminService = new AdminService();
    private final Scanner sc = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. Quản lý Phòng họp");
            System.out.println("2. Quản lý Thiết bị");
            System.out.println("3. Quản lý Người dùng");
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
                case 0:
                    System.out.println("Đã đăng xuất!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void roomManagement() {
        while (true) {
            System.out.println("\n--- Quản lý Phòng họp ---");
            System.out.println("1. Xem tất cả phòng");
            System.out.println("2. Thêm phòng");
            System.out.println("3. Sửa phòng");
            System.out.println("4. Xóa phòng");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            int choice = InputUtil.getInt();

            switch (choice) {
                case 1:
                    viewAllRooms();
                    break;
                case 2:
                    addRoom();
                    break;
                case 3:
                    editRoom();
                    break;
                case 4:
                    deleteRoom();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

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

    private void deleteRoom() {
        System.out.print("ID phòng cần xóa: ");
        int id = InputUtil.getInt();

        if (adminService.deleteRoom(id)) {
            System.out.println("Xóa phòng thành công!");
        } else {
            System.out.println("Xóa phòng thất bại!");
        }
    }

    private void equipmentManagement() {
        while (true) {
            System.out.println("\n--- Quản lý Thiết bị ---");
            System.out.println("1. Xem tất cả thiết bị");
            System.out.println("2. Thêm thiết bị");
            System.out.println("3. Cập nhật số lượng thiết bị");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            int choice = InputUtil.getInt();

            switch (choice) {
                case 1:
                    viewAllEquipments();
                    break;
                case 2:
                    addEquipment();
                    break;
                case 3:
                    updateEquipmentQuantity();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

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
            System.out.println("✓ Tạo tài khoản Support Staff thành công!");
        } else {
            System.out.println("❌ Tạo tài khoản thất bại!");
        }
    }

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
            System.out.println("✓ Tạo tài khoản Employee thành công!");
        } else {
            System.out.println("❌ Tạo tài khoản thất bại!");
        }
    }
}
