package project.gamemechanics.charlist;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.services.interfaces.CharacterListDAO;
import project.gamemechanics.services.interfaces.UserCharacterDAO;
import project.server.dao.UserDao;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CharacterListPoolService implements CharacterListPool {

    private final Map<Integer, CharacterList> characterListMap = new ConcurrentHashMap<>();
    private final CharacterListDAO characterListDAO;
    private final UserDao userDao;
    private final UserCharacterDAO userCharacterDAO;

    public CharacterListPoolService(CharacterListDAO characterListDAO, UserDao userDao, UserCharacterDAO userCharacterDAO) {
        this.characterListDAO = characterListDAO;
        this.userDao = userDao;
        this.userCharacterDAO = userCharacterDAO;
    }

    @Override
    public @Nullable CharacterList getCharacterList(@NotNull Integer ownerID) {
        return characterListMap.getOrDefault(ownerID, null);
    }

    @Override
    public Charlist initCharacterList(@NotNull Integer ownerID) {
        Integer charlistID = userDao.getCharacaterListIdByUserId(ownerID);
        List<UserCharacter> userCharacterList = new ArrayList<>();
        List<Integer> charactersList = characterListDAO.getCharacters(charlistID);

        for (Integer charId : charactersList) {
            UserCharacter userCharacter = new UserCharacter(userCharacterDAO.getUserCharacterById(charId));
            userCharacterList.add(userCharacter);
        }

        CharacterList.CharacterListModel characterListModel = new CharacterList.CharacterListModel(ownerID, charlistID, userCharacterList);
        CharacterList characterList = new CharacterList(characterListModel);
        characterListMap.put(ownerID, characterList);
        return characterList;
    }

    @Override
    public void deleteCharacterList(@NotNull Integer ownerId) {
        characterListMap.remove(ownerId);
    }
}
