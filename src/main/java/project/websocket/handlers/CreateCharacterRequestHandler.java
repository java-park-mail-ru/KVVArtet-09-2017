package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.world.World;
import project.states.CharacterListState;
import project.websocket.messages.Message;
import project.websocket.messages.charlist.CreateCharacterRequestMessage;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Component
public class CreateCharacterRequestHandler extends MessageHandler<CreateCharacterRequestMessage> {

    @SuppressWarnings("FieldCanBeLocal")
    private final @NotNull World world;
    private final @NotNull CharacterListState characterListState;

    public CreateCharacterRequestHandler(@NotNull World world,
                                         @NotNull CharacterListState characterListState) {
        super(CreateCharacterRequestMessage.class);
        this.world = world;
        this.characterListState = characterListState;
    }

    @PostConstruct
    private void init() {
        characterListState.registerHandler(CreateCharacterRequestMessage.class, this);
    }

    @Override
    public Message handle(@NotNull CreateCharacterRequestMessage message, @NotNull Integer forUser) {
        //TODO NOT AVAILABLE NOW
        return message;
    }
}
