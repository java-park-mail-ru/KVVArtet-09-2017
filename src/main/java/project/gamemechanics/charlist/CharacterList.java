package project.gamemechanics.charlist;

import project.gamemechanics.aliveentities.AbstractAliveEntity.UserCharacterModel;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.services.interfaces.CharacterListDAO;
import project.gamemechanics.services.interfaces.UserCharacterDAO;
import project.server.dao.UserDao;
import project.websocket.messages.models.UserCharacterClientModel;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CharacterList implements Charlist {

    @NotNull
    private final Integer characterListID;
    private final List<UserCharacter> characterList;
    private final Integer ownerID;

    public static class CharacterListModel {
        // CHECKSTYLE:OFF
        List<UserCharacter> characterList = new ArrayList<>();
        Integer ownerID;
        Integer charlistID;
        CharacterListDAO characterListDAO;
        UserCharacterDAO userCharacterDAO;
        UserDao userDao;
        // CHECKSTYLE:ON

        public CharacterListModel(@NotNull Integer ownerID, @NotNull List<UserCharacter> characterList) {
            this.ownerID = ownerID;
            this.characterList = characterList;
        }

        CharacterListModel(@NotNull Integer ownerID) {
            this.ownerID = ownerID;
            this.setDefaultCharlist(ownerID);
        }

        void setDefaultCharlist(Integer ownerID) {
            this.charlistID = userDao.getCharacaterListIdByUserId(ownerID);
            List<Integer> charactersList = characterListDAO.getCharacters(this.charlistID);
            for (Integer charId : charactersList) {
                UserCharacter userCharacter = new UserCharacter(userCharacterDAO.getUserCharacterById(charId));
                this.characterList.add(userCharacter);
            }
        }
    }

    public CharacterList(@NotNull CharacterListModel characterListModel) {
        this.characterList = characterListModel.characterList;
        this.ownerID = characterListModel.ownerID;
        this.characterListID = characterListModel.charlistID;
    }

    @Override
    public @NotNull Integer getID() {
        return characterListID;
    }

    @Override
    public @NotNull Integer getInstancesCount() {
        return null;
    }

    @Override
    public @NotNull UserCharacter createCharacter(@NotNull UserCharacterModel userModel) {
        final UserCharacter newUserCharacter = new UserCharacter(userModel);
        this.characterList.add(newUserCharacter);
        return newUserCharacter;
    }

    @Override
    public void deleteCharacter(@NotNull Integer index) {
        this.characterList.remove(index);
    }

    @Override
    public @NotNull Integer getOwnerId() {
        return ownerID;
    }

    @Override
    public @NotNull List<UserCharacter> getCharacterList() {
        return characterList;
    }

    @Override
    public List<UserCharacterClientModel> packToUserCharacterClientModelList() {
        List<UserCharacterClientModel> userCharacterClientModels = new ArrayList<>();
        for (int i = 0; i < characterList.size(); i++) {
            UserCharacterClientModel characterToAdd = this.characterList.get(i).packToClientModel();
            characterToAdd.setCharIndexInCharist(i);
            userCharacterClientModels.add(characterToAdd);
        }
        return userCharacterClientModels;
    }
}
