package gamemechanics.battlefield.actionresults.events;

import gamemechanics.interfaces.MapNode;

public interface TurnEvent {
    Integer getEventKind();

    default MapNode getWhere() {
        return null;
    }

    default Integer getAmount() {
        return 0;
    }
}
