package project.gamemechanics.charlist;

public interface CharacterListPool {

    public CharacterList getCharacterList(Integer ownerID);

    public void initCharacterList(Integer ownerID);
}
