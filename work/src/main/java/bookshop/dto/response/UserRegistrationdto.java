package bookshop.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for User Registration
 * Simplified for basic bookshop without authentication
 */
public class UserRegistrationdto {

    @NotBlank(message = "Name is Required")
    @Size(min = 4, max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private String course;
    private Integer age;

    // Constructors
    public UserRegistrationdto() {
    }

    public UserRegistrationdto(String name, String email, String course, Integer age) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.age = age;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "UserRegistrationdto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", age=" + age +
                '}';
    }
}

