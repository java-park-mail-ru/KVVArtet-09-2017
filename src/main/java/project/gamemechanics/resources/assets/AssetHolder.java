package project.gamemechanics.resources.assets;

import org.jetbrains.annotations.Nullable;
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

@SuppressWarnings("unused")
public interface AssetHolder<T extends Countable> {
    @Nullable T getAsset(@NotNull Integer assetIndex);

    @NotNull Boolean hasAsset(@NotNull Integer assetIndex);

    @NotNull Set<Integer> getAvailableAssets();

    @NotNull Map<Integer, T> getAllAssets();

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
