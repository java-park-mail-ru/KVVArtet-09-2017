package project.gamemechanics.charlist;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import project.gamemechanics.services.interfaces.CharacterListDAO;
import project.server.dao.UserDao;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CharacterListPoolService implements CharacterListPool {

    private Map<Integer, CharacterList> characterListMap = new ConcurrentHashMap<>();
    private CharacterListDAO characterListDAO;

    public CharacterListPoolService(CharacterListDAO characterListDAO) {
        this.characterListDAO = characterListDAO;
    }

    @Override
    public @Nullable CharacterList getCharacterList(@NotNull Integer ownerID) {
        return characterListMap.getOrDefault(ownerID, null);
    }

    @Override
    public Charlist initCharacterList(@NotNull Integer ownerID) {
        CharacterList.CharacterListModel characterListModel = new CharacterList.CharacterListModel(ownerID);
        CharacterList characterList = new CharacterList(characterListModel);
        characterListMap.put(ownerID, characterList);
        return characterList;
    }

    @Override
    public void updateCharacterList(Integer ownerId) {
        Integer characterListId = characterListMap.get(ownerId).getID();
        List<Integer> characterIdsList = characterListDAO.getCharacters(characterListId);

    }
}
