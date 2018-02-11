package project.gamemechanics.charlist;

import project.gamemechanics.aliveentities.AbstractAliveEntity.UserCharacterModel;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.interfaces.Charlist;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public class CharacterList implements Charlist {

    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer charlistID = INSTANCE_COUNTER.getAndIncrement();
    private final List<UserCharacter> characterList;
    private final Integer ownerID;

    @SuppressWarnings({"InstanceMethodNamingConvention", "unused", "RedundantSuppression"})
    public static class CharacterListModel {
        //noinspection VisibilityModifier
        // CHECKSTYLE:OFF
        @SuppressWarnings("RedundantSuppression")
        List<UserCharacter> characterList = new ArrayList<>();
        final Integer ownerID;
        // CHECKSTYLE:ON

        public CharacterListModel(@NotNull Integer ownerID, @NotNull List<UserCharacter> characterList) {
            this.ownerID = ownerID;
            this.characterList = characterList;
        }

        public CharacterListModel(@NotNull Integer ownerID) {
            this.ownerID = ownerID;
            this.setDefaultCharacters();
        }

        @SuppressWarnings("EmptyMethod")
        void setDefaultCharacters() {
            //TODO MAKE IT WORK
        }
    }

    public CharacterList(@NotNull CharacterListModel characterListModel) {
        this.characterList = characterListModel.characterList;
        this.ownerID = characterListModel.ownerID;
    }


    @Override
    public @NotNull Integer getID() {
        return charlistID;
    }

    @Override
    public @NotNull Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public @NotNull UserCharacter createChar(@NotNull UserCharacterModel userModel) {
        final UserCharacter newUserCharacter = new UserCharacter(userModel);
        this.characterList.add(newUserCharacter);
        //insert in database
        return newUserCharacter;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void deleteChar(@NotNull Integer index) {
        final Integer charID = this.characterList.get(index).getID();
        this.characterList.remove(index);
        //delete in database with charID
    }

    @Override
    public @NotNull Integer getOwnerId() {
        return ownerID;
    }

    public @NotNull List<UserCharacter> getCharacterList() {
        return characterList;
    }
}
