package project.gamemechanics.resources.pcg.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(ItemFactoryImpl.class),
})
public interface ItemsFactory {
    EquipableItem makeItem(@NotNull ItemBlueprint blueprint);
}
