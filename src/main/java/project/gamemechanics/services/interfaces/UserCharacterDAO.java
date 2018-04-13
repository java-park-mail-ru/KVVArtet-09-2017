package project.gamemechanics.services.interfaces;

import project.gamemechanics.aliveentities.AbstractAliveEntity;

import javax.validation.constraints.NotNull;

public interface UserCharacterDAO {
    AbstractAliveEntity.UserCharacterModel getUserCharacterById(@NotNull  Integer id);

    @NotNull
    Integer setUserCharacter(AbstractAliveEntity.UserCharacterModel userCharacterModel);

    void updateUserCharacter(Integer id, AbstractAliveEntity.UserCharacterModel newUserCharacterModel);

    void deleteUserCharacter(Integer id);

}
