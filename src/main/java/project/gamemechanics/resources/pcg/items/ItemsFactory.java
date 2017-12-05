package gamemechanics.resources.pcg.items;

import gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;

public interface ItemsFactory {
    EquipableItem makeItem(@NotNull ItemBlueprint blueprint);
}
