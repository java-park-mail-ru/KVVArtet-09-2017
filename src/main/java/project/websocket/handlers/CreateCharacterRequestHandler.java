package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.world.World;
import project.states.CharacterListState;
import project.websocket.messages.CharacterListRequestMessage;
import project.websocket.messages.CreateCharacterRequestMessage;
import project.websocket.messages.Message;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
@Component
public class CreateCharacterRequestHandler extends MessageHandler<CreateCharacterRequestMessage> {

    @NotNull
    private World world;
    @NotNull
    private CharacterListState characterListState;

    public CreateCharacterRequestHandler(@NotNull World world, @NotNull CharacterListState characterListState) {
        super(CreateCharacterRequestMessage.class);
        this.world = world;
        this.characterListState = characterListState;
    }

    @PostConstruct
    private void init() {
        characterListState.registerHandler(CreateCharacterRequestMessage.class, this);
    }

    @Override
    public Message handle(@NotNull CreateCharacterRequestMessage message, Integer forUser) {
        //TODO some world class method
        return message;
    }
}
