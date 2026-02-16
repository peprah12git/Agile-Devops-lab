package bookshop.services.serviceimp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookshop.dao.daoInterface.UserDao;
import bookshop.models.User;
import bookshop.services.serviceInterface.UserService;

/**
 * Implementation of UserService with business logic
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User registerUser(User user) {
        // Validate user data
        validateUser(user);

        // Check if email already exists
        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        return userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        return userDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return userDao.findByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        // Validate user data
        validateUser(user);

        // Check if user exists
        Optional<User> existingUser = userDao.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + user.getId());
        }

        // Check if email is being changed to an existing email
        Optional<User> userWithEmail = userDao.findByEmail(user.getEmail());
        if (userWithEmail.isPresent() && userWithEmail.get().getId() != user.getId()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        return userDao.update(user);
    }

    @Override
    public void deleteUser(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }

        // Check if user exists before deleting
        Optional<User> user = userDao.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }

        userDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userDao.findByEmail(email).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> authenticateUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Note: Current User model doesn't have password field
        // This method needs to be redesigned or User model needs password field
        return userDao.findByEmail(email);
    }

    /**
     * Validate user data
     * @param user the user to validate
     */
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // Basic email format validation
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Validate age if provided
        if (user.getAge() != null && user.getAge() < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
}