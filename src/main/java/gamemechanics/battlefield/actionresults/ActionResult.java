package gamemechanics.battlefield.actionresults;

import gamemechanics.battlefield.actionresults.events.TurnEvent;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

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

    TurnEvent getEvent(Integer eventIndex);

    void addEvent(@NotNull TurnEvent event);

    void addEvent(Integer position, @NotNull TurnEvent event);

    Integer getEventIndex(@NotNull TurnEvent event);

    Boolean getIsProcessed();

    void markProcessed();
}
