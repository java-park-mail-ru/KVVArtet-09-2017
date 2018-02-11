package project.gamemechanics.resources.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.aliveentities.AbstractAliveEntity;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.components.properties.MapProperty;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.Bag;
import project.gamemechanics.interfaces.CharacterRole;
import project.gamemechanics.items.containers.CharacterDoll;
import project.gamemechanics.items.containers.StorageBag;
import project.gamemechanics.resources.models.NewCharacterModel;
import project.gamemechanics.world.config.ResourcesConfig;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

public class DummiesFactory {
    private final AssetProvider assets;

    public DummiesFactory(@NotNull AssetProvider assets) {
        this.assets = assets;
    }

    public @Nullable AliveEntity makeNewDummy(@NotNull Integer classId, @NotNull String name,
                                              @NotNull String description) {
        final ObjectMapper mapper = new ObjectMapper();
        NewCharacterModel dummyProps = null;
        // noinspection OverlyBroadCatchBlock
        try {
            dummyProps = mapper.readValue(Resources.getResource(
                    ResourcesConfig.getNewCharacterPropsName()), NewCharacterModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final CharacterRole role = assets.getCharacterClass(classId);
        if (dummyProps == null || role == null) {
            return null;
        }

        final List<Bag> bags = new ArrayList<>();
        final Integer bagsCapacity = 16;
        for (Integer i = 0; i < Constants.DEFAULT_PERSONAL_REWARD_BAG_SIZE; ++i) {
            bags.add(new StorageBag(new StorageBag.EmptyBagModel("Common bag",
                    "Common 16-slot bag", bagsCapacity)));
        }
        final Integer raceId = dummyProps.getProperties().get(PropertyCategories.PC_CHARACTER_RACE_ID).getProperty();
        dummyProps.getProperties().remove(PropertyCategories.PC_CHARACTER_RACE_ID);
        dummyProps.getProperties().put(PropertyCategories.PC_ACTIVE_ROLE,
                new SingleValueProperty(new ArrayList<>(Objects.requireNonNull(role.getAvailableRoles())).get(0)));
        dummyProps.getProperties().put(PropertyCategories.PC_ABILITIES_COOLDOWN,
                new MapProperty(initAbilitiesCooldown(role)));

        final AbstractAliveEntity.UserCharacterModel model =
                new AbstractAliveEntity.UserCharacterModel(Constants.UNDEFINED_ID, name, description,
                        dummyProps.getProperties(), bags, role, assets.getCharacterRace(raceId),
                        new CharacterDoll(), initPerkRanks(role));
        return new UserCharacter(model);
    }

    private Map<Integer, Map<Integer, Integer>> initPerkRanks(@NotNull CharacterRole role) {
        final Map<Integer, Map<Integer, Integer>> perkRanks = new HashMap<>();
        for (Integer branchId : Objects.requireNonNull(role.getPerkBranchesIds())) {
            perkRanks.put(branchId, new HashMap<>());
            final Set<Integer> perkIds = role.getBranchPerksIds(branchId);
            for (Integer perkId : Objects.requireNonNull(perkIds)) {
                perkRanks.get(branchId).put(perkId, 0);
            }
        }
        return perkRanks;
    }

    private Map<Integer, Integer> initAbilitiesCooldown(@NotNull CharacterRole role) {
        final Map<Integer, Integer> cooldowns = new HashMap<>();
        for (Integer abilityId : role.getAllAbilities().keySet()) {
            cooldowns.put(abilityId, 0);
        }
        return cooldowns;
    }
}
