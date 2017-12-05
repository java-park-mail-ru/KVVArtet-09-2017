package project.gamemechanics.resources.assets;

import project.gamemechanics.flyweights.CharacterRace;
import project.gamemechanics.flyweights.PerkBranch;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.CharacterRole;
import project.gamemechanics.interfaces.Perk;

import javax.validation.constraints.NotNull;

public interface AssetProvider {
    int PERK_RESOURCE_NAME = 0;
    int PERK_BRANCH_RESOURCE_NAME = 1;
    int CHARACTER_RACE_RESOURCE_NAME = 2;

    int ABILITY_RESOURCE_NAME = 3;

    int CHARACTER_CLASS_RESOURCE_NAME = 4;

    int NPC_ROLE_RESOURCE_NAME = 5;

    Perk getPerk(@NotNull Integer perkId);
    PerkBranch getPerkBranch(@NotNull Integer branchId);

    Ability getAbility(@NotNull Integer abilityId);

    CharacterRace getCharacterRace(@NotNull Integer raceId);
    CharacterRole getCharacterClass(@NotNull Integer classId);

    CharacterRole getNpcRole(@NotNull Integer roleId);
}
