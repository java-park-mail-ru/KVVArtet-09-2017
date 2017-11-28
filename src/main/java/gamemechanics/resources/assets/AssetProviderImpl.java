package gamemechanics.resources.assets;

import gamemechanics.flyweights.CharacterRace;
import gamemechanics.flyweights.PerkBranch;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.CharacterRole;
import gamemechanics.interfaces.Perk;

import javax.validation.constraints.NotNull;
import java.util.List;

public class AssetProviderImpl implements AssetProvider {
    private final AssetHolder.PerkHolder perkHolder;
    private final AssetHolder.PerkBranchHolder perkBranchHolder;
    private final AssetHolder.CharacterRaceHolder characterRaceHolder;

    private final AssetHolder.AbilityHolder abilityHolder;

    private final AssetHolder.CharacterClassHolder characterClassHolder;

    private final AssetHolder.NpcRoleHolder npcRoleHolder;

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
}
