package gamemechanics.resources.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import gamemechanics.flyweights.perks.IngamePerk;
import gamemechanics.interfaces.Perk;
import gamemechanics.resources.holders.GameResourceHolder;
import gamemechanics.resources.holders.ResourceHolder;
import gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PerkAssetHolder extends AbstractAssetHolder<Perk> implements AssetHolder.PerkHolder {
    public PerkAssetHolder(@NotNull String fileName) {
        super();
        fillFromFile(fileName);
    }

    private void fillFromFile(@NotNull String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, GameResource> readPerks = new HashMap<>();
        try {
            ResourceHolder holder = mapper.readValue(new File(fileName), GameResourceHolder.class);
            readPerks.putAll(holder.getAllResources());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Integer perkId : readPerks.keySet()) {
            GameResource perkResource = readPerks.get(perkId);
            IngamePerk.PerkModel model = new IngamePerk.PerkModel(perkId, perkResource.getName(),
                    perkResource.getDescription(), perkResource.getAllAffectors());
            assets.put(model.id, new IngamePerk(model));
        }
    }
}
