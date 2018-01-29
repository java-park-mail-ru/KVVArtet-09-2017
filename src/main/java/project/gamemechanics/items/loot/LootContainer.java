package project.gamemechanics.items.loot;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(IngameLootContainer.class),
})
public interface LootContainer {
    @NotNull List<EquipableItem> getItemsList();

    @Nullable EquipableItem getItem(@NotNull Integer itemIndex);

    void addItem(@NotNull EquipableItem item);

    void removeItem(@NotNull Integer itemIndex);

    @NotNull Integer getCashReward();

    void changeCash(@NotNull Integer amount);

    @NotNull Integer getExpReward();

    void changeExp(@NotNull Integer amount);
}
