package gamemechanics.interfaces;

public interface EquipableItem extends Levelable, AffectorProvider, PropertyProvider {
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
