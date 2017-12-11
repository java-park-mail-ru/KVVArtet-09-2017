package project.gamemechanics.battlefield.actionresults.events;

import project.gamemechanics.battlefield.map.helpers.Route;

import javax.validation.constraints.NotNull;

public class MoveEvent implements TurnEvent {
    private final Route route;

    public MoveEvent(@NotNull Route route) {
        this.route = route;
    }

    @Override
    public Integer getEventKind() {
        return EventCategories.EC_MOVE;
    }

    public Route getRoute() {
        return route;
    }
}

