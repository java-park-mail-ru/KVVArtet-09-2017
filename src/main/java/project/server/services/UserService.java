package project.server.services;

import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.server.dao.UserDao;
import project.server.mappers.UserMapper;
import project.server.models.User;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.util.List;

@SuppressWarnings({"ConstantConditions", "Duplicates"})
@Service
public class UserService implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder encoder;

    public UserService(@NotNull JdbcTemplate jdbcTemplate,
                       @NotNull PasswordEncoder encoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.encoder = encoder;
    }

    @Override
    public @Nullable User getUserById(@NotNull Integer id) {
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
    public @Nullable User getUserByUsernameOrEmail(@NotNull String usernameOrEmail) {
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
    public @NotNull Integer getUserIdByUsername(@NotNull String username) {
        final String sql = "SELECT id FROM public.user WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

    @Override
    public @NotNull Integer getUserIdByEmail(@NotNull String email) {
        final String sql = "SELECT id FROM public.user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email);
    }

    @Override
    public @NotNull Integer getUserIdByUsernameOrEmail(@NotNull String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            return getUserIdByEmail(usernameOrEmail);
        }
        return getUserIdByUsername(usernameOrEmail);
    }

    @Override
    public @NotNull User setUser(@NotNull User newUser) {
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
        return new User(keyHolder.getKey().intValue(),
                newUser.getUsername(), newUser.getEmail(), encryptedPassword);
    }

    @Override
    public @NotNull User updateUserPassword(@NotNull User currentUser, @NotNull String password) {
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
    public @NotNull User updateUserLogin(@NotNull User currentUser, @NotNull String username) {
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
    public boolean isUsernameExists(@NotNull String username) {
        final String sql = "SELECT count(*) from public.user WHERE username = ?";
        final Integer check = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return check != null && check > 0;
    }

    @Override
    public boolean isEmailExists(@NotNull String email) {
        final String sql = "SELECT count(*) from public.user WHERE email = ?";
        final Integer check = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return check != null && check > 0;
    }

    @Override
    public boolean isIdExists(@NotNull Integer id) {
        final String sql = "SELECT count(*) from public.user WHERE id = ?";
        final Integer check = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return check != null && check > 0;
    }

    @Override
    public boolean isExist(@NotNull String usernameOrEmail) {
        final String sql = "SELECT count(*) from public.user WHERE username = ? OR email = ?";
        final Integer check = jdbcTemplate.queryForObject(sql, Integer.class, usernameOrEmail, usernameOrEmail);
        return check != null && check > 0;
    }

    @Override
    public void deleteUser(@NotNull Integer id) {
        final String sql = "DELETE FROM public.user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public @NotNull List<User> getAllUsers() {
        final String sql = "SELECT * FROM public.user";
        return jdbcTemplate.query(sql, new UserMapper());
    }
}
