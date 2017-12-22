package project.gamemechanics.battlefield.actionresults.events;

@SuppressWarnings("unused")
public class EndTurnEvent implements TurnEvent {

    @Override
    public Integer getEventKind() {
        return EventCategories.EC_END_TURN;
    }
}
