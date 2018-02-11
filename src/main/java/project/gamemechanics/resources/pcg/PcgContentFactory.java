package project.gamemechanics.resources.pcg;

import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;
import project.gamemechanics.resources.pcg.items.ItemsFactory;
import project.gamemechanics.resources.pcg.npcs.NpcBlueprint;
import project.gamemechanics.resources.pcg.npcs.NpcsFactory;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public interface PcgContentFactory {
    @NotNull EquipableItem makeItem(@NotNull ItemBlueprint blueprint);

    @NotNull EquipableItem makeItem(@NotNull Integer level);

    @NotNull AliveEntity makeNpc(@NotNull NpcBlueprint blueprint);

    @NotNull AliveEntity makeNpc(@NotNull Integer level);

    @NotNull ItemsFactory getItemsFactory();

    @NotNull NpcsFactory getNpcsFactory();

    void reset();
}
