package bookshop.services.serviceInterface;

import bookshop.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * Register a new user
     * @param user the user to register
     * @return the registered user with generated ID
     */
    User registerUser(User user);

    /**
     * Get all users
     * @return list of all users
     */
    List<User> getAllUsers();

    /**
     * Find user by ID
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> getUserById(int id);

    /**
     * Find user by email
     * @param email the user email
     * @return Optional containing the user if found
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Update user information
     * @param user the user with updated information
     * @return the updated user
     */
    User updateUser(User user);

    /**
     * Delete user by ID
     * @param id the user ID to delete
     */
    void deleteUser(int id);

    /**
     * Check if email already exists
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean emailExists(String email);

    /**
     * Authenticate user with email and password
     * @param email user email
     * @param password user password
     * @return Optional containing the user if authentication successful
     */
    Optional<User> authenticateUser(String email, String password);
}
