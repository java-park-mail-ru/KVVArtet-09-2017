package project.websocket.messages;
import project.gamemechanics.battlefield.actionresults.events.TurnEvent;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.MapNode;

import java.util.List;

public class ActionResultResponseMessage extends Message {
    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;
    private final List<TurnEvent> events;

    public ActionResultResponseMessage(List<TurnEvent> events,
                                       MapNode sender,
                                       MapNode target,
                                       Ability ability) {
        this.events = events;
        this.sender = sender;
        this.target = target;
        this.ability = ability;
    }

    public MapNode getSender() {
        return sender;
    }

    public MapNode getTarget() {
        return target;
    }

    public Ability getAbility() {
        return ability;
    }

    public List<TurnEvent> getEvents() {
        return events;
    }
}
