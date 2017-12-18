package project.gamemechanics.resources.assets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import project.gamemechanics.flyweights.perks.IngamePerk;
import project.gamemechanics.interfaces.Perk;
import project.gamemechanics.resources.holders.GameResourceHolder;
import project.gamemechanics.resources.holders.ResourceHolder;
import project.gamemechanics.resources.models.GameResource;

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
        final ObjectMapper mapper = new ObjectMapper();
        final Map<Integer, GameResource> readPerks = new HashMap<>();
        //noinspection Duplicates,TryWithIdenticalCatches
        try {
            final ResourceHolder holder = mapper.readValue(Resources.getResource(fileName), GameResourceHolder.class);
            readPerks.putAll(holder.getAllResources());
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Integer perkId : readPerks.keySet()) {
            final GameResource perkResource = readPerks.get(perkId);
            final IngamePerk.PerkModel model = new IngamePerk.PerkModel(perkId, perkResource.getName(),
                    perkResource.getDescription(), perkResource.getAllAffectors());
            assets.put(model.id, new IngamePerk(model));
        }
    }
}
