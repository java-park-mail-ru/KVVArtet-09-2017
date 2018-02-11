package project.gamemechanics.items.loot;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"RedundantSuppression", "unused"})
public class IngameLootContainer implements LootContainer {
    private final List<EquipableItem> lootList = new ArrayList<>();
    private Integer goldReward = 0;
    private Integer expReward = 0;

    public IngameLootContainer() {
    }

    public IngameLootContainer(@NotNull List<EquipableItem> lootList, @NotNull Integer goldReward,
                               @NotNull Integer expReward) {
        this.lootList.addAll(lootList);
        this.goldReward += goldReward;
        this.expReward += expReward;
    }

    @Override
    @JsonProperty("items")
    public @NotNull List<EquipableItem> getItemsList() {
        return lootList;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @Nullable EquipableItem getItem(@NotNull Integer itemIndex) {
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
    @JsonProperty("cash")
    public @NotNull Integer getCashReward() {
        return goldReward;
    }

    @Override
    public void changeCash(@NotNull Integer amount) {
        goldReward += amount;
    }

    @Override
    @JsonProperty("experience")
    public @NotNull Integer getExpReward() {
        return expReward;
    }

    @Override
    public void changeExp(@NotNull Integer amount) {
        expReward += amount;
    }
}
