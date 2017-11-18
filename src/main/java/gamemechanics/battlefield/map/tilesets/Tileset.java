package gamemechanics.battlefield.map.tilesets;

import gamemechanics.battlefield.actionresults.events.TurnEvent;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface Tileset {
    Integer getTilesCount();
    Boolean isValid();
    default void applyEffects(@NotNull List<TurnEvent> events) {}
}
