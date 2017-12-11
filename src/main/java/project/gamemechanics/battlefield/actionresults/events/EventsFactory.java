package project.gamemechanics.battlefield.actionresults.events;

import project.gamemechanics.battlefield.map.helpers.Route;
import project.gamemechanics.interfaces.Effect;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

public final class EventsFactory {
    private EventsFactory() {
    }

    public static TurnEvent makeMovementEvent(@NotNull Route route) {
        return new MoveEvent(route);
    }

    public static TurnEvent makeHitpointsChangeEvent(@NotNull MapNode where, Integer amount) {
        return new HitpointsChangeEvent(where, amount);
    }

    public static TurnEvent makeCastEvent(@NotNull MapNode where, Integer abilityID) {
        return new CastEvent(where, abilityID);
    }

    public static TurnEvent makeRewardEvent(@NotNull MapNode where, @NotNull Integer expAmount,
                                            @NotNull Integer cashAmount) {
        return new RewardEvent(where, expAmount, cashAmount);
    }

    public static TurnEvent makeApplyEffectEvent(@NotNull MapNode where, @NotNull Effect effect) {
        return new ApplyEffectEvent(where, effect);
    }

    public static TurnEvent makeEndTurnEvent() {
        return new EndTurnEvent();
    }

    public static TurnEvent makeRollbackEvent() {
        return new RollbackEvent();
    }
}
