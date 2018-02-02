package project.gamemechanics.interfaces;

import javax.validation.constraints.NotNull;

/**
 * An interface for entities that have name and description.
 * Extends {@link Countable} interface
 */
public interface GameEntity extends Countable {
    /**
     * get entity's name.
     *
     * @return entity's name
     */
    @NotNull String getName();

    /**
     * get entity's description.
     *
     * @return entity's description
     */
    @NotNull String getDescription();
}
