package project.gamemechanics.resources.assets;

import project.gamemechanics.flyweights.CharacterRace;
import project.gamemechanics.flyweights.PerkBranch;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.CharacterRole;
import project.gamemechanics.interfaces.Perk;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface AssetProvider {
    int PERK_RESOURCE_NAME = 0;
    int PERK_BRANCH_RESOURCE_NAME = 1;
    int CHARACTER_RACE_RESOURCE_NAME = 2;

    int ABILITY_RESOURCE_NAME = 3;

    int CHARACTER_CLASS_RESOURCE_NAME = 4;

    int NPC_ROLE_RESOURCE_NAME = 5;
    @SuppressWarnings("FieldNamingConvention")
    int INSTANCE_NAME_DESCRIPTION_FIRST_RESOURCE_NAME = 6;
    @SuppressWarnings("FieldNamingConvention")
    int INSTANCE_NAME_DESCRIPTION_SECOND_RESOURCE_NAME = 7;

    int ASSET_HOLDERS_FILES_COUNT = 8;

    Perk getPerk(@NotNull Integer perkId);

    PerkBranch getPerkBranch(@NotNull Integer branchId);

    Ability getAbility(@NotNull Integer abilityId);

    CharacterRace getCharacterRace(@NotNull Integer raceId);

    CharacterRace getCharacterRace();

    CharacterRole getCharacterClass(@NotNull Integer classId);

    CharacterRole getNpcRole(@NotNull Integer roleId);

    CharacterRole getNpcRole();

    List<String> makeInstanceNameDescription();

    void reset();
}
