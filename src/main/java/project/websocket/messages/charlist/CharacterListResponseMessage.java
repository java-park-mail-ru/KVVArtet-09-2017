package project.websocket.messages.charlist;

import org.jetbrains.annotations.NotNull;
import project.gamemechanics.aliveentities.UserCharacter;
import project.websocket.messages.Message;

import java.util.List;

@SuppressWarnings("unused")
public class CharacterListResponseMessage extends Message {

    private @NotNull List<UserCharacter> characterList;

    public CharacterListResponseMessage(@NotNull List<UserCharacter> characterList) {
        this.characterList = characterList;
    }

    public @NotNull List<UserCharacter> getCharacterList() {
        return characterList;
    }

    public void setCharacterList(@NotNull List<UserCharacter> characterList) {
        this.characterList = characterList;
    }
}
