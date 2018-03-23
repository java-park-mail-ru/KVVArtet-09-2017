package project.gamemechanics.services;

import org.springframework.stereotype.Service;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.items.containers.StorageBag;
import project.gamemechanics.services.databaseModels.BagDatabaseModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("unused")
@Service
public interface BagDAO {

    @NotNull
    StorageBag.FilledBagModel getSerializeBagById(@NotNull Integer id);

    @NotNull
    Integer setFilledBagWithItems(@NotNull BagDatabaseModel newBag, @NotNull List<EquipableItem> contents);

    @NotNull
    Integer setFilledBag(@NotNull BagDatabaseModel newBag);

    void addItemsArrayToBag(@NotNull Integer bagId, @NotNull Integer[] itemsIds);

    void deleteItemFromBag(@NotNull Integer bagId, @NotNull Integer itemId);
}
