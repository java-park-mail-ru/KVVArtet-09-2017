package project.websocket.messages.charlist;

import org.jetbrains.annotations.NotNull;
import project.websocket.messages.Message;
import project.websocket.messages.models.UserCharacterClientModel;

import java.util.List;

@SuppressWarnings("unused")
public class CharacterListResponseMessage extends Message {

    private @NotNull List<UserCharacterClientModel> characterListForClient;

    public CharacterListResponseMessage(@NotNull List<UserCharacterClientModel> characterList) {
        this.characterListForClient = characterList;
    }

    @NotNull
    public List<UserCharacterClientModel> getCharacterListForClient() {
        return characterListForClient;
    }

    public void setCharacterListForClient(@NotNull List<UserCharacterClientModel> characterListForClient) {
        this.characterListForClient = characterListForClient;
    }
}
