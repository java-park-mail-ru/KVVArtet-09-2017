package project.gamemechanics.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.postgresql.util.PGobject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;
import project.gamemechanics.world.config.ResourcesConfig;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Service
@SuppressWarnings("unused")
public class ItemService implements ItemDAO {

    private final Logger logger = Logger.getLogger(ItemService.class);
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    private PcgContentFactory pcgFactory;

    public ItemService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        final AssetProvider assetProvider = new AssetProviderImpl(
                ResourcesConfig.getAssetHoldersFileNames());
        pcgFactory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
                ResourcesConfig.getNpcPartsFilename(), assetProvider);
    }

    @Override
    public EquipableItem getItemById(Integer id) {
        final String sql = "SELECT blueprint_json_model FROM public.item WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (ResultSet rs, int rwNumber) -> {
            ItemBlueprint itemBlueprint = null;
            try {
                itemBlueprint = mapper.readValue(rs.getString("blueprint_json_model"), ItemBlueprint.class);
            } catch (IOException e) {
                logger.trace(e);
            }
            try {
                System.out.println(mapper.writeValueAsString(pcgFactory.makeItem(itemBlueprint)));
            } catch (JsonProcessingException e) {
                logger.trace(e);
            }
            return pcgFactory.makeItem(itemBlueprint);
        });
    }

    @Override
    public Integer setItem(ItemBlueprint itemBlueprint) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT into public.item(id, blueprint_json_model)" + "values(?, ?)" + "returning id";
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                try {
                    PGobject jsonObject = new PGobject();
                    jsonObject.setType("json");
                    jsonObject.setValue(mapper.writeValueAsString(itemBlueprint));
                    preparedStatement.setObject(1, itemBlueprint.getProperties().get(PropertyCategories.PC_ITEM_ID).getProperty());
                    preparedStatement.setObject(2, jsonObject);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    logger.trace(e);
                }
                return preparedStatement;
            }, keyHolder);
        } catch (DataAccessException exception) {
            logger.trace(exception);
            return Constants.UNDEFINED_ID;
        }
        return keyHolder.getKey().intValue();

    }

    public void bindPcgFactory(PcgFactory factory) {
        this.pcgFactory = factory;
    }
}
