package presentation.auth;

import business.AuthService;
import model.User;

import java.util.Scanner;

public class AuthView {

    private final AuthService authService = new AuthService();
    private final Scanner sc = new Scanner(System.in);

    public User showMenu() {
        while (true) {
            System.out.println("\n========== ĐĂNG NHẬP / ĐĂNG KÝ ==========");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                if (choice == 1) {
                    User user = handleLogin();
                    if (user != null) {
                        return user;
                    }
                } else if (choice == 2) {
                    handleRegister();
                } else if (choice == 0) {
                    System.out.println("Tạm biệt!");
                    System.exit(0);
                } else {
                    System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
            }
        }
    }

    private User handleLogin() {
        System.out.println("\n========== ĐĂNG NHẬP ==========");
        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Mật khẩu: ");
        String password = sc.nextLine();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email và mật khẩu không được để trống!");
            return null;
        }

        User user = authService.login(email, password);

        if (user != null) {
            System.out.println("Đăng nhập thành công! Vai trò: " + user.getRole());
            return user;
        } else {
            System.out.println("Email hoặc mật khẩu không chính xác!");
            return null;
        }
    }

    private void handleRegister() {
        System.out.println("\n========== ĐĂNG KÝ TÀI KHOẢN ==========");

        System.out.print("Họ và tên: ");
        String name = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Số điện thoại (VD: 0123456789): ");
        String phone = sc.nextLine().trim();

        System.out.print("Mật khẩu (ít nhất 6 ký tự): ");
        String password = sc.nextLine();

        System.out.print("Xác nhận mật khẩu: ");
        String confirmPassword = sc.nextLine();

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            System.out.println("Mật khẩu không trùng khớp!");
            return;
        }

        // Call service and get result
        String result = authService.register(name, email, phone, password);

        if ("success".equals(result)) {
            System.out.println("Đăng ký thành công! Bạn có thể đăng nhập ngay.");
        } else {
            System.out.println(result);
        }
    }
}