package server.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.dao.UserDao;
import server.models.User;

import java.sql.PreparedStatement;
import java.util.*;

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
        String sql = "SELECT * FROM public.user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rwNumber) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    @Override
    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        String sql = "SELECT * FROM public.user WHERE username = ? OR email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{usernameOrEmail, usernameOrEmail}, (rs, rwNumber) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    @Override
    public Integer getUserIdByUsername(String username) {
        String sql = "SELECT id FROM public.user WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

    @Override
    public Integer getUserIdByEmail(String email) {
        String sql = "SELECT id FROM public.user WHERE email = ?";
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
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final Integer three = 3;
        String encryptedPassword = encoder.encode(newUser.getPassword());
        jdbcTemplate.update(con -> {
            PreparedStatement pst = con.prepareStatement(
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
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String encryptedPassword = encoder.encode(password);
        jdbcTemplate.update(con -> {
            PreparedStatement pst = con.prepareStatement(
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
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement pst = con.prepareStatement(
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
        String sql = "SELECT count(*) from public.user WHERE username = ?";
        Integer check = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return check != null && check > 0;
    }

    @Override
    public boolean isEmailExists(String email) {
        String sql = "SELECT count(*) from public.user WHERE email = ?";
        Integer check = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return check != null && check > 0;
    }

    @Override
    public boolean isIdExists(Integer id) {
        String sql = "SELECT count(*) from public.user WHERE id = ?";
        Integer check = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return check != null && check > 0;
    }

    @Override
    public boolean isExist(String usernameOrEmail) {
        String sql = "SELECT count(*) from public.user WHERE username = ? OR email = ?";
        Integer check = jdbcTemplate.queryForObject(sql, Integer.class, usernameOrEmail, usernameOrEmail);
        return check != null && check > 0;
    }

    @Override
    public void deleteUser(Integer id) {
        String sql = "DELETE FROM public.user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM public.user";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        List<User> result = new ArrayList<>();
        for (Map<String, Object> row:rows) {
            User user = new User();
            user.setId((Integer) row.get("id"));
            user.setUsername((String) row.get("username"));
            user.setEmail((String) row.get("email"));
            user.setPassword((String) row.get("password"));
            result.add(user);
        }

        return result;
    }
}
