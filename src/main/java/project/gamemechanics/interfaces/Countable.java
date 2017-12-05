package project.gamemechanics.interfaces;

/**
 * The main interface for the most of game mechanic's entities.
 * Allows to get an entity's ID.
 */
public interface Countable {
    /**
     * get entity's ID.
     *
     * @return entity's ID
     */
    Integer getID();

    /**
     * get number of the entities instantiated since the last project.server restart.
     *
     * @return number of entities of an implementing class instantiated
     */
    Integer getInstancesCount();
}
