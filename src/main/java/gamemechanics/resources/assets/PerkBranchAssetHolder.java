package gamemechanics.resources.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import gamemechanics.flyweights.PerkBranch;
import gamemechanics.globals.MappingIndices;
import gamemechanics.interfaces.Perk;
import gamemechanics.resources.holders.GameResourceHolder;
import gamemechanics.resources.holders.ResourceHolder;
import gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerkBranchAssetHolder extends AbstractAssetHolder<PerkBranch> implements AssetHolder.PerkBranchHolder {
    public PerkBranchAssetHolder(@NotNull String fileName, @NotNull Map<Integer, Perk> perks) {
        super();
        fillFromFile(fileName, perks);
    }

    private void fillFromFile(@NotNull String fillName, @NotNull Map<Integer, Perk> perks) {
        Map<Integer, GameResource> resourceData = readFromFile(fillName);
        for (Integer branchId : resourceData.keySet()) {
            List<Perk> branchPerks = new ArrayList<>();
            GameResource branchResource = resourceData.get(branchId);
            List<Integer> perkIds = branchResource.getMapping(MappingIndices.PERKS_MAPPING);
            for (Integer perkId : perkIds) {
                Perk perk = perks.getOrDefault(perkId, null);
                if (perk != null && !branchPerks.contains(perk)) {
                    branchPerks.add(perk);
                }
            }
            PerkBranch.PerkBranchModel model = new PerkBranch.PerkBranchModel(branchId, branchResource.getName(),
                    branchResource.getDescription(), branchPerks);
            assets.put(model.id, new PerkBranch(model));
        }
    }

    private Map<Integer, GameResource> readFromFile(@NotNull String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, GameResource> readData = new HashMap<>();
        try {
            ResourceHolder holder = mapper.readValue(new File(fileName), GameResourceHolder.class);
            readData.putAll(holder.getAllResources());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readData;
    }
}
