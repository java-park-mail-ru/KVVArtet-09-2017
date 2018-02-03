package project.gamemechanics.charlist;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class CharacterListPoolImpl implements CharacterListPool {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Map<Integer, CharacterList> characterListMap = new HashMap<>();


    @Override
    public @Nullable CharacterList getCharacterList(@NotNull Integer ownerID) {
        return characterListMap.get(ownerID);
    }

    @Override
    public void initCharacterList(@NotNull Integer ownerID) {
        //TODO HOW TO INITIALIZE DEFAULT CHARACTERS
       // CharacterList characterListDefault = new CharacterList();
        //characterListMap.put(ownerID, characterListDefault);
    }
}
