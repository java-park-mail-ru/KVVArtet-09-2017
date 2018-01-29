package project.gamemechanics.items.containers;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.Bag;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;
import project.gamemechanics.resources.pcg.items.ItemsFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Random;

public class MonsterLootBag implements Bag {
    private final List<ItemBlueprint> itemPresets;
    private final ItemsFactory itemGenerator;

    public MonsterLootBag(@NotNull List<ItemBlueprint> itemPresets, @NotNull Integer level,
                          @NotNull ItemsFactory itemGenerator) {
        this.itemPresets = itemPresets;

        final Random random = new Random(System.currentTimeMillis());
        for (ItemBlueprint itemPreset : itemPresets) {
            if (!itemPreset.getProperties().containsKey(PropertyCategories.PC_LEVEL)) {
                final Property property = new SingleValueProperty(level
                        + random.nextInt(Constants.LEVEL_RANGE_FOR_LOOT_DROPPING)
                        - Constants.LEVEL_RANGE_FOR_LOOT_DROPPING / 2);
                itemPreset.getProperties().put(PropertyCategories.PC_LEVEL, property);
            } else {
                if (itemPreset.getProperties().get(PropertyCategories.PC_LEVEL).getProperty()
                        < Constants.START_LEVEL) {
                    itemPreset.getProperties().get(PropertyCategories.PC_LEVEL).setSingleProperty(level
                            + random.nextInt(Constants.LEVEL_RANGE_FOR_LOOT_DROPPING)
                            - Constants.LEVEL_RANGE_FOR_LOOT_DROPPING / 2);
                }
            }
        }

        this.itemGenerator = itemGenerator;
    }

    @Override
    public @NotNull Integer getID() {
        return Constants.UNDEFINED_ID;
    }

    @Override
    public @NotNull Integer getInstancesCount() {
        return 0;
    }

    @Override
    public @NotNull String getName() {
        return "";
    }

    @Override
    public @NotNull String getDescription() {
        return "";
    }

    @Override
    public @NotNull Integer getSlotsCount() {
        return itemPresets.size();
    }

    @Override
    public @NotNull Integer getFreeSlotsCount() {
        return 0;
    }

    @Override
    public @NotNull Boolean swap(@NotNull Integer fromPos, @NotNull Integer toPos) {
        return false;
    }

    @Override
    public @NotNull Boolean swap(@NotNull Integer fromPos,
                                 @NotNull Bag toBag,
                                 @NotNull Integer toPos) {
        return false;
    }

    @Override
    public @NotNull Boolean addItem(@NotNull EquipableItem item) {
        return false;
    }

    @Override
    public @NotNull Boolean addItem(@NotNull EquipableItem item, @NotNull Integer toPos) {
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
        final Random random = new Random(System.currentTimeMillis());
        return random.nextInt(Constants.WIDE_PERCENTAGE_CAP_INT) <= itemPresets.get(itemIndex).getDropChance()
                ? itemGenerator.makeItem(itemPresets.get(itemIndex)) : null;
    }
}
