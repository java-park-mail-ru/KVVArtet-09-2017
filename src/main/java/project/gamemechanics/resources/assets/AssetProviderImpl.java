package project.gamemechanics.resources.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import project.gamemechanics.flyweights.CharacterRace;
import project.gamemechanics.flyweights.PerkBranch;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.CharacterRole;
import project.gamemechanics.interfaces.Perk;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AssetProviderImpl implements AssetProvider {
    private final AssetHolder.PerkHolder perkHolder;
    private final AssetHolder.PerkBranchHolder perkBranchHolder;
    private final AssetHolder.CharacterRaceHolder characterRaceHolder;

    private final AssetHolder.AbilityHolder abilityHolder;

    private final AssetHolder.CharacterClassHolder characterClassHolder;

    private final AssetHolder.NpcRoleHolder npcRoleHolder;

    private final List<AssetHolder.InstanceNameDescriptionHolder> instanceNameDescriptionHolders = new ArrayList<>();

    public AssetProviderImpl(@NotNull List<String> fileNames) {
        perkHolder = new PerkAssetHolder(fileNames.get(PERK_RESOURCE_NAME));
        perkBranchHolder = new PerkBranchAssetHolder(fileNames.get(PERK_BRANCH_RESOURCE_NAME),
                perkHolder.getAllAssets());
        characterRaceHolder = new CharacterRaceAssetHolder(fileNames.get(CHARACTER_RACE_RESOURCE_NAME));
        abilityHolder = new AbilityAssetHolder(fileNames.get(ABILITY_RESOURCE_NAME));
        characterClassHolder = new CharacterClassAssetHolder(fileNames.get(CHARACTER_CLASS_RESOURCE_NAME),
                abilityHolder.getAllAssets(), perkBranchHolder.getAllAssets());
        npcRoleHolder = new NpcRoleAssetHolder(fileNames.get(NPC_ROLE_RESOURCE_NAME),
                abilityHolder.getAllAssets());
        final ObjectMapper mapper = new ObjectMapper();
        //noinspection OverlyBroadCatchBlock
        try {
            instanceNameDescriptionHolders.add(mapper.readValue(Resources.getResource(
                    fileNames.get(INSTANCE_NAME_DESCRIPTION_FIRST_RESOURCE_NAME)),
                    InstanceNameDescriptionAssetHolder.class));
        } catch (IOException e) {
            e.printStackTrace();
            instanceNameDescriptionHolders.add(new InstanceNameDescriptionAssetHolder(new HashMap<>()));
        }
        //noinspection OverlyBroadCatchBlock
        try {
            instanceNameDescriptionHolders.add(mapper.readValue(Resources.getResource(
                    fileNames.get(INSTANCE_NAME_DESCRIPTION_SECOND_RESOURCE_NAME)),
                    InstanceNameDescriptionAssetHolder.class));
        } catch (IOException e) {
            e.printStackTrace();
            instanceNameDescriptionHolders.add(new InstanceNameDescriptionAssetHolder(new HashMap<>()));
        }
    }

    @Override
    public Perk getPerk(@NotNull Integer perkId) {
        return perkHolder.getAsset(perkId);
    }

    @Override
    public PerkBranch getPerkBranch(@NotNull Integer branchId) {
        return perkBranchHolder.getAsset(branchId);
    }

    @Override
    public CharacterRace getCharacterRace(@NotNull Integer raceId) {
        return characterRaceHolder.getAsset(raceId);
    }

    @Override
    public CharacterRace getCharacterRace() {
        final List<Integer> characterRaceKeyList = new ArrayList<>(characterRaceHolder.getAvailableAssets());
        final Random random = new Random(System.currentTimeMillis());
        return getCharacterRace(characterRaceKeyList.get(random.nextInt(characterRaceKeyList.size())));
    }

    @Override
    public Ability getAbility(@NotNull Integer abilityId) {
        return abilityHolder.getAsset(abilityId);
    }

    @Override
    public CharacterRole getCharacterClass(@NotNull Integer classId) {
        return characterClassHolder.getAsset(classId);
    }

    @Override
    public CharacterRole getNpcRole(@NotNull Integer roleId) {
        return npcRoleHolder.getAsset(roleId);
    }

    @Override
    public CharacterRole getNpcRole() {
        final List<Integer> npcRoleKeysList = new ArrayList<>(npcRoleHolder.getAvailableAssets());
        final Random random = new Random(System.currentTimeMillis());
        return getNpcRole(npcRoleKeysList.get(random.nextInt(npcRoleKeysList.size())));
    }

    @Override
    public List<String> makeInstanceNameDescription() {
        final Random random = new Random(System.currentTimeMillis());

        final StringBuilder nameBuilder = new StringBuilder();
        final StringBuilder descriptionBuilder = new StringBuilder();

        for (Integer i = 0; i < DigitsPairIndices.PAIR_SIZE; ++i) {
            final List<Integer> idsList = new ArrayList<>(instanceNameDescriptionHolders.get(i).getAvailableAssets());
            final Integer chosenId = random.nextInt(idsList.size());
            nameBuilder.append(instanceNameDescriptionHolders.get(i).getAsset(chosenId).getName());
            if (i > 0) {
                nameBuilder.append(' ');
            }
            descriptionBuilder.append(instanceNameDescriptionHolders.get(i).getAsset(chosenId).getDescription());
            if (i > 0) {
                descriptionBuilder.append(' ');
            }
        }

        final List<String> nameDescription = new ArrayList<>();
        nameDescription.add(nameBuilder.toString());
        nameDescription.add(descriptionBuilder.toString());
        return nameDescription;
    }

    @Override
    public void reset() {
        instanceNameDescriptionHolders.clear();
    }
}
