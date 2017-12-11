package gamemechanics.battlefield.actionresults;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gamemechanics.battlefield.actionresults.events.TurnEvent;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(BattleActionResult.class),
})
public interface ActionResult {
    Integer getActionID();

    MapNode getSender();

    default MapNode getTarget() {
        return null;
    }

    default Ability getAbility() {
        return null;
    }

    Integer getEventsCount();

    TurnEvent getEvent(@NotNull Integer eventIndex);

    void addEvent(@NotNull TurnEvent event);

    void addEvent(@NotNull Integer position, @NotNull TurnEvent event);

    Integer getEventIndex(@NotNull TurnEvent event);

    Boolean getIsProcessed();

    void markProcessed();
}
