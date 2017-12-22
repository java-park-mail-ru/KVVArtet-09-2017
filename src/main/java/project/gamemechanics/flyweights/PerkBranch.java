package project.gamemechanics.flyweights;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    public Integer getInstancesCount() {
        return 0;
    }

    @Override
    public Integer getID() {
        return branchID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @SuppressWarnings("ConstantConditions")
    public Perk getPerk(Integer perkIndex) {
        if (perkIndex < 0 || perkIndex >= perks.size()) {
            return null;
        }
        return perks.get(perkIndex);
    }

    @JsonIgnore
    public Set<Integer> getPerkIds() {
        final Set<Integer> perkIds = new HashSet<>();
        for (Perk perk : perks) {
            perkIds.add(perk.getID());
        }
        return perkIds;
    }
}
