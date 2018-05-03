package project.gamemechanics.charlist;

import project.gamemechanics.aliveentities.AbstractAliveEntity.UserCharacterModel;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.interfaces.Countable;
import project.websocket.messages.models.UserCharacterClientModel;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * interface for charlist {@link CharacterList}.
 * Implementations shall always provide access to the {@link CharacterList}.
 */
@SuppressWarnings({"RedundantSuppression", "unused"})
public interface Charlist extends Countable {
    /**
     * create a {@link UserCharacter} and add him in {@link CharacterList}.
     *
     * @param userCharacterModel that need to be fulfill
     * @return null if there's no {@link UserCharacterModel} in @param or {@link UserCharacter} otherwise
     */
    @SuppressWarnings("ConstantConditions")
    default @NotNull UserCharacter createCharacter(@NotNull UserCharacterModel userCharacterModel) {
        return null;
    }

    /**
     * delete a character from {@link CharacterList}.
     *
     * @param index of {@link UserCharacter} in {@link CharacterList}
     */
    default void deleteCharacter(@NotNull Integer index) {

    }

    /**
     * get ID of an owner of a {@link CharacterList}.
     *
     * @return null if there's empty ownerID in {@link CharacterList} and ownerID otherwise
     */
    default @NotNull Integer getOwnerId() {
        return 0;
    }

    /**
     * get list of {@link UserCharacter}.
     *
     * @return null if there's empty list in {@link CharacterList} and list of {@link UserCharacter} otherwise
     */
    default List<UserCharacter> getCharacterList() {
        return null;
    }

    /**
     * get list of {@link UserCharacterClientModel}.
     *
     * @return null if there's empty list in {@link CharacterList} and list of {@link UserCharacterClientModel} otherwise
     */
    default List<UserCharacterClientModel> packToUserCharacterClientModelList() {
        return null;
    }
}
