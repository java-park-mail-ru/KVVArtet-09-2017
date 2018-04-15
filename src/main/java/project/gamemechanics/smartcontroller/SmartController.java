package project.gamemechanics.smartcontroller;

import org.jetbrains.annotations.Nullable;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;
import project.gamemechanics.charlist.Charlist;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public interface SmartController {

    void tick();

    @Nullable Message getOutboxMessage();

    void addInboxMessage(@NotNull Message message);

    @Nullable UserCharacter getActiveChar();

    void setActiveChar(@Nullable UserCharacter activeChar);

    @Nullable Charlist getCharacterList();

    void setCharacterList(@NotNull CharacterList characterList);

    @Nullable WebSocketSession getWebSocketSession();

    @NotNull Boolean isValid();

    @NotNull Integer getOwnerID();

    void reset(@NotNull CloseStatus closeStatus);

    void reset();

    @NotNull Boolean set(@NotNull Integer ownerID,
                         @NotNull WebSocketSession webSocketSession,
                         @Nullable Charlist characterList);
}
