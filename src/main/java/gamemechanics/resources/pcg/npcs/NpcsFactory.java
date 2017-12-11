package gamemechanics.resources.pcg.npcs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.resources.assets.AssetProvider;
import gamemechanics.resources.pcg.items.ItemsFactory;

import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(NpcsFactoryImpl.class),
})
public interface NpcsFactory {
    AliveEntity makeNpc(@NotNull NpcBlueprint blueprint, @NotNull AssetProvider assetProvider,
                        @NotNull ItemsFactory itemsGenerator);
}
