package project.websocket.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.websocket.messages.battle.*;
import project.websocket.messages.charlist.CharacterListRequestMessage;
import project.websocket.messages.charlist.CharacterListResponseMessage;
import project.websocket.messages.charlist.CreateCharacterRequestMessage;
import project.websocket.messages.charlist.DeleteCharacterRequestMessage;
import project.websocket.messages.matchmaking.LobbyConfirmationMessage;
import project.websocket.messages.matchmaking.LobbyRequestMessage;
import project.websocket.messages.matchmaking.MatchmakingNotificationMessage;
import project.websocket.messages.typecontainer.BooleanMessage;

@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(ActionResultResponseMessage.class),
        @Type(ActionRequestMessage.class),
        @Type(CharacterListResponseMessage.class),
        @Type(CharacterListRequestMessage.class),
        @Type(CreateCharacterRequestMessage.class),
        @Type(DeleteCharacterRequestMessage.class),
        @Type(LobbyRequestMessage.class),
        @Type(NextRoomRequestMessage.class),
        @Type(NextRoomResponseMessage.class),
        @Type(LobbyConfirmationMessage.class),
        @Type(ActionConfirmationMessage.class),
        @Type(MatchmakingNotificationMessage.class),
        @Type(BooleanMessage.class)
})
public abstract class Message {
    // CHECKSTYLE:OFF
    String status;
    // CHECKSTYLE:ON
}