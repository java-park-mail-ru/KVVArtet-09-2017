package gamemechanics.charlist;

import gamemechanics.aliveentities.AbstractAliveEntity.UserCharacterModel;
import gamemechanics.aliveentities.UserCharacter;
import gamemechanics.interfaces.Charlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CharacterList implements Charlist {

    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer charlistID = INSTANCE_COUNTER.getAndIncrement();
    private List<UserCharacter> characterList = new ArrayList<>();
    private final Integer ownerID;

    public static class CharacterListModel {
        List<UserCharacter> characterList = new ArrayList<>();
        final Integer ownerID;

        public CharacterListModel(Integer ownerID, List<UserCharacter> characterList) {
            this.ownerID = ownerID;
            this.characterList = characterList;
        }
    }

    public CharacterList(CharacterListModel characterListModel) {
        this.characterList = characterListModel.characterList;
        this.ownerID = characterListModel.ownerID;
    }

    @Override
    public Integer getID() {
        return charlistID;
    }

    @Override
    public Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public UserCharacter createChar(UserCharacterModel userModel) {
        final UserCharacter newUserCharacter = new UserCharacter(userModel);
        this.characterList.add(newUserCharacter);
        //insert in database
        return newUserCharacter;
    }

    @Override
    public void deleteChar(Integer index) {
        final Integer charID = this.characterList.get(index).getID();
        this.characterList.remove(index);
        //delete in database with charID
    }

    @Override
    public Integer getOwnerId() {
        return ownerID;
    }
}
