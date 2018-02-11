package project.gamemechanics.smartcontroller;

import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.WebSocketSession;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;
import project.statemachine.StateService;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;
import java.util.ArrayDeque;
import java.util.Deque;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class SmartController {
    private final Deque<Message> inboxMessageQueue = new ArrayDeque<>();
    private final Deque<Message> outboxMessageQueue = new ArrayDeque<>();
    private UserCharacter activeChar;
    private CharacterList characterList;
    private final StateService stateService = new StateService();
    private WebSocketSession webSocketSession;
    private Integer ownerID;

    public void tick() {
        while (!inboxMessageQueue.isEmpty()) {
            outboxMessageQueue.add(stateService.handleMessage(inboxMessageQueue.getFirst(), this.ownerID));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public @Nullable Message getOutboxMessage() {
        if (outboxMessageQueue.isEmpty()) {
            return null;
        } else {
            return outboxMessageQueue.getFirst();
        }
    }

    public void addInboxMessage(@NotNull Message inboxMessage) {
        this.inboxMessageQueue.add(inboxMessage);
    }

    public @Nullable UserCharacter getActiveChar() {
        return activeChar;
    }

    public void setActiveChar(@Nullable UserCharacter activeChar) {
        this.activeChar = activeChar;
    }

    public @NotNull CharacterList getCharacterList() {
        return characterList;
    }

    public void setCharacterList(@NotNull CharacterList characterList) {
        this.characterList = characterList;
    }

    public @Nullable WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(@NotNull WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public @NotNull Boolean isValid() {
        return webSocketSession.isOpen();
    }

    public void setOwnerID(@NotNull Integer ownerID) {
        this.ownerID = ownerID;
    }

    public @NotNull Integer getOwnerID() {
        return ownerID;
    }
}
