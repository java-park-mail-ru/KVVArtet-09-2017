package gamemechanics.interfaces;

import gamemechanics.globals.ItemRarity;

public interface EquipableItem extends Levelable, AffectorProvider {
    Integer getPrice();
    ItemRarity getRarity();

    default Integer getDamage() {
        return 0;
    }

    default Integer getDefense() {
        return 0;
    }

    Integer getKind();

    default Boolean isWeapon() {
        return false;
    }

    default Boolean isArmour() {
        return false;
    }

    default Boolean isTrinket() {
        return false;
    }
}
