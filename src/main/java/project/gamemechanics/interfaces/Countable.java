package project.gamemechanics.interfaces;

import javax.validation.constraints.NotNull;

/**
 * The main interface for the most of game mechanic's entities.
 * Allows to get an entity's ID.
 */
@SuppressWarnings("unused")
public interface Countable {
    /**
     * get entity's ID.
     *
     * @return entity's ID
     */
    @NotNull Integer getID();

    /**
     * get number of the entities instantiated since the last project.server restart.
     *
     * @return number of entities of an implementing class instantiated
     */
    @NotNull Integer getInstancesCount();
}
