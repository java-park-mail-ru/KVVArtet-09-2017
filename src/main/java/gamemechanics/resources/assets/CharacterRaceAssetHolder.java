package gamemechanics.resources.assets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gamemechanics.flyweights.CharacterRace;
import gamemechanics.resources.holders.GameResourceHolder;
import gamemechanics.resources.holders.ResourceHolder;
import gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CharacterRaceAssetHolder extends AbstractAssetHolder<CharacterRace>
        implements AssetHolder.CharacterRaceHolder {
    public CharacterRaceAssetHolder(@NotNull String fileName) {
        super();
        fillFromFile(fileName);
    }

    private void fillFromFile(@NotNull String fileName) {
        final ObjectMapper mapper = new ObjectMapper();
        //noinspection TryWithIdenticalCatches
        try {
            final ResourceHolder holder = mapper.readValue(new File(fileName), GameResourceHolder.class);
            final Map<Integer, GameResource> raceResources = holder.getAllResources();
            for (Integer raceId : raceResources.keySet()) {
                final GameResource raceResource = raceResources.get(raceId);
                final CharacterRace.CharacterRaceModel model = new CharacterRace.CharacterRaceModel(raceId,
                        raceResource.getName(), raceResource.getDescription(), raceResource.getAllAffectors());
                assets.put(model.id, new CharacterRace(model));
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
