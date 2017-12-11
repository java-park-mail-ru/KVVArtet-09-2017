package project.gamemechanics.items.loot;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(IngameLootContainer.class),
})
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
