package project.websocket.messages;

import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.MapNode;

public class ActionRequestMessage extends Message {
    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;

    public ActionRequestMessage(MapNode sender,
                                MapNode target,
                                Ability ability) {
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

}
