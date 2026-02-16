package bookshop.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data Transfer Object for User Registration
 * Used to receive registration data from the API layer
 */
@Data
@NoArgsConstructor
public class UserRegistrationdto {

    @NotBlank( message = "Name is Required")
    @Size(min = 4, max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
@NotBlank(message = "phone contact required")
@NotNull(message= " Cannot be empty")
    private String phone;

    private String address;
    private String role;
}
