package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.smartcontroller.SmartController;
import project.states.CharacterListState;
import project.websocket.messages.CharacterListRequestMessage;
import project.websocket.messages.CharacterListResponseMessage;
import project.websocket.messages.Message;
import project.websocket.services.ConnectionPoolService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class CharacterListRequestHandler extends MessageHandler<CharacterListRequestMessage> {

    private final @NotNull ConnectionPoolService connectionPoolService;

    private final @NotNull CharacterListState characterListState;

    public CharacterListRequestHandler(@NotNull ConnectionPoolService connectionPoolService,
                                       @NotNull CharacterListState characterListState) {
        super(CharacterListRequestMessage.class);
        this.connectionPoolService = connectionPoolService;
        this.characterListState = characterListState;
    }

    @PostConstruct
    private void init() {
        characterListState.registerHandler(CharacterListRequestMessage.class, this);
    }

    @Override
    public Message handle(@NotNull CharacterListRequestMessage message, Integer forUser) {
        final SmartController activeSmart = connectionPoolService.getActiveSmartControllers().get(forUser);
        return new CharacterListResponseMessage(activeSmart.getCharacterList().getCharacterList());
    }
}
