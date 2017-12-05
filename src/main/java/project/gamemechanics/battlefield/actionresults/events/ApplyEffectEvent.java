package gamemechanics.battlefield.actionresults.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import gamemechanics.interfaces.Effect;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

public class ApplyEffectEvent implements TurnEvent {
    private final MapNode where;
    private final Effect appliedEffect;

    public ApplyEffectEvent(@NotNull MapNode where, @NotNull Effect appliedEffect) {
        this.where = where;
        this.appliedEffect = appliedEffect;
    }

    @Override
    public Integer getEventKind() {
        return EventCategories.EC_APPLY_EFFECT;
    }

    @Override
    public MapNode getWhere() {
        return where;
    }

    @JsonProperty("effect")
    public Effect getAppliedEffect() {
        return appliedEffect;
    }
}
