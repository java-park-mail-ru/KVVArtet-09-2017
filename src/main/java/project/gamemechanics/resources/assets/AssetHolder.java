package project.gamemechanics.resources.assets;

import project.gamemechanics.flyweights.CharacterRace;
import project.gamemechanics.flyweights.PerkBranch;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.CharacterRole;
import project.gamemechanics.interfaces.Countable;
import project.gamemechanics.interfaces.Perk;
import project.gamemechanics.resources.models.InstanceNameDescription;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public interface AssetHolder<T extends Countable> {
    T getAsset(@NotNull Integer assetIndex);

    Boolean hasAsset(@NotNull Integer assetIndex);

    Set<Integer> getAvailableAssets();

    Map<Integer, T> getAllAssets();

    interface PerkHolder extends AssetHolder<Perk> {
    }

    interface PerkBranchHolder extends AssetHolder<PerkBranch> {
    }

    interface AbilityHolder extends AssetHolder<Ability> {
    }

    interface CharacterClassHolder extends AssetHolder<CharacterRole> {
    }

    interface CharacterRaceHolder extends AssetHolder<CharacterRace> {
    }

    interface NpcRoleHolder extends AssetHolder<CharacterRole> {
    }

    interface InstanceNameDescriptionHolder extends  AssetHolder<InstanceNameDescription> {
    }
}
