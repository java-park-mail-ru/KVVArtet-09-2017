package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.world.World;
import project.states.DungeonState;
import project.websocket.messages.CreateCharacterRequestMessage;
import project.websocket.messages.Message;
import project.websocket.messages.NextRoomRequestMessage;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
@Component
public class NextRoomRequestHandler extends MessageHandler<NextRoomRequestMessage> {

    @NotNull
    private World world;
    @NotNull
    private DungeonState dungeonState;

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
    public Message handle(@NotNull NextRoomRequestMessage message, Integer forUser) {
        //TODO some world class method

        world.getActiveInstances().get(forUser);
        return message;
    }
}