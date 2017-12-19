package project.gamemechanics.charlist;

@SuppressWarnings("EmptyMethod")
interface CharacterListPool {

    CharacterList getCharacterList(Integer ownerID);

    void initCharacterList(Integer ownerID);
}
