package project.gamemechanics.battlefield.map.tilesets;

import project.gamemechanics.battlefield.actionresults.events.TurnEvent;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface Tileset {
    Integer getTilesCount();

    Boolean isValid();

    default void applyEffects(@NotNull List<TurnEvent> events) {
    }
}
