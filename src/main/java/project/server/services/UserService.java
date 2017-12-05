package project.server.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.server.dao.UserDao;
import project.server.mappers.UserMapper;
import project.server.models.User;

import java.sql.PreparedStatement;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "Duplicates"})
@Service
public class UserService implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder encoder;

    public UserService(JdbcTemplate jdbcTemplate, PasswordEncoder encoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.encoder = encoder;
    }

    @Override
    public User getUserById(Integer id) {
        final String sql = "SELECT * FROM public.user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rwNumber) -> {
            final User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    @Override
    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        final String sql = "SELECT * FROM public.user WHERE username = ? OR email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{usernameOrEmail, usernameOrEmail}, (rs, rwNumber) -> {
            final User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    @Override
    public Integer getUserIdByUsername(String username) {
        final String sql = "SELECT id FROM public.user WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

    @Override
    public Integer getUserIdByEmail(String email) {
        final String sql = "SELECT id FROM public.user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email);
    }

    @Override
    public Integer getUserIdByUsernameOrEmail(String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            return getUserIdByEmail(usernameOrEmail);
        }
        return getUserIdByUsername(usernameOrEmail);
    }

    @Override
    public User setUser(User newUser) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        //noinspection QuestionableName
        final Integer three = 3;
        final String encryptedPassword = encoder.encode(newUser.getPassword());
        jdbcTemplate.update(con -> {
            final PreparedStatement pst = con.prepareStatement(
                    "insert into public.user(username, email, password)" + " values(?,?,?)" + " returning id",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, newUser.getUsername());
            pst.setString(2, newUser.getEmail());
            pst.setString(three, encryptedPassword);
            return pst;
        }, keyHolder);
        return new User(keyHolder.getKey().intValue(), newUser.getUsername(), newUser.getEmail(), encryptedPassword);
    }

    @Override
    public User updateUserPassword(User currentUser, String password) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String encryptedPassword = encoder.encode(password);
        jdbcTemplate.update(con -> {
            final PreparedStatement pst = con.prepareStatement(
                    "UPDATE public.user SET password = ? WHERE id = ?" + " returning id",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, encryptedPassword);
            pst.setInt(2, currentUser.getId());
            return pst;
        }, keyHolder);
        return new User(keyHolder.getKey().intValue(), currentUser.getUsername(),
                currentUser.getEmail(), encryptedPassword);
    }

    @Override
    public User updateUserLogin(User currentUser, String username) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement pst = con.prepareStatement(
                    "UPDATE public.user SET username = ? WHERE id = ?" + " returning id",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, username);
            pst.setInt(2, currentUser.getId());
            return pst;
        }, keyHolder);
        return new User(keyHolder.getKey().intValue(), username,
                currentUser.getEmail(), currentUser.getPassword());
    }

    @Override
    public boolean isUsernameExists(String username) {
        final String sql = "SELECT count(*) from public.user WHERE username = ?";
        final Integer check = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return check != null && check > 0;
    }

    @Override
    public boolean isEmailExists(String email) {
        final String sql = "SELECT count(*) from public.user WHERE email = ?";
        final Integer check = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return check != null && check > 0;
    }

    @Override
    public boolean isIdExists(Integer id) {
        final String sql = "SELECT count(*) from public.user WHERE id = ?";
        final Integer check = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return check != null && check > 0;
    }

    @Override
    public boolean isExist(String usernameOrEmail) {
        final String sql = "SELECT count(*) from public.user WHERE username = ? OR email = ?";
        final Integer check = jdbcTemplate.queryForObject(sql, Integer.class, usernameOrEmail, usernameOrEmail);
        return check != null && check > 0;
    }

    @Override
    public void deleteUser(Integer id) {
        final String sql = "DELETE FROM public.user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<User> getAllUsers() {
        final String sql = "SELECT * FROM public.user";
        return jdbcTemplate.query(sql, new UserMapper());
    }
}
