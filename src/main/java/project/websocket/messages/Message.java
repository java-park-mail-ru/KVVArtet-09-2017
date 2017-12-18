package project.websocket.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
        @Type(StayInLineRequestMessage.class),
        @Type(LobbyConfirmationMessage.class)
})
public abstract class Message {
    String status;
}