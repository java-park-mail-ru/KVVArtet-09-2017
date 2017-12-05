package project.gamemechanics.resources.pcg;

import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;

import javax.validation.constraints.NotNull;

@SuppressWarnings("InterfaceNeverImplemented")
public interface PcgContentFactory {
    EquipableItem makeItem(@NotNull ItemBlueprint blueprint);
    EquipableItem makeItem(@NotNull Integer level);
}
