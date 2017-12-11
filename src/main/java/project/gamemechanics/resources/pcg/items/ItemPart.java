package project.gamemechanics.resources.pcg.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.interfaces.AffectorProvider;
import project.gamemechanics.interfaces.GameEntity;
import project.gamemechanics.interfaces.PropertyProvider;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(ItemPartAsset.class),
})
public interface ItemPart extends GameEntity, AffectorProvider, PropertyProvider {
    int FIRST_PART_ID = 0;
    int SECOND_PART_ID = 1;
    int THIRD_PART_ID = 2;
    int ITEM_PARTS_COUNT = 3;

    @JsonIgnore
    Integer getLevel();

    Integer getPartIndex();

    Map<Integer, Affector> getAllAffectors();

    Map<Integer, Property> getAllProperties();
}
