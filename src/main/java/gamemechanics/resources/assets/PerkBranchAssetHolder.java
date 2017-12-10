package gamemechanics.resources.assets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import gamemechanics.flyweights.PerkBranch;
import gamemechanics.globals.MappingIndices;
import gamemechanics.interfaces.Perk;
import gamemechanics.resources.holders.GameResourceHolder;
import gamemechanics.resources.holders.ResourceHolder;
import gamemechanics.resources.models.GameResource;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PerkBranchAssetHolder extends AbstractAssetHolder<PerkBranch> implements AssetHolder.PerkBranchHolder {
    public PerkBranchAssetHolder(@NotNull String fileName, @NotNull Map<Integer, Perk> perks) {
        super();
        fillFromFile(fileName, perks);
    }

    private void fillFromFile(@NotNull String fillName, @NotNull Map<Integer, Perk> perks) {
        final Map<Integer, GameResource> resourceData = readFromFile(fillName);
        for (Integer branchId : resourceData.keySet()) {
            final List<Perk> branchPerks = new ArrayList<>();
            final GameResource branchResource = resourceData.get(branchId);
            final List<Integer> perkIds = branchResource.getMapping(MappingIndices.PERKS_MAPPING);
            for (Integer perkId : perkIds) {
                final Perk perk = perks.getOrDefault(perkId, null);
                if (perk != null && !branchPerks.contains(perk)) {
                    branchPerks.add(perk);
                }
            }
            final PerkBranch.PerkBranchModel model = new PerkBranch.PerkBranchModel(branchId, branchResource.getName(),
                    branchResource.getDescription(), branchPerks);
            assets.put(model.id, new PerkBranch(model));
        }
    }

    private Map<Integer, GameResource> readFromFile(@NotNull String fileName) {
        final ObjectMapper mapper = new ObjectMapper();
        final Map<Integer, GameResource> readData = new HashMap<>();
        //noinspection Duplicates,TryWithIdenticalCatches
        try {
            final ResourceHolder holder = mapper.readValue(Resources.getResource(fileName), GameResourceHolder.class);
            readData.putAll(holder.getAllResources());
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readData;
    }
}
