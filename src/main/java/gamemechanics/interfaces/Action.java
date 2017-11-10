package gamemechanics.interfaces;

/**
 * interface for various battle actions
 */
public interface Action extends Countable {
    MapNode getSender();
    MapNode getTarget();

    /**
     * get the {@link Ability} that's being implemented during the action
     * @return null if it's a movement or skip turn actions or {@link Ability} otherwise
     * @see Ability
     */
    default Ability getAbility() {
        return null;
    }

    /**
     * is the action a movement action
     * @return true if the action is a movement or false otherwise
     */
    Boolean isMovement();

    /**
     * execute action
     * @return true on execution success or false otherwise
     */
    Boolean execute();
}
