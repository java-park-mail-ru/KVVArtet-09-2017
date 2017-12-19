package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.world.World;
import project.states.LobbyState;
import project.websocket.messages.LobbyRequestMessage;
import project.websocket.messages.Message;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
@SuppressWarnings("FieldCanBeLocal")
@Component
public class LobbyRequestHandler extends MessageHandler<LobbyRequestMessage> {

    @NotNull
    private final World world;
    @NotNull
    private final LobbyState lobbyState;

    public LobbyRequestHandler(@NotNull World world, @NotNull LobbyState lobbyState) {
        super(LobbyRequestMessage.class);
        this.world = world;
        this.lobbyState = lobbyState;
    }

    @PostConstruct
    private void init() {
        lobbyState.registerHandler(LobbyRequestMessage.class, this);
    }

    @Override
    public Message handle(@NotNull LobbyRequestMessage message, Integer forUser) {
        //TODO some world class method
        return message;
    }
}