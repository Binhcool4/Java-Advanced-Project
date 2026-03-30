import util.DBConnection;
import java.sql.Connection;
import model.User;
import model.enums.Role;
import presentation.auth.AuthView;
import presentation.admin.AdminView;
import presentation.employee.EmployeeView;
import presentation.support.SupportStaffView;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("Kết nối thành công!");
        } else {
            System.out.println("Kết nối thất bại!");
        }
        AuthView authView = new AuthView();
        User user = authView.showMenu();

        if (user != null && user.getRole() == Role.ADMIN) {
            AdminView adminView = new AdminView();
            adminView.showMenu();
        } else if (user != null && user.getRole() == Role.EMPLOYEE) {
            EmployeeView employeeView = new EmployeeView();
            employeeView.showMenu(user);
        } else if (user != null && user.getRole() == Role.SUPPORT) {
            SupportStaffView supportView = new SupportStaffView();
            supportView.showMenu(user.getId());
        } else {
            System.out.println("Login thất bại!");
        }
    }
}