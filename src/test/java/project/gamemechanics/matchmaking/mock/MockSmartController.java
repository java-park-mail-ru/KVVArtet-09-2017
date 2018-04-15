package project.gamemechanics.matchmaking.mock;

import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;
import project.gamemechanics.charlist.Charlist;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.smartcontroller.SmartController;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;
import java.util.ArrayDeque;
import java.util.Deque;

public class MockSmartController implements SmartController {
    private Integer ownerId = Constants.UNDEFINED_ID;

    private final Deque<Message> outbox = new ArrayDeque<>();

    private UserCharacter activeChar = null;

    private Charlist characterList = null;

    @Override
    public void tick() {
        outbox.clear();
    }

    @Override
    public @Nullable Message getOutboxMessage() {
        return outbox.isEmpty() ? null : outbox.getFirst();
    }

    @Override
    public void addInboxMessage(@NotNull Message inboxMessage) {
        if (isValid()) {
            outbox.offerLast(inboxMessage);
        }
    }

    @Override
    public @Nullable UserCharacter getActiveChar() {
        return activeChar;
    }

    @Override
    public void setActiveChar(@NotNull UserCharacter activeChar) {
        final Boolean belongsToUser = activeChar != null
                && activeChar.getOwnerID().equals(ownerId);
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
        if (!isValid()) {
            this.characterList = characterList;
        }
    }

    @Override
    public @Nullable WebSocketSession getWebSocketSession() {
        return null;
    }

    @Override
    public @NotNull Integer getOwnerID() {
        return ownerId;
    }

    @Override
    public @NotNull Boolean isValid() {
        return ownerId != Constants.UNDEFINED_ID
                && characterList != null;
    }

    @Override
    public void reset(@NotNull CloseStatus closeStatus) {
        ownerId = Constants.UNDEFINED_ID;
        activeChar = null;
        outbox.clear();
        characterList = null;
    }

    @Override
    public void reset() {
        reset(CloseStatus.NORMAL);
    }

    @Override
    public @NotNull Boolean set(@NotNull Integer ownerId,
                                @NotNull WebSocketSession session,
                                @Nullable Charlist characterList) {
        if (!isValid()) {
            this.ownerId = ownerId;
            this.characterList = characterList;
            if (!outbox.isEmpty()) {
                outbox.clear();
            }
            activeChar = null;
            return true;
        }
        return false;
    }
}
