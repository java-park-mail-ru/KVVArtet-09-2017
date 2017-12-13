package project.gamemechanics.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.items.IngameItem;

/**
 * interface for items that may be stored in {@link Bag}s,
 * worn by {@link project.gamemechanics.aliveentities.UserCharacter}s or looted from NPC monsters
 *
 * @see Levelable
 * @see AffectorProvider
 * @see PropertyProvider
 * @see Bag
 * @see project.gamemechanics.aliveentities.UserCharacter
 * @see project.gamemechanics.aliveentities.npcs.NonPlayerCharacter
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(IngameItem.class),
})
public interface EquipableItem extends Levelable, AffectorProvider, PropertyProvider {
    /**
     * check if the item is some weapon
     *
     * @return true if it is or false otherwise
     */
    @JsonIgnore
    default Boolean isWeapon() {
        return false;
    }

    /**
     * check if the item is some armour
     *
     * @return true if it is or false otherwise
     */
    @JsonIgnore
    default Boolean isArmour() {
        return false;
    }

    /**
     * check if the item is some trinket
     *
     * @return true if it is or false otherwise
     */
    @JsonIgnore
    default Boolean isTrinket() {
        return false;
    }
}
