package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.charlist.UserCharacterFactory;
import project.gamemechanics.smartcontroller.SmartController;
import project.states.CharacterListState;
import project.websocket.messages.Message;
import project.websocket.messages.charlist.CreateCharacterConfirmationMessage;
import project.websocket.messages.charlist.CreateCharacterRequestMessage;
import project.websocket.services.ConnectionPoolService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class CreateCharacterRequestHandler extends MessageHandler<CreateCharacterRequestMessage> {

    private final @NotNull CharacterListState characterListState;
    private final @NotNull ConnectionPoolService connectionPoolService;
    private final @NotNull UserCharacterFactory userCharacterFactory;

    public CreateCharacterRequestHandler(@NotNull CharacterListState characterListState,
                                         @NotNull ConnectionPoolService connectionPoolService, UserCharacterFactory userCharacterFactory) {
        super(CreateCharacterRequestMessage.class);
        this.characterListState = characterListState;
        this.connectionPoolService = connectionPoolService;
        this.userCharacterFactory = userCharacterFactory;
    }

    @PostConstruct
    private void init() {
        characterListState.registerHandler(CreateCharacterRequestMessage.class, this);
    }

    @Override
    public Message handle(@NotNull CreateCharacterRequestMessage message, @NotNull Integer forUser) {
        final SmartController activeSmart = connectionPoolService.getActiveSmartControllers().get(forUser);
        userCharacterFactory.createUserCharacter(message, activeSmart);
        return new CreateCharacterConfirmationMessage();
    }
}
