package gamemechanics.resources.assets;

import gamemechanics.flyweights.CharacterRace;
import gamemechanics.flyweights.PerkBranch;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.CharacterRole;
import gamemechanics.interfaces.Countable;
import gamemechanics.interfaces.Perk;

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
}
