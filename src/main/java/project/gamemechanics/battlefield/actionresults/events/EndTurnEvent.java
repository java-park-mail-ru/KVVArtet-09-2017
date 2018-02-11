package project.gamemechanics.battlefield.actionresults.events;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public class EndTurnEvent implements TurnEvent {

    @Override
    public @NotNull Integer getEventKind() {
        return EventCategories.EC_END_TURN;
    }
}
