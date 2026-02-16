package bookshop.controller;

import bookshop.dto.request.UserRequestdto;
import bookshop.dto.response.UserRegistrationdto;
import bookshop.dto.response.UserResponsedto;
import bookshop.exceptions.EmailAlreadyExistsException;
import bookshop.exceptions.UserNotFoundException;
import bookshop.models.User;
import bookshop.services.serviceInterface.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for User operations
 */
@RestController
@RequestMapping("/api/users")
public class
UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user
     * 
     * POST /api/users/register
     * 
     * @param registrationDto the user registration data
     * @return the created user with HTTP 201 status
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponsedto> registerUser(@Valid @RequestBody UserRegistrationdto registrationDto) {
        // Check if email already exists
        if (userService.emailExists(registrationDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered: " + registrationDto.getEmail());
        }

        // Create user from DTO
        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setCourse(registrationDto.getRole()); // Using course field to store role
        // Note: Password hashing should be done in service layer
        
        User createdUser = userService.registerUser(user);
        
        // Build response
        UserResponsedto response = buildUserResponse(createdUser);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all users
     * 
     * GET /api/users
     * 
     * @return list of all users
     */
    @GetMapping
    public ResponseEntity<List<UserResponsedto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponsedto> response = users.stream()
                .map(this::buildUserResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get user by ID
     * 
     * GET /api/users/{id}
     * 
     * @param id the user ID
     * @return the user information
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponsedto> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        
        UserResponsedto response = buildUserResponse(user);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get user by email
     * 
     * GET /api/users/email/{email}
     * 
     * @param email the user email
     * @return the user information
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponsedto> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        
        UserResponsedto response = buildUserResponse(user);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Update user information
     * 
     * PUT /api/users/{id}
     * 
     * @param id the user ID
     * @param userRequestDto the updated user data
     * @return the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponsedto> updateUser(
            @PathVariable int id,
            @Valid @RequestBody UserRequestdto userRequestDto) {
        
        // Verify user exists
        User existingUser = userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        
        // Update user fields
        existingUser.setName(userRequestDto.getName());
        existingUser.setEmail(userRequestDto.getEmail());
        
        User updatedUser = userService.updateUser(existingUser);
        UserResponsedto response = buildUserResponse(updatedUser);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete user by ID
     * 
     * DELETE /api/users/{id}
     * 
     * @param id the user ID
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        // Verify user exists before deleting
        userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if email exists
     * 
     * GET /api/users/exists/email/{email}
     * 
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    /**
     * Get user count
     * 
     * GET /api/users/count
     * 
     * @return the total number of users
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getUserCount() {
        int count = userService.getAllUsers().size();
        return ResponseEntity.ok(count);
    }

    /**
     * Helper method to build UserResponseDto from User model
     * 
     * @param user the user model
     * @return user response DTO
     */
    private UserResponsedto buildUserResponse(User user) {
        UserResponsedto response = new UserResponsedto();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }
}
