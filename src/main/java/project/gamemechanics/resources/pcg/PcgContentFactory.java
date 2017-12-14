package project.gamemechanics.resources.pcg;

import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;
import project.gamemechanics.resources.pcg.items.ItemsFactory;
import project.gamemechanics.resources.pcg.npcs.NpcBlueprint;
import project.gamemechanics.resources.pcg.npcs.NpcsFactory;

import javax.validation.constraints.NotNull;

public interface PcgContentFactory {
    EquipableItem makeItem(@NotNull ItemBlueprint blueprint);

    EquipableItem makeItem(@NotNull Integer level);

    AliveEntity makeNpc(@NotNull NpcBlueprint blueprint);

    AliveEntity makeNpc(@NotNull Integer level);

    ItemsFactory getItemsFactory();

    NpcsFactory getNpcsFactory();

    void reset();
}
