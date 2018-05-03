package project.server.dao;

import org.jetbrains.annotations.Nullable;
import project.server.models.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserDao {

    @Nullable User getUserById(@NotNull Integer id);

    @NotNull Integer getCharacaterListIdByUserId(@NotNull Integer id);

    @Nullable User getUserByUsernameOrEmail(@NotNull String usernameOrEmail);

    @NotNull Integer getUserIdByUsername(@NotNull String username);

    @NotNull Integer getUserIdByEmail(@NotNull String email);

    @NotNull Integer getUserIdByUsernameOrEmail(@NotNull String usernameOrEmail);

    @NotNull User setUser(@NotNull User newUser);

    @NotNull User updateUserPassword(@NotNull User user, @NotNull String password);

    @NotNull User updateUserLogin(@NotNull User user, @NotNull String username);

    boolean isUsernameExists(@NotNull String username);

    boolean isEmailExists(@NotNull String email);

    boolean isIdExists(@NotNull Integer id);

    boolean isExist(@NotNull String usernameOrEmail);

    void deleteUser(@NotNull Integer id);

    @NotNull List<User> getAllUsers();

}
