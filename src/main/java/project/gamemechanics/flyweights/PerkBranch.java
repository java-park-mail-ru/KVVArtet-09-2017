package project.gamemechanics.flyweights;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.interfaces.GameEntity;
import project.gamemechanics.interfaces.Perk;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("RedundantSuppression")
public class PerkBranch implements GameEntity {
    private final Integer branchID;

    private final String name;
    private final String description;

    private final List<Perk> perks;

    public static class PerkBranchModel {
        // CHECKSTYLE:OFF
        public final Integer id;
        final String name;
        final String description;
        final List<Perk> perks;
        // CHECKSTYLE:ON

        public PerkBranchModel(@NotNull Integer id, @NotNull String name,
                               @NotNull String description, @NotNull List<Perk> perks) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.perks = perks;
        }
    }

    public PerkBranch(@NotNull PerkBranchModel model) {
        branchID = model.id;
        name = model.name;
        description = model.description;
        perks = model.perks;
    }

    @Override
    public @NotNull Integer getInstancesCount() {
        return 0;
    }

    @Override
    public @NotNull Integer getID() {
        return branchID;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @SuppressWarnings("ConstantConditions")
    public @Nullable Perk getPerk(@NotNull Integer perkIndex) {
        if (perkIndex < 0 || perkIndex >= perks.size()) {
            return null;
        }
        return perks.get(perkIndex);
    }

    @JsonIgnore
    public @NotNull Set<Integer> getPerkIds() {
        final Set<Integer> perkIds = new HashSet<>();
        for (Perk perk : perks) {
            perkIds.add(perk.getID());
        }
        return perkIds;
    }
}
