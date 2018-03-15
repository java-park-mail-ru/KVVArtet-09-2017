package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.smartcontroller.SmartController;
import project.states.CharacterListState;
import project.websocket.messages.Message;
import project.websocket.messages.charlist.CharacterListRequestMessage;
import project.websocket.messages.charlist.CharacterListResponseMessage;
import project.websocket.services.ConnectionPoolService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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
    public @NotNull Message handle(@NotNull CharacterListRequestMessage message, @NotNull Integer forUser) {
        final SmartController activeSmart = connectionPoolService.getActiveSmartControllers().get(forUser);
        return new CharacterListResponseMessage(Objects.requireNonNull(activeSmart.getCharacterList()).getCharacterList());
    }
}
