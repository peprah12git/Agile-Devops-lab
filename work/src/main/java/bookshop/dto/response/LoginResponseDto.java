package bookshop.dto.response;

/**
 * DTO for login response with JWT token
 */
public class LoginResponseDto {
    
    private String token;
    private String type = "Bearer";
    private int userId;
    private String email;
    private String name;
    private String role;

    // Constructors
    public LoginResponseDto() {
    }

    public LoginResponseDto(String token, int userId, String email, String name, String role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "type='" + type + '\'' +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
