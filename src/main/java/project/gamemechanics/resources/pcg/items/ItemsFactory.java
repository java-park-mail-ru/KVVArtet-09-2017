package project.gamemechanics.resources.pcg.items;

import project.gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;

public interface ItemsFactory {
    EquipableItem makeItem(@NotNull ItemBlueprint blueprint);
}
