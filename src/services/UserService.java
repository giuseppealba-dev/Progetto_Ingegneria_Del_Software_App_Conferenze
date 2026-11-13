package services;

import java.util.List;

import data.dao.UserDAO;
import models.User;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public boolean emailExists(String email) {
        return userDAO.emailExists(email);
    }

    public boolean register(User user) {
        return userDAO.save(user);
    }

    public User login(String email, String passwordHash) {
        return userDAO.getByEmailAndPassword(email, passwordHash);
    }

    public User getUserById(int id) {
        return userDAO.getById(id);
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
    
    public boolean updatePassword(String email, String newPasswordHash) {
        return userDAO.updatePassword(email, newPasswordHash);
    }

    public List<User> getRevisoriByConferenza(int conferenzaId) {
        return userDAO.getRevisoriByConferenza(conferenzaId);
    }

}