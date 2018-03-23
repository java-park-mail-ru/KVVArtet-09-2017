package project.gamemechanics.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.items.containers.StorageBag;
import project.gamemechanics.services.databaseModels.BagDatabaseModel;

import javax.validation.constraints.NotNull;
import java.sql.Array;
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
            Integer[] itemsIds = (Integer[]) rs.getArray("items_ids").getArray();
            for (Integer item : itemsIds) {
                equipableItems.add(itemService.getItemById(item));
            }
            return new StorageBag.FilledBagModel(rs.getInt("id"),
                    rs.getString("name"), rs.getString("description"), equipableItems);
        });
    }

    @Override
    public Integer setFilledBagWithItems(@NotNull BagDatabaseModel newBag, @NotNull List<EquipableItem> contents) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT into public.bag(bag_json_model)" + "values(?) RETURNING id";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setObject(1, newBag.getName());
            return  preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    @NotNull
    public Integer setFilledBag(@NotNull BagDatabaseModel newBag) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT into public.bag(bag_json_model)" + "values(?) RETURNING id";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, newBag.getName());
            return  preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void addItemsArrayToBag(@NotNull Integer bagId, @NotNull Integer[] itemsIds) {
        final String sql = "UPDATE public.bag SET items_ids = array_cat(items_ids, ?) WHERE id = ?";
        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Array items_array = connection.createArrayOf("INTEGER", itemsIds);
            preparedStatement.setArray(1, items_array);
            preparedStatement.setObject(2, bagId);
            return preparedStatement;
        });
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void deleteItemFromBag(@NotNull Integer bagId, @NotNull Integer itemId){
        final String sql = "UPDATE public.bag SET items_ids = array_remove(items_ids, ?) WHERE id = ?";
        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, itemId);
            preparedStatement.setObject(2, bagId);
            return preparedStatement;
        });
    }
}
