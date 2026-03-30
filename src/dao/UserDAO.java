package dao;

import model.User;

public interface UserDAO {
    boolean insert(User user);
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
}