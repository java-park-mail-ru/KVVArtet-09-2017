package project.gamemechanics.interfaces;

import project.gamemechanics.aliveentities.AbstractAliveEntity.UserCharacterModel;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;

/**
 * interface for charlist {@link CharacterList}
 * Implementations shall always provide access to the {@link CharacterList}.
 */
public interface Charlist extends Countable {
    /**
     * create a {@link UserCharacter} and add him in {@link CharacterList}
     *
     * @param userCharacterModel that need to be fulfill
     * @return null if there's no {@link UserCharacterModel} in @param or {@link UserCharacter} otherwise
     */
    default UserCharacter createChar(UserCharacterModel userCharacterModel) {
        return null;
    }

    /**
     * delete a character from {@link CharacterList}
     *
     * @param index of {@link UserCharacter} in {@link CharacterList}
     */
    default void deleteChar(Integer index) {

    }

    /**
     * get ID of an owner of a {@link CharacterList}
     *
     * @return null if there's empty ownerID in {@link CharacterList} and ownerID otherwise
     */
    default Integer getOwnerId() {
        return 0;
    }

}
