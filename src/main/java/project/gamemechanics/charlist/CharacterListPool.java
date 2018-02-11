package project.gamemechanics.charlist;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

@SuppressWarnings({"EmptyMethod", "unused"})
interface CharacterListPool {
    @Nullable CharacterList getCharacterList(@NotNull Integer ownerID);

    void initCharacterList(@NotNull Integer ownerID);
}
