package project.gamemechanics.world.config;

import project.gamemechanics.resources.assets.AssetProvider;

import java.util.ArrayList;
import java.util.List;

public final class ResourcesConfig {
    private static final List<String> ASSET_HOLDERS_FILE_NAMES = initAssetHoldersFileNames();

    private static final String NPC_PARTS_FILENAME = "npcParts.json";
    private static final String ITEM_PARTS_FILENAME = "itemParts.json";

    private static final String NEW_CHARACTER_PROPS = "newCharacterResource.json";

    private ResourcesConfig() {
    }

    private static List<String> initAssetHoldersFileNames() {
        final List<String> fileNames = new ArrayList<>(AssetProvider.ASSET_HOLDERS_FILES_COUNT);

        fileNames.add(AssetProvider.PERK_RESOURCE_NAME, "perks.json");
        fileNames.add(AssetProvider.PERK_BRANCH_RESOURCE_NAME, "perkBranches.json");
        fileNames.add(AssetProvider.CHARACTER_RACE_RESOURCE_NAME, "characterRaces.json");
        fileNames.add(AssetProvider.ABILITY_RESOURCE_NAME, "abilities.json");
        fileNames.add(AssetProvider.CHARACTER_CLASS_RESOURCE_NAME, "characterClasses.json");
        fileNames.add(AssetProvider.NPC_ROLE_RESOURCE_NAME, "npcRoles.json");
        fileNames.add(AssetProvider.INSTANCE_NAME_DESCRIPTION_FIRST_RESOURCE_NAME,
                "instanceNameDescriptionsOne.json");
        fileNames.add(AssetProvider.INSTANCE_NAME_DESCRIPTION_SECOND_RESOURCE_NAME,
                "instanceNameDescriptionsTwo.json");

        return fileNames;
    }

    public static List<String> getAssetHoldersFileNames() {
        return ASSET_HOLDERS_FILE_NAMES;
    }

    public static String getNpcPartsFilename() {
        return NPC_PARTS_FILENAME;
    }

    public static String getItemPartsFilename() {
        return ITEM_PARTS_FILENAME;
    }

    public static String getNewCharacterPropsName() {
        return NEW_CHARACTER_PROPS;
    }
}
