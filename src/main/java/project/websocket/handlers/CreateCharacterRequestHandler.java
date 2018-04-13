package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.aliveentities.AbstractAliveEntity;
import project.gamemechanics.globals.ClassDescription;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.assets.DummiesFactory;
import project.gamemechanics.services.CharacterListService;
import project.gamemechanics.smartcontroller.SmartController;
import project.gamemechanics.world.World;
import project.gamemechanics.world.config.ResourcesConfig;
import project.states.CharacterListState;
import project.websocket.messages.Message;
import project.websocket.messages.charlist.CreateCharacterRequestMessage;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Component
public class CreateCharacterRequestHandler extends MessageHandler<CreateCharacterRequestMessage> {

    @SuppressWarnings("FieldCanBeLocal")
    private final @NotNull CharacterListState characterListState;
    private final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
    private final DummiesFactory dummiesFactory = new DummiesFactory(assets);
    private final @NotNull SmartController smartController;
    private final @NotNull CharacterListService characterListService;

    public CreateCharacterRequestHandler(@NotNull World world,
                                         @NotNull CharacterListState characterListState, SmartController smartController, @NotNull CharacterListService characterListService) {
        super(CreateCharacterRequestMessage.class);
        this.characterListState = characterListState;
        this.smartController = smartController;
        this.characterListService = characterListService;
    }

    @PostConstruct
    private void init() {
        characterListState.registerHandler(CreateCharacterRequestMessage.class, this);
    }

    @Override
    public Message handle(@NotNull CreateCharacterRequestMessage message, @NotNull Integer forUser) {
        Integer classId = ClassDescription.valueOf(message.getRole()).asInt();
        Integer raceId = ClassDescription.valueOf(message.getRace()).asInt();
        final AbstractAliveEntity.UserCharacterModel firstUserCharacterModel = dummiesFactory
                .makeNewDummyModel(classId, raceId, message.getName(), "");
        characterListService.setCharacterInCharacterList();
        return message;
    }
}
