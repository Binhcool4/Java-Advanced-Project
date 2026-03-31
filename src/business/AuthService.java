package business;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;
import model.enums.Role;
import util.PasswordHash;
import util.ValidationUtil;

public class AuthService {

    private final UserDAO userDAO = new UserDAOImpl();

    /**
     * Register a new user with validation
     * @return "success" if registration successful, error message otherwise
     */
    public String register(String name, String email, String phone, String password) {
        // Validate name
        if (!ValidationUtil.isValidName(name)) {
            return "Tên phải có ít nhất 3 ký tự!";
        }

        // Validate email
        if (!ValidationUtil.isValidEmail(email)) {
            return "Email không hợp lệ!";
        }

        // Check if email already exists
        if (userDAO.findByEmail(email) != null) {
            return "Email này đã được sử dụng!";
        }

        // Validate phone
        if (!ValidationUtil.isValidPhone(phone)) {
            return "Số điện thoại không hợp lệ! (Định dạng: 0xxx xxx xxxx hoặc +84xxx xxx xxxx)";
        }

        // Validate password
        if (!ValidationUtil.isValidPassword(password)) {
            return "Mật khẩu phải có ít nhất 6 ký tự!";
        }

        // Create and insert user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(PasswordHash.hashPassword(password));
        user.setRole(Role.EMPLOYEE);

        boolean success = userDAO.insert(user);
        return success ? "success" : "Đăng ký thất bại!";
    }

    public User login(String email, String password) {
        String hashed = PasswordHash.hashPassword(password);
        return userDAO.findByEmailAndPassword(email, hashed);
    }

    public boolean updateProfile(int userId, String name, String email, String phone) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }

        // Validate
        if (!ValidationUtil.isValidName(name)) {
            return false;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            return false;
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            return false;
        }

        // Check if email changed and already exists
        if (!user.getEmail().equals(email) && userDAO.findByEmail(email) != null) {
            return false;
        }

        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        return userDAO.update(user);
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }
}