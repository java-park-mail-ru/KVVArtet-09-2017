package project.gamemechanics.charlist;

import project.gamemechanics.aliveentities.AbstractAliveEntity.UserCharacterModel;
import project.gamemechanics.aliveentities.UserCharacter;
import project.websocket.messages.models.UserCharacterClientModel;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CharacterList implements Charlist {

    private final Integer characterListID;
    private final List<UserCharacter> characterList;
    private final Integer ownerID;

    public static class CharacterListModel {
        private final List<UserCharacter> characterList;
        private final Integer ownerID;
        private final Integer charlistID;

        public CharacterListModel(@NotNull Integer ownerID, Integer charlistID, @NotNull List<UserCharacter> characterList) {
            this.ownerID = ownerID;
            this.charlistID = charlistID;
            this.characterList = characterList;
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

    @SuppressWarnings("SuspiciousMethodCalls")
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
