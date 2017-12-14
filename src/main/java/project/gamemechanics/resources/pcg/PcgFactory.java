package project.gamemechanics.resources.pcg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.EquipmentKind;
import project.gamemechanics.globals.ItemRarity;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;
import project.gamemechanics.resources.pcg.items.ItemFactoryImpl;
import project.gamemechanics.resources.pcg.items.ItemPart;
import project.gamemechanics.resources.pcg.items.ItemsFactory;
import project.gamemechanics.resources.pcg.npcs.NpcBlueprint;
import project.gamemechanics.resources.pcg.npcs.NpcPart;
import project.gamemechanics.resources.pcg.npcs.NpcsFactory;
import project.gamemechanics.resources.pcg.npcs.NpcsFactoryImpl;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PcgFactory implements PcgContentFactory {
    private ItemsFactory itemsFactory = null;
    private NpcsFactory npcsFactory = null;
    private final AssetProvider assetProvider;

    public PcgFactory(@NotNull String itemResourcesFname, @NotNull String npcsResourcesFname,
                      @NotNull AssetProvider assetProvider) {
        final ObjectMapper mapper = new ObjectMapper();
        this.assetProvider = assetProvider;
        try {
            itemsFactory = mapper.readValue(Resources.getResource(itemResourcesFname), ItemFactoryImpl.class);
            npcsFactory = mapper.readValue(Resources.getResource(npcsResourcesFname), NpcsFactoryImpl.class);
        } catch (IOException e) {
            e.printStackTrace();
            if (itemsFactory == null) {
                itemsFactory = new ItemFactoryImpl(new HashMap<>());
            }
            if (npcsFactory == null) {
                npcsFactory = new NpcsFactoryImpl(new HashMap<>());
            }
        }
    }

    @Override
    public EquipableItem makeItem(@NotNull ItemBlueprint blueprint) {
        return itemsFactory.makeItem(blueprint);
    }
    @Override
    public EquipableItem makeItem(@NotNull Integer level) {
        final Map<Integer, Integer> parts = new HashMap<>();
        for (Integer i = 0; i < ItemPart.ITEM_PARTS_COUNT; ++i) {
            parts.put(i, Constants.UNDEFINED_ID);
        }
        final Map<Integer, Property> properties = new HashMap<>();
        properties.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(level));
        properties.put(PropertyCategories.PC_ITEM_RARITY,
                new SingleValueProperty(ItemRarity.IR_UNDEFINED.asInt()));
        properties.put(PropertyCategories.PC_ITEM_KIND,
                new SingleValueProperty(EquipmentKind.EK_UNDEFINED.asInt()));
        return itemsFactory.makeItem(new ItemBlueprint(Constants.UNDEFINED_RARITY_DEFAULT_DROP_CHANCE,
                properties, parts));
    }

    @Override
    public AliveEntity makeNpc(@NotNull NpcBlueprint blueprint) {
        return npcsFactory.makeNpc(blueprint, assetProvider, itemsFactory);
    }

    @Override
    public AliveEntity makeNpc(@NotNull Integer level) {
        final Map<Integer, Integer> parts = new HashMap<>();
        for (Integer i = 0; i < NpcPart.NPC_PARTS_COUNT; ++i) {
            parts.put(i, Constants.UNDEFINED_ID);
        }
        final Map<Integer, Property> properties = new HashMap<>();
        properties.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(level));
        properties.put(PropertyCategories.PC_CHARACTER_ROLE_ID,
                new SingleValueProperty(Constants.UNDEFINED_ID));
        return npcsFactory.makeNpc(new NpcBlueprint(properties, parts), assetProvider, itemsFactory);
    }

    @Override
    @JsonIgnore
    public ItemsFactory getItemsFactory() {
        return itemsFactory;
    }

    @Override
    @JsonIgnore
    public NpcsFactory getNpcsFactory() {
        return npcsFactory;
    }

    @Override
    public void reset() {
        assetProvider.reset();
    }
}
