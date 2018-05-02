package project.gamemechanics.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import project.gamemechanics.services.interfaces.CharacterListDAO;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.util.List;

@SuppressWarnings("unused")
@Service
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
        final String sql = "UPDATE public.character_list SET characters_ids = array_append(characters_ids, ?) WHERE id = ?";
        jdbcTemplate.update(connection -> {
            final PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, characterId);
            pst.setInt(2, id);
            return pst;
        });
    }

    @Override
    public Integer createDefaultEmptyCharacterList() {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO public.character_list DEFAULT VALUES returning id";
        jdbcTemplate.update(connection -> connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS), keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void deleteCharacterFromCharacterList(@NotNull Integer id, @NotNull Integer characterId) {
        final String sql = "UPDATE public.character_list SET characters_ids = array_remove(characters_ids, ?) WHERE id = ?";
        jdbcTemplate.update(connection -> {
            final PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, characterId);
            pst.setInt(2, id);
            return pst;
        });
    }
}
