package gamemechanics.items.loot;

import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PendingLootPool {
    List<EquipableItem> getLootPool(@NotNull AliveEntity owner);

    void clearLootPool(@NotNull AliveEntity owner);

    void createLootPool(@NotNull AliveEntity owner);

    void removeLootPool(@NotNull AliveEntity owner);

    void pollItemFromPool(@NotNull AliveEntity owner, @NotNull Integer itemIndex);

    void rejectItemFromPool(@NotNull AliveEntity owner, @NotNull Integer itemIndex);

    void offerItemToPool(@NotNull AliveEntity owner, @NotNull EquipableItem item);
}
