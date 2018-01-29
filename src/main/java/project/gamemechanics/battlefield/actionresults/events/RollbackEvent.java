package project.gamemechanics.battlefield.actionresults.events;

import javax.validation.constraints.NotNull;

public class RollbackEvent implements TurnEvent {

    @Override
    public @NotNull Integer getEventKind() {
        return EventCategories.EC_ROLLBACK;
    }
}
