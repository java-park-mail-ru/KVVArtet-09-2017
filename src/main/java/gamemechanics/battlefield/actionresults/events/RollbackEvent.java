package gamemechanics.battlefield.actionresults.events;

public class RollbackEvent implements TurnEvent {
    public RollbackEvent() {}

    @Override
    public Integer getEventKind() {
        return EventCategories.EC_ROLLBACK;
    }
}
