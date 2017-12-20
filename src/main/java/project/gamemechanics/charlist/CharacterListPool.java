package project.gamemechanics.charlist;

@SuppressWarnings({"EmptyMethod", "unused"})
interface CharacterListPool {

    CharacterList getCharacterList(Integer ownerID);

    void initCharacterList(Integer ownerID);
}
