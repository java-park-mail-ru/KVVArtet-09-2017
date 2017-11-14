package gamemechanics.battlefield.actionresults;

import gamemechanics.interfaces.MapNode;

public interface ActionResult {
    Integer getActionID();
    MapNode getSender();

    default MapNode getTarget() {
        return null;
    }

    Boolean isProcessed();
}
