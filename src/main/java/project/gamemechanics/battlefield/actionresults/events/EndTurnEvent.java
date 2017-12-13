package project.gamemechanics.battlefield.actionresults.events;

public class EndTurnEvent implements TurnEvent {

    @Override
    public Integer getEventKind() {
        return EventCategories.EC_END_TURN;
    }
}
