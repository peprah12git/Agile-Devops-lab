package bookshop.dao.daoInterface;

import bookshop.models.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    // Create
    User save(User user);

    // Read
    List<User> findAll();

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    // Update
    User update(User user);

    // Delete
    void deleteById(int id);
}
