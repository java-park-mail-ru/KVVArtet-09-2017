package project.gamemechanics.charlist;

import java.util.HashMap;
import java.util.Map;

public class CharacterListPoolImpl implements CharacterListPool {

    Map<Integer, CharacterList> characterListMap = new HashMap<>();


    @Override
    public CharacterList getCharacterList(Integer ownerID) {
        return characterListMap.get(ownerID);
    }

    @Override
    public void initCharacterList(Integer ownerID) {
        //TODO HOW TO INITIALIZE DEFAULT CHARACTERS
       // CharacterList characterListDefault = new CharacterList();
        //characterListMap.put(ownerID, characterListDefault);
    }
}
