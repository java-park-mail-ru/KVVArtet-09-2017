package project.gamemechanics.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;

import java.io.IOException;
import java.sql.ResultSet;

@Service
@SuppressWarnings("unused")
public class ItemService implements ItemDAO {

    private Logger logger = Logger.getLogger(ItemService.class);
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    private PcgFactory pcgFactory;

    public ItemService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public EquipableItem getItemById(Integer id) {
        final String sql = "SELECT blueprint_json_model FROM public.item WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (ResultSet rs, int rwNumber) -> {
            ItemBlueprint itemBlueprint = null;
            try {
                itemBlueprint = mapper.readValue(rs.getString("blueprint_json_model"), ItemBlueprint.class);
            } catch (IOException e) {
                //CHECKSTYLE:OFF
                //TODO: Logger
                //CHECKSTYLE:ON
            }
            return pcgFactory.getItemsFactory().makeItem(itemBlueprint);
        });
    }

    @Override
    public EquipableItem setItem(Integer id, ItemBlueprint itemBlueprint) {
        return null;
    }

    public void bindPcgFactory(PcgFactory factory) {
        this.pcgFactory = factory;
    }
}
