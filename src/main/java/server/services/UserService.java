package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import server.dao.UserDao;
import server.models.User;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.*;

@Service
public class UserService implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    
    public UserService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
    public User getUserById(Integer id) {
        String sql = "SELECT * FROM public.user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rwNumber) -> {
                User user = new User();
            user.setId(rs.getInt("id"));
            user.setLogin(rs.getString("username"));
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
        Integer id = getUserIdByUsername(usernameOrEmail);
        if (id > 0) {
            return id;
        }

        id = getUserIdByEmail(usernameOrEmail);
        if (id > 0) {
            return id;
        }

        return 0;
    }

    @Override
    public void setUser(User newUser) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement pst = con.prepareStatement(
                    "insert into public.user(username, email, password)" + " values(?,?,?)" + " returning id",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, newUser.getLogin());
            pst.setString(2, newUser.getEmail());
            pst.setString(3, newUser.getPassword());
            return pst;
        }, keyHolder);
    }

    @Override
    public void updateUserPassword(Integer id, String password) {
        String sql = "UPDATE public.user SET password = ? WHERE id = ?";
        jdbcTemplate.update(sql, id, password);
    }

    @Override
    public void updateUserLogin(Integer id, String username) {
        String sql = "UPDATE public.user SET username = ? WHERE id = ?";
        jdbcTemplate.update(sql, id, username);
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
        return isUsernameExists(usernameOrEmail) || isEmailExists(usernameOrEmail);
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
            user.setLogin((String) row.get("username"));
            user.setEmail((String) row.get("email"));
            user.setPassword((String) row.get("password"));
            result.add(user);
        }

        return result;
    }
}
