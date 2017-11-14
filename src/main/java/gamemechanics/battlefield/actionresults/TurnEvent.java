package gamemechanics.battlefield.actionresults;

import gamemechanics.interfaces.MapNode;

public interface TurnEvent {
    Integer getEventKind();
    MapNode getFrom();

    default MapNode getWhere() {
        return null;
    }

    default Integer getAmount() {
        return 0;
    }
}
