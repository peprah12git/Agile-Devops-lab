package bookshop.dto.response;

import java.sql.Timestamp;

/**
 * Data Transfer Object for User Response
 * Used to return user data to the API layer without sensitive information
 */
public class UserResponsedto {
    private int userId;
    private String name;
    private String email;
    private String course;
    private Integer age;
    private Timestamp createdAt;

    // Constructors
    public UserResponsedto() {
    }

    public UserResponsedto(int userId, String name, String email, String course, Integer age, Timestamp createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.course = course;
        this.age = age;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserResponsedto{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", age=" + age +
                ", createdAt=" + createdAt +
                '}';
    }
}
