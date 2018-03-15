package project.gamemechanics.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.items.containers.StorageBag;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class BagService implements BagDAO {

   private final JdbcTemplate jdbcTemplate;
   private final ItemService itemService;

    public BagService(JdbcTemplate jdbcTemplate, ItemService itemService) {
        this.jdbcTemplate = jdbcTemplate;
        this.itemService = itemService;
    }

    @Override
    public StorageBag.FilledBagModel getSerializeBagById(@NotNull Integer id) {
        final String sql = "SELECT * FROM public.bag WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (ResultSet rs, int rwNumber) -> {
            List<EquipableItem> equipableItems = new ArrayList<>();
            List<Integer> itemsIds =  (List<Integer>) rs.getArray("items_ids");
            for (Integer item : itemsIds) {
                equipableItems.add(itemService.getItemById(item));
            }
            return new StorageBag.FilledBagModel(rs.getInt("id"),
                    rs.getString("name"), rs.getString("description"), equipableItems);
        });
    }

    @Override
    public void setFilledBag(@NotNull StorageBag.FilledBagModel newBag) {
        final String sql = "INSERT into public.bag(name, description, items_ids)" + "values(?,?,?)";
        jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, newBag.name);
                preparedStatement.setObject(2, newBag.description);
                //CHECKSTYLE:OFF
                preparedStatement.setObject(3, newBag.contents);
                //CHECKSTYLE:ON
                return  preparedStatement;
        });
    }
}
