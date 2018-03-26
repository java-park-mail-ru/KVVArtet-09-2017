package project.gamemechanics.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import project.gamemechanics.services.interfaces.CharacterListDAO;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Types.NULL;

@SuppressWarnings("unused")
public class CharacterListService implements CharacterListDAO {

    private final JdbcTemplate jdbcTemplate;

    public CharacterListService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Nullable
    public List<Integer> getCharacters(@NotNull Integer id) {
        String sql = "SELECT characters_ids FROM public.character_list WHERE id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{id}, Integer.class);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void setCharacterInCharacterList(@NotNull Integer id, @NotNull Integer characterId) {
        String sql = "UPDATE public.character_list SET characters_ids = array_append(characters_ids, ?) WHERE id = ?";
        jdbcTemplate.update(connection -> {
            final PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, characterId);
            pst.setInt(2, id);
            return pst;
        });
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void createDefaultEmptyCharacterList(@NotNull Integer id) {
        String sql = "INSERT INTO public.character_list VALUES(?,?)";
        jdbcTemplate.update(connection -> {
            final PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setInt(2, NULL);
            return pst;
        });
    }
}
