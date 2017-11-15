package gamemechanics.interfaces;

/**
 * interface for various spells and combat abilities that may be used in the battle
 * abilities're flyweights that actually exist in single instance per one concrete
 * Abilities' action and properties're scaled to the caster's level on every use.
 * @see GameEntity
 * @see PropertyProvider
 * @see AffectorProvider
 */
public interface Ability extends GameEntity, PropertyProvider, AffectorProvider {
    /**
     * execute an ability
     * @param sender ability's caster
     * @param target map node to apply ability on (since we have some AoE abilities
     *               it's easier to throw it on the node first and get the node's inhabitant
     *               if needed
     * @return true if execution was successful or false otherwise
     */
    Boolean execute(AliveEntity sender, MapNode target);
}
