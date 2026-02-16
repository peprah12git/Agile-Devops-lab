package bookshop.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import bookshop.dao.daoInterface.UserDao;
import bookshop.models.User;
@Repository
public class Userdaoimp implements UserDao {
    @Autowired
    private DataSource dataSource;
    public Userdaoimp(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public User save(User user) {
        String sql = """
            INSERT INTO users
            (name, email, course, age)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, user.getName());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getCourse());
            if (user.getAge() != null) {
                pstm.setInt(4, user.getAge());
            } else {
                pstm.setNull(4, Types.INTEGER);
            }

            pstm.executeUpdate();

            // Get generated ID
            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        }

        return user;
    }

    @Override
    public List<User> findAll() {

        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users", e);
        }

        return users;
    }


    @Override
    public Optional<User> findById(int id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by id", e);
        }

        return Optional.empty();
    }


    @Override
    public Optional<User> findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, email);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by email", e);
        }

        return Optional.empty();
    }

    @Override
    public User update(User user) {

        String sql = """
            UPDATE users
            SET name = ?,
                email = ?,
                course = ?,
                age = ?
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, user.getName());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getCourse());
            if (user.getAge() != null) {
                pstm.setInt(4, user.getAge());
            } else {
                pstm.setNull(4, Types.INTEGER);
            }
            pstm.setInt(5, user.getId());

            int rowsUpdated = pstm.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("User not found with id " + user.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }

        return user;
    }

    @Override
    public void deleteById(int id) {

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, id);

            int rowsDeleted = pstm.executeUpdate();

            if (rowsDeleted == 0) {
                throw new RuntimeException("User not found with id " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }


    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setCourse(rs.getString("course"));
        user.setAge(rs.getInt("age"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }

}
