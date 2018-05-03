package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.charlist.UserCharacterFactory;
import project.gamemechanics.smartcontroller.SmartController;
import project.states.CharacterListState;
import project.websocket.messages.Message;
import project.websocket.messages.charlist.DeleteCharacterConfirmationMessage;
import project.websocket.messages.charlist.DeleteCharacterRequestMessage;
import project.websocket.services.ConnectionPoolService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class DeleteCharacterRequestHandler extends MessageHandler<DeleteCharacterRequestMessage> {

    private final @NotNull CharacterListState characterListState;
    private final @NotNull ConnectionPoolService connectionPoolService;
    private final @NotNull UserCharacterFactory userCharacterFactory;

    public DeleteCharacterRequestHandler(@NotNull CharacterListState characterListState,
                                         @NotNull ConnectionPoolService connectionPoolService, UserCharacterFactory userCharacterFactory) {
        super(DeleteCharacterRequestMessage.class);
        this.characterListState = characterListState;
        this.connectionPoolService = connectionPoolService;
        this.userCharacterFactory = userCharacterFactory;
    }

    @PostConstruct
    private void init() {
        characterListState.registerHandler(DeleteCharacterRequestMessage.class, this);
    }

    @Override
    protected @NotNull Message handle(@NotNull DeleteCharacterRequestMessage message, @NotNull Integer forUser) {
        final SmartController activeSmart = connectionPoolService.getActiveSmartControllers().get(forUser);
        userCharacterFactory.deleteUserCharacter(message, activeSmart);
        return new DeleteCharacterConfirmationMessage();
    }
}
