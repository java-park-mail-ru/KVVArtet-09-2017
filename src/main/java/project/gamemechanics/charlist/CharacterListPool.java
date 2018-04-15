package project.gamemechanics.charlist;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public interface CharacterListPool {
    @Nullable CharacterList getCharacterList(@NotNull Integer ownerID);

    Charlist initCharacterList(@NotNull Integer ownerId);

    void updateCharacterList(@NotNull Integer ownerId);
}
