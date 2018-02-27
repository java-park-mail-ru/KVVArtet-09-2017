package project.gamemechanics.services;

import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;

public interface ItemDAO {
    EquipableItem getItemById(Integer id);

    Integer setItem(ItemBlueprint itemBlueprint);
}
