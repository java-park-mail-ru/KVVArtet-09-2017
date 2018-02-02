package project.gamemechanics.interfaces;

import project.gamemechanics.aliveentities.AbstractAliveEntity.UserCharacterModel;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;

import javax.validation.constraints.NotNull;

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
    default @NotNull UserCharacter createChar(@NotNull UserCharacterModel userCharacterModel) {
        return null;
    }

    /**
     * delete a character from {@link CharacterList}.
     *
     * @param index of {@link UserCharacter} in {@link CharacterList}
     */
    default void deleteChar(@NotNull Integer index) {

    }

    /**
     * get ID of an owner of a {@link CharacterList}.
     *
     * @return null if there's empty ownerID in {@link CharacterList} and ownerID otherwise
     */
    default @NotNull Integer getOwnerId() {
        return 0;
    }

}
