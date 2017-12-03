package gamemechanics.resources.pcg;

import gamemechanics.interfaces.EquipableItem;
import gamemechanics.resources.pcg.items.ItemBlueprint;

import javax.validation.constraints.NotNull;

public interface PcgContentFactory {
    EquipableItem makeItem(@NotNull ItemBlueprint blueprint);
    EquipableItem makeItem(@NotNull Integer level);
}
