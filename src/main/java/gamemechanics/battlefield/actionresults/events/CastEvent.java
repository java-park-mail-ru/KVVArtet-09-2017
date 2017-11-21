package gamemechanics.battlefield.actionresults.events;

import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

public class CastEvent implements TurnEvent {
    private final MapNode where;
    private final Integer abilityID;

    public CastEvent(@NotNull MapNode where, @NotNull Integer abilityID) {
        this.where = where;
        this.abilityID = abilityID;
    }

    @Override
    public Integer getEventKind() {
        return EventCategories.EC_CAST;
    }

    @Override
    public MapNode getWhere() {
        return where;
    }
}
