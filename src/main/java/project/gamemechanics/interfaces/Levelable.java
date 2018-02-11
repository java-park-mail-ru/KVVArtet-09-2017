package project.gamemechanics.interfaces;

import javax.validation.constraints.NotNull;

/**
 * Interface for entities that have level property.
 * Allows to ask for entity's current level and implements some
 * level-up logic. Extends {@link GameEntity} interface
 */
@SuppressWarnings("unused")
public interface Levelable extends GameEntity {
    /**
     * get an entity's current level.
     *
     * @return entity's current level
     */
    @NotNull Integer getLevel();

    /**
     * method for level-up logic.
     * can be used both for in-game level-up (as in {@link project.gamemechanics.aliveentities.UserCharacter} class)
     * or to scale entity's properties on creation (as for {@link project.gamemechanics.items.IngameItem} class)
     */
    default void levelUp() {
    }
}
