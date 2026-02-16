package bookshop.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data Transfer Object for User Response
 * Used to return user data to the API layer without sensitive information
 */
@Data
@NoArgsConstructor
public class UserResponsedto {
    private int userId;
    private String name;
    private String email;

}
