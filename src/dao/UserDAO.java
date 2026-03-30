package dao;

import model.User;
import java.util.List;

public interface UserDAO {
    boolean insert(User user);
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
    List<User> findByRole(String role);
}