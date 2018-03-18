package project.gamemechanics.services;

import org.springframework.stereotype.Service;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;

@Service
public interface ItemDAO {
    EquipableItem getItemById(Integer id);

    Integer setItem(ItemBlueprint itemBlueprint);
}
