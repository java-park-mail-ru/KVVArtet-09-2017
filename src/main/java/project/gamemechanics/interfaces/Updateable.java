package project.gamemechanics.interfaces;

import javax.validation.constraints.NotNull;

/**
 * An interface for entities which have some repeatable routine either
 * for turn-based battle mode or for project.server ticks
 */
public interface Updateable {
    /**
     * routine to repeat on each battle turn.
     */
    default void update() {
    } // for battle turns

    /**
     * routine to repeat on each project.server tick (once per given timestep)
     *
     * @param timestep fixed timestep value
     */
    @SuppressWarnings("unused")
    default void update(@NotNull Integer timestep) {
    } // for any other occasions;
}
