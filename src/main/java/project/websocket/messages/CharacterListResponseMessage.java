package project.websocket.messages;

import org.jetbrains.annotations.NotNull;
import project.gamemechanics.aliveentities.UserCharacter;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class CharacterListResponseMessage extends Message {

    @NotNull private List<UserCharacter> characterList;

    public CharacterListResponseMessage(@NotNull List<UserCharacter> characterList) {
        this.characterList = characterList;
    }

    @NotNull
    public List<UserCharacter> getCharacterList() {
        return characterList;
    }

    public void setCharacterList(@NotNull List<UserCharacter> characterList) {
        this.characterList = characterList;
    }
}
