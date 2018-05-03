package project.gamemechanics.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.postgresql.util.PGobject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import project.gamemechanics.aliveentities.AbstractAliveEntity;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.services.interfaces.UserCharacterDAO;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Service
public class UserCharacterService implements UserCharacterDAO {

    private final Logger logger = Logger.getLogger(BagService.class);
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public UserCharacterService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public AbstractAliveEntity.UserCharacterModel getUserCharacterById(@NotNull Integer id) {
        final String sql = "SELECT * FROM public.character WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (ResultSet rs, int rwNumber) -> {
           AbstractAliveEntity.UserCharacterModel userCharacterModel = null;
            try {
                userCharacterModel = mapper.readValue(rs.getString("character_json_model"), AbstractAliveEntity.UserCharacterModel.class);
            } catch (IOException e) {
                logger.trace(e);
            }
            return userCharacterModel;
        });
    }

    @Override
    @NotNull
    @SuppressWarnings("Duplicates")
    public Integer setUserCharacter(AbstractAliveEntity.UserCharacterModel newUserCharacterModel) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT into public.character(character_json_model)" + "values(?) RETURNING id";
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                try {
                    PGobject jsonObject = new PGobject();
                    jsonObject.setType("json");
                    jsonObject.setValue(mapper.writeValueAsString(newUserCharacterModel));
                    preparedStatement.setObject(1, jsonObject);
                } catch (JsonProcessingException e) {
                    logger.trace(e);
                }
                return preparedStatement;
            }, keyHolder);
        } catch (DataAccessException e) {
            logger.trace(e);
            return Constants.UNDEFINED_ID;
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    /*TODO updateUserChar method*/
    public void updateUserCharacter(Integer id, AbstractAliveEntity.UserCharacterModel newUserCharacterModel) {

    }

    @Override
    public void deleteUserCharacter(Integer id) {
        final String sql = "DELETE FROM public.character WHERE id = ?";
        jdbcTemplate.update(con -> {
            final PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            return pst;
        });
    }
}
