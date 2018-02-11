package project.gamemechanics.battlefield.actionresults.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public class CastEvent implements TurnEvent {
    private final MapNode where;
    private final Integer abilityID;

    public CastEvent(@NotNull MapNode where, @NotNull Integer abilityID) {
        this.where = where;
        this.abilityID = abilityID;
    }

    @Override
    public @NotNull Integer getEventKind() {
        return EventCategories.EC_CAST;
    }

    @Override
    public @NotNull MapNode getWhere() {
        return where;
    }

    @JsonProperty("abilityId")
    public @NotNull Integer getAbilityID() {
        return abilityID;
    }
}
