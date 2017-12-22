package project.gamemechanics.resources.pcg.npcs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.pcg.items.ItemsFactory;

import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(NpcsFactoryImpl.class),
})
public interface NpcsFactory {
    AliveEntity makeNpc(@NotNull NpcBlueprint blueprint, @NotNull AssetProvider assetProvider,
                        @NotNull ItemsFactory itemsGenerator);
}
