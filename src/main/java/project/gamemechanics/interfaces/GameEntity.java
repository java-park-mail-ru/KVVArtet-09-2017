package project.gamemechanics.interfaces;

/**
 * An interface for entities that have name and description
 * Extends {@link Countable} interface
 */
public interface GameEntity extends Countable {
    /**
     * get entity's name
     *
     * @return entity's name
     */
    String getName();

    /**
     * get entity's description
     *
     * @return entity's description
     */
    String getDescription();
}
