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
     * @return true if the action is a {@link gamemechanics.battlefield.map.actions.MovementAction}
     * or false otherwise
     */
    default Boolean isMovement() {
        return false;
    }

    /**
     * is the action a skip turn action
     * @return true if the action is a {@link gamemechanics.battlefield.map.actions.SkipTurnAction}
     * or false otherwise
     */
    default Boolean isSkip() {
        return false;
    }

    /**
     * execute action
     * @return true on execution success or false otherwise
     */
    /* TODO: change return type from boolean to some ActionResult
     * to store action results and prepare data for JSON response
     */
    Boolean execute();
}
