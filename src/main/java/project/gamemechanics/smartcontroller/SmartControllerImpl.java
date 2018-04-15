package project.gamemechanics.smartcontroller;

import org.jetbrains.annotations.Nullable;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;
import project.gamemechanics.charlist.Charlist;
import project.gamemechanics.globals.Constants;

import project.statemachine.StateService;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class SmartControllerImpl implements SmartController {
    private final Deque<Message> inboxMessageQueue = new ArrayDeque<>();
    private final Deque<Message> outboxMessageQueue = new ArrayDeque<>();

    private UserCharacter activeChar = null;
    private Charlist characterList = null;
    private final StateService stateService = new StateService();
    private WebSocketSession webSocketSession = null;
    private Integer ownerID = Constants.UNDEFINED_ID;

    @Override
    public void tick() {
        while (!inboxMessageQueue.isEmpty()) {
            outboxMessageQueue.add(stateService
                    .handleMessage(inboxMessageQueue.getFirst(), this.ownerID));
        }
    }

    @Override
    public @Nullable Message getOutboxMessage() {
        if (outboxMessageQueue.isEmpty()) {
            return null;
        } else {
            return outboxMessageQueue.getFirst();
        }
    }

    @Override
    public void addInboxMessage(@NotNull Message inboxMessage) {
        this.inboxMessageQueue.add(inboxMessage);
    }

    @Override
    public @Nullable UserCharacter getActiveChar() {
        return activeChar;
    }

    @Override
    public void setActiveChar(@Nullable UserCharacter activeChar) {

        final Boolean belongsToUser = activeChar != null
                && characterList.getCharacterList().contains(activeChar)
                && activeChar.getOwnerID().equals(ownerID);
        if (isValid() && (activeChar == null || belongsToUser)) {
            this.activeChar = activeChar;
        }
    }

    @Override
    public @Nullable Charlist getCharacterList() {
        return characterList;
    }

    @Override
    public void setCharacterList(@NotNull CharacterList characterList) {

        if (this.characterList == null) {
            this.characterList = characterList;
        }
    }

    @Override
    public @Nullable WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    @Override
    public @NotNull Boolean isValid() {
        //noinspection OverlyComplexBooleanExpression
        return ownerID != Constants.UNDEFINED_ID
                && webSocketSession != null
                && webSocketSession.isOpen()
                && characterList != null;
    }

    @Override
    public @NotNull Integer getOwnerID() {
        return ownerID;
    }

    @Override
    public void reset(@NotNull CloseStatus closeStatus) {
        ownerID = Constants.UNDEFINED_ID;
        closeConnection(closeStatus);
        webSocketSession = null;
        characterList = null;
        activeChar = null;
        if (!inboxMessageQueue.isEmpty()) {
            inboxMessageQueue.clear();
        }
        if (!outboxMessageQueue.isEmpty()) {
            outboxMessageQueue.clear();
        }
    }

    @Override
    public void reset() {
        reset(CloseStatus.NORMAL);
    }

    @Override
    public @NotNull Boolean set(@NotNull Integer newOwnerID,
                                @NotNull WebSocketSession newWebSocketSession,
                                @Nullable Charlist newCharacterList) {
        if (!isValid()) {
            ownerID = newOwnerID;
            if (webSocketSession != null) {
                closeConnection(CloseStatus.NORMAL);
                webSocketSession = null;
            }
            webSocketSession = newWebSocketSession;
            characterList = newCharacterList;
            if (activeChar != null) {
                activeChar = null;
            }
            if (!inboxMessageQueue.isEmpty()) {
                inboxMessageQueue.clear();
            }
            if (!outboxMessageQueue.isEmpty()) {
                outboxMessageQueue.clear();
            }
            return true;
        }
        return false;
    }

    private void closeConnection(@NotNull CloseStatus closeStatus) {
        if (isValid()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
