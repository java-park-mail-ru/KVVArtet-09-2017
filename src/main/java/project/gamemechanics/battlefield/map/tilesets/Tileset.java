package project.gamemechanics.battlefield.map.tilesets;

import project.gamemechanics.battlefield.actionresults.events.TurnEvent;

import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("unused")
public interface Tileset {
    @NotNull Integer getTilesCount();

    @NotNull Boolean isValid();

    default void applyEffects(@NotNull List<TurnEvent> events) {
    }
}
