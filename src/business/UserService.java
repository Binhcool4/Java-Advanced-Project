package business;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;
import model.enums.Role;
import util.PasswordHash;
import java.util.List;

public class UserService {

    private UserDAO userDAO = new UserDAOImpl();

    public boolean createSupport(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(PasswordHash.hashPassword(password));
        user.setRole(Role.SUPPORT);

        return userDAO.insert(user);
    }

    public List<User> getSupportStaff() {
        return userDAO.findByRole("SUPPORT");
    }
}