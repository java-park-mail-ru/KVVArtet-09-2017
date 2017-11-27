package gamemechanics.items.loot;

import gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface LootContainer {
    List<EquipableItem> getItemsList();
    EquipableItem getItem(@NotNull Integer itemIndex);
    void addItem(@NotNull EquipableItem item);
    void removeItem(@NotNull Integer itemIndex);

    Integer getCashReward();
    void changeCash(@NotNull Integer amount);

    Integer getExpReward();
    void changeExp(@NotNull Integer amount);
}
