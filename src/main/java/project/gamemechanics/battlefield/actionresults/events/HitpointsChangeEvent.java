package project.gamemechanics.battlefield.actionresults.events;

import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

public class HitpointsChangeEvent implements TurnEvent {
    private final MapNode where;
    private final Integer amount;

    public HitpointsChangeEvent(@NotNull MapNode where, @NotNull Integer amount) {
        this.where = where;
        this.amount = amount;
    }

    @Override
    public @NotNull Integer getEventKind() {
        return EventCategories.EC_HITPOINTS_CHANGE;
    }

    @Override
    public @NotNull MapNode getWhere() {
        return where;
    }

    @Override
    public @NotNull Integer getAmount() {
        return amount;
    }
}
