package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.world.World;
import project.states.DungeonState;
import project.websocket.messages.Message;
import project.websocket.messages.battle.NextRoomRequestMessage;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class NextRoomRequestHandler extends MessageHandler<NextRoomRequestMessage> {

    private final @NotNull World world;
    private final @NotNull DungeonState dungeonState;

    public NextRoomRequestHandler(@NotNull World world, @NotNull DungeonState dungeonState) {
        super(NextRoomRequestMessage.class);
        this.world = world;
        this.dungeonState = dungeonState;
    }

    @PostConstruct
    private void init() {
        dungeonState.registerHandler(NextRoomRequestMessage.class, this);
    }

    @Override
    public Message handle(@NotNull NextRoomRequestMessage message, @NotNull Integer forUser) {
        //TODO NOT AVAILABLE NOW CAUSE RESPONSE DOES'NT REQUIRE REQUEST AND SENDING BY HIMSELF

        world.getActiveInstances().get(forUser);
        return message;
    }
}