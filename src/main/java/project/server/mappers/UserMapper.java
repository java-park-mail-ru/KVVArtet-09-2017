package project.server.mappers;

import org.springframework.jdbc.core.RowMapper;
import project.server.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @SuppressWarnings("NullableProblems")
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"),
                rs.getString("email"), rs.getString("password"));
    }
}
