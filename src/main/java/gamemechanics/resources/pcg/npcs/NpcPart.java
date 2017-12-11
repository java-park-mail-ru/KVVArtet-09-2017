package gamemechanics.resources.pcg.npcs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gamemechanics.components.properties.Property;
import gamemechanics.interfaces.Levelable;
import gamemechanics.interfaces.PropertyProvider;
import gamemechanics.resources.pcg.items.ItemBlueprint;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(NpcPartAsset.class),
})
public interface NpcPart extends Levelable, PropertyProvider {
    int PREFIX_PART_ID = 0;
    int BODY_PART_ID = 1;
    int NPC_PARTS_COUNT = 2;

    Integer getPartIndex();

    Map<Integer, Property> getAllProperties();

    List<ItemBlueprint> getLootList();
}
