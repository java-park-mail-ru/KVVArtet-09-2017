package gamemechanics.items.loot;

import gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class IngameLootContainer implements LootContainer {
    private final List<EquipableItem> lootList = new ArrayList<>();
    private Integer goldReward = 0;
    private Integer expReward = 0;

    public IngameLootContainer() {}

    public IngameLootContainer(@NotNull List<EquipableItem> lootList, @NotNull Integer goldReward,
                               @NotNull Integer expReward) {
        this.lootList.addAll(lootList);
        this.goldReward += goldReward;
        this.expReward += expReward;
    }

    @Override
    public List<EquipableItem> getItemsList() {
        return lootList;
    }

    @Override
    public EquipableItem getItem(@NotNull Integer itemIndex) {
        if (itemIndex < 0 || itemIndex >= lootList.size()) {
            return null;
        }
        return lootList.get(itemIndex);
    }

    @Override
    public void addItem(@NotNull EquipableItem item) {
        lootList.add(item);
    }

    @Override
    public void removeItem(@NotNull Integer itemIndex) {
        if (itemIndex < 0 || itemIndex >= lootList.size()) {
            return;
        }
        lootList.remove(itemIndex.intValue());
    }

    @Override
    public Integer getCashReward() {
        return goldReward;
    }

    @Override
    public void changeCash(@NotNull Integer amount) {
        goldReward += amount;
    }

    @Override
    public Integer getExpReward() {
        return expReward;
    }

    @Override
    public void changeExp(@NotNull Integer amount) {
        expReward += amount;
    }
}
