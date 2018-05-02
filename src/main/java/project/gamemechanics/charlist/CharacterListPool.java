package project.gamemechanics.charlist;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

/**
 * interface for characterListPoolService {@link CharacterListPoolService}.
 * Implementations shall always provide access to the {@link CharacterListPoolService}.
 */
public interface CharacterListPool {
    /**
     * get a {@link Charlist} from ownerID to CharacterList map.
     *
     * @param ownerId that need to be fulfill
     * @return null if there's no {@link CharacterList} with @param in map or {@link Charlist} otherwise
     */
    @SuppressWarnings("unused")
    @Nullable Charlist getCharacterList(@NotNull Integer ownerId);

    /**
     * initialize a {@link CharacterList} from database.
     *
     * @param ownerId that need to be fulfill
     * @return null if there's no {@link CharacterList} with @param in db or {@link Charlist} otherwise
     */
    Charlist initCharacterList(@NotNull Integer ownerId);


    /**
     * delete a {@link CharacterList} from map.
     *
     * @param ownerId that need to be fulfill
     */
    void deleteCharacterList(@NotNull Integer ownerId);

}
