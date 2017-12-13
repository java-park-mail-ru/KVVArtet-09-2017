package project.gamemechanics.interfaces;

import project.gamemechanics.battlefield.actionresults.ActionResult;

/**
 * interface for various battle actions
 */
public interface Action extends Countable {
    MapNode getSender();

    MapNode getTarget();

    /**
     * get the {@link Ability} that's being implemented during the action
     *
     * @return null if it's a movement or skip turn actions or {@link Ability} otherwise
     * @see Ability
     */
    default Ability getAbility() {
        return null;
    }

    /**
     * is the action a movement action
     *
     * @return true if the action is a {@link project.gamemechanics.battlefield.map.actions.MovementAction}
     * or false otherwise
     */
    default Boolean isMovement() {
        return false;
    }

    /**
     * is the action a skip turn action
     *
     * @return true if the action is a {@link project.gamemechanics.battlefield.map.actions.SkipTurnAction}
     * or false otherwise
     */
    default Boolean isSkip() {
        return false;
    }

    /**
     * execute action
     *
     * @return {@link ActionResult}
     */
    ActionResult execute();
}
