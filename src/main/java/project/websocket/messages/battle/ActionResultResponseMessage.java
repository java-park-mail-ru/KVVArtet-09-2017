package project.websocket.messages.battle;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.actionresults.events.TurnEvent;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.MapNode;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("unused")
public class ActionResultResponseMessage extends Message {
    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;
    private final List<TurnEvent> events;

    public ActionResultResponseMessage(@NotNull List<TurnEvent> events,
                                       @NotNull MapNode sender,
                                       @Nullable MapNode target,
                                       @Nullable Ability ability) {
        this.events = events;
        this.sender = sender;
        this.target = target;
        this.ability = ability;
    }

    public @NotNull MapNode getSender() {
        return sender;
    }

    public @Nullable MapNode getTarget() {
        return target;
    }

    public @Nullable Ability getAbility() {
        return ability;
    }

    public @NotNull List<TurnEvent> getEvents() {
        return events;
    }
}
