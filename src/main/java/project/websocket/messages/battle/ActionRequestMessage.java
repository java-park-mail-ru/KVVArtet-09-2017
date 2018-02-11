package project.websocket.messages.battle;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.MapNode;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public class ActionRequestMessage extends Message {
    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;

    public ActionRequestMessage(@NotNull MapNode sender,
                                @Nullable MapNode target,
                                @Nullable Ability ability) {
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

}
