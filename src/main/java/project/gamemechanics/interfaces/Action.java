package project.gamemechanics.interfaces;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.actionresults.ActionResult;

import javax.validation.constraints.NotNull;

/**
 * interface for various battle actions.
 */
@SuppressWarnings({"RedundantSuppression", "unused"})
public interface Action extends Countable {
    @NotNull MapNode getSender();

    @Nullable MapNode getTarget();

    /**
     * get the {@link Ability} that's being implemented during the action.
     *
     * @return null if it's a movement or skip turn actions or {@link Ability} otherwise
     * @see Ability
     */
    @SuppressWarnings("ConstantConditions")
    default @Nullable Ability getAbility() {
        return null;
    }

    /**
     * is the action a movement action.
     *
     * @return true if the action is a {@link project.gamemechanics.battlefield.map.actions.MovementAction}
     *     or false otherwise.
     */
    default @NotNull Boolean isMovement() {
        return false;
    }

    /**
     * is the action a skip turn action.
     *
     * @return true if the action is a {@link project.gamemechanics.battlefield.map.actions.SkipTurnAction}
     *     or false otherwise.
     */
    default @NotNull Boolean isSkip() {
        return false;
    }

    /**
     * execute action.
     *
     * @return {@link ActionResult}
     */
    @NotNull ActionResult execute();
}
