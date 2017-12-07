package gamemechanics.items.containers;

import gamemechanics.globals.Constants;
import gamemechanics.interfaces.Bag;
import gamemechanics.interfaces.EquipableItem;
import gamemechanics.resources.pcg.items.ItemBlueprint;
import gamemechanics.resources.pcg.items.ItemsFactory;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MonsterLootBag implements Bag {
    private final Integer level;

    private final List<ItemBlueprint> itemPresets;
    private final ItemsFactory itemGenerator;

    public MonsterLootBag(@NotNull List<ItemBlueprint> itemPresets, @NotNull Integer level,
                          @NotNull ItemsFactory itemGenerator) {
        this.itemPresets = itemPresets;
        this.level = level;
        this.itemGenerator = itemGenerator;
    }

    @Override
    public Integer getID() {
        return Constants.UNDEFINED_ID;
    }

    @Override
    public Integer getInstancesCount() {
        return 0;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public Integer getSlotsCount() {
        return itemPresets.size();
    }

    @Override
    public Integer getFreeSlotsCount() {
        return 0;
    }

    @Override
    public Boolean swap(@NotNull Integer fromPos, @NotNull Integer toPos) {
        return false;
    }

    @Override
    public Boolean swap(@NotNull Integer fromPos, @NotNull Bag toBag, @NotNull Integer toPos) {
        return false;
    }

    @Override
    public Boolean addItem(@NotNull EquipableItem item) {
        return false;
    }

    @Override
    public Boolean addItem(@NotNull EquipableItem item, @NotNull Integer toPos) {
        return false;
    }

    @Override
    public void throwAway(@NotNull Integer fromPos, @NotNull Boolean isConfirmed) {
    }

    @Override
    public @Nullable EquipableItem getItem(@NotNull Integer itemIndex) {
        if (itemIndex < 0 || itemIndex >= itemPresets.size()) {
            return null;
        }
        return itemGenerator.makeItem(itemPresets.get(itemIndex));
    }
}
