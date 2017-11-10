package gamemechanics.interfaces;

/**
 * interface for items that may be stored in {@link Bag}s,
 * worn by {@link gamemechanics.aliveentities.UserCharacter}s or looted from NPC monsters
 * @see Levelable
 * @see AffectorProvider
 * @see PropertyProvider
 * @see Bag
 * @see gamemechanics.aliveentities.UserCharacter
 * @see gamemechanics.aliveentities.npcs.NonPlayerCharacter
 */
public interface EquipableItem extends Levelable, AffectorProvider, PropertyProvider {
    /**
     * check if the item is some weapon
     * @return true if it is or false otherwise
     */
    default Boolean isWeapon() {
        return false;
    }

    /**
     * check if the item is some armour
     * @return true if it is or false otherwise
     */
    default Boolean isArmour() {
        return false;
    }

    /**
     * check if the item is some trinket
     * @return true if it is or false otherwise
     */
    default Boolean isTrinket() {
        return false;
    }
}
