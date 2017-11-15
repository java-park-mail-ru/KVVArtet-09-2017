package gamemechanics.battlefield.actionresults.events;

import gamemechanics.battlefield.map.helpers.Route;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

public final class EventsFactory {
    private EventsFactory() {}

    public static TurnEvent makeMovementEvent(@NotNull Route route) {
        return new MoveEvent(route);
    }

    public static TurnEvent makeHitpointsChangeEvent(@NotNull MapNode where, Integer amount) {
        return new HitpointsChangeEvent(where, amount);
    }

    public static TurnEvent makeCastEvent(@NotNull MapNode where, Integer abilityID) {
        return new CastEvent(where, abilityID);
    }

    public static TurnEvent makeEndTurnEvent() {
        return new EndTurnEvent();
    }

    public static TurnEvent makeRollbackEvent() {
        return new RollbackEvent();
    }
}
