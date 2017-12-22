package project.gamemechanics.battlefield.actionresults.events;

public class RollbackEvent implements TurnEvent {

    @Override
    public Integer getEventKind() {
        return EventCategories.EC_ROLLBACK;
    }
}
