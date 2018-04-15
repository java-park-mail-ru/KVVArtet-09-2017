package project.gamemechanics.charlist;

import org.springframework.stereotype.Component;
import project.gamemechanics.aliveentities.AbstractAliveEntity;
import project.gamemechanics.globals.ClassDescription;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.assets.DummiesFactory;
import project.gamemechanics.services.interfaces.CharacterListDAO;
import project.gamemechanics.services.interfaces.UserCharacterDAO;
import project.gamemechanics.smartcontroller.SmartController;
import project.gamemechanics.world.config.ResourcesConfig;
import project.websocket.messages.charlist.CreateCharacterRequestMessage;
import project.websocket.messages.charlist.DeleteCharacterRequestMessage;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Component
public class UserCharacterFactoryImpl implements UserCharacterFactory {


    private final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
    private final DummiesFactory dummiesFactory = new DummiesFactory(assets);
    private final @NotNull CharacterListDAO characterListDAO;
    private final @NotNull UserCharacterDAO userCharacterDAO;

    public UserCharacterFactoryImpl(@NotNull CharacterListDAO characterListDAO, @NotNull UserCharacterDAO userCharacterDAO) {
        this.characterListDAO = characterListDAO;
        this.userCharacterDAO = userCharacterDAO;
    }

    @Override
    public void createUserCharacter(CreateCharacterRequestMessage requestMessage, SmartController activeSmart) {
        Integer classId = ClassDescription.valueOf(requestMessage.getRole()).asInt();
        Integer raceId = ClassDescription.valueOf(requestMessage.getRace()).asInt();
        final AbstractAliveEntity.UserCharacterModel firstUserCharacterModel = dummiesFactory
                .makeNewDummyModel(classId, raceId, requestMessage.getName(), "");

        Integer characterId = userCharacterDAO.setUserCharacter(firstUserCharacterModel);

        Integer characterListId = Objects.requireNonNull(activeSmart.getCharacterList()).getID();
        characterListDAO.setCharacterInCharacterList(characterListId, characterId);

        Objects.requireNonNull(activeSmart.getCharacterList()).createCharacter(firstUserCharacterModel);
    }

    @Override
    public void deleteUserCharacter(DeleteCharacterRequestMessage requestMessage, SmartController activeSmart) {
        Integer characterIndexInCharacterList = requestMessage.getCharIndexInCharist();
        Integer characterListId = Objects.requireNonNull(activeSmart.getCharacterList()).getID();
        Integer characterId = activeSmart.getCharacterList().getCharacterList().get(characterIndexInCharacterList).getID();

        userCharacterDAO.deleteUserCharacter(characterId);

        characterListDAO.deleteCharacterFromCharacterList(characterListId, characterId);

        activeSmart.getCharacterList().deleteCharacter(characterIndexInCharacterList);
    }
}
