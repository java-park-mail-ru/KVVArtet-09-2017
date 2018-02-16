package project.gamemechanics.smartcontroller;

import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.WebSocketSession;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public interface SmartController {

    void tick();

    Message getOutboxMessage();

    void addInboxMessage(Message message);

    UserCharacter getActiveChar();

    void setActiveChar(@Nullable UserCharacter activeChar);

    @NotNull CharacterList getCharacterList();

    void setCharacterList(@NotNull CharacterList characterList);

    @Nullable WebSocketSession getWebSocketSession();

    void setWebSocketSession(@NotNull WebSocketSession webSocketSession);

    @NotNull Boolean isValid();

    void setOwnerID(@NotNull Integer ownerID);

    @NotNull Integer getOwnerID();
}
