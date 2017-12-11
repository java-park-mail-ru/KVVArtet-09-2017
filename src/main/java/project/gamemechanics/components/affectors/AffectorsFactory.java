package project.gamemechanics.components.affectors;

import project.gamemechanics.globals.CharacterRatings;
import project.gamemechanics.globals.CharacterStats;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.EquipmentKind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class AffectorsFactory {
    private AffectorsFactory() {
    }

    @SuppressWarnings("OverlyComplexMethod")
    public static @Nullable Affector getAffector(@NotNull Integer affectorID) {
        Affector affector = null;
        switch (affectorID) {
            case AffectorCategories.AC_STATS_AFFECTOR:
                affector = makeStatsAffector();
                break;
            case AffectorCategories.AC_RATINGS_AFFECTOR:
                affector = makeRatingsAffector();
                break;
            case AffectorCategories.AC_DAMAGE_AFFECTOR:
                affector = makeDamageAffector();
                break;
            case AffectorCategories.AC_DEFENSE_AFFECTOR:
                affector = makeDefenseAffector();
                break;
            case AffectorCategories.AC_HEALTH_AFFECTOR:
                affector = makeHealthAffector();
                break;
            case AffectorCategories.AC_OVER_TIME_AFFECTOR:
                affector = makeOverTimeAffector();
                break;
            case AffectorCategories.AC_WEAPON_DAMAGE_AFFECTOR:
                affector = makeWeaponDamageAffector();
                break;
            case AffectorCategories.AC_ARMOUR_DEFENSE_AFFECTOR:
                affector = makeArmourDefenseAffector();
                break;
            case AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR:
                affector = makeAbilityHealthAffector();
                break;
            case AffectorCategories.AC_BASE_DAMAGE_ROLE_AFFECTOR:
                affector = makeBaseDamageRoleAffector();
                break;
            case AffectorCategories.AC_BASE_DEFENSE_ROLE_AFFECTOR:
                affector = makeBaseDefenseRoleAffector();
                break;
            default:
                break;
        }
        return affector;
    }

    private static Affector makeStatsAffector() {
        return new ListAffector(new ArrayList<>(CharacterStats.CS_SIZE.asInt()));
    }

    private static Affector makeRatingsAffector() {
        return new ListAffector(new ArrayList<>(CharacterRatings.CR_SIZE.asInt()));
    }

    private static Affector makeDamageAffector() {
        final Map<Integer, Integer> damageAffections = initializeDamageMap();
        return new MapAffector(damageAffections);
    }

    private static Affector makeDefenseAffector() {
        final Map<Integer, Integer> defenseAffections = initializeDefenseMap();
        return new MapAffector(defenseAffections);
    }

    private static Affector makeHealthAffector() {
        return new SingleValueAffector(0);
    }

    private static Affector makeArmourDefenseAffector() {
        return new SingleValueAffector(0);
    }

    private static Affector makeOverTimeAffector() {
        return new ListAffector(new ArrayList<>(DigitsPairIndices.PAIR_SIZE));
    }

    private static Affector makeWeaponDamageAffector() {
        return new ListAffector(new ArrayList<>(DigitsPairIndices.PAIR_SIZE));
    }

    private static Affector makeAbilityHealthAffector() {
        return new ListAffector(new ArrayList<>(DigitsPairIndices.PAIR_SIZE));
    }

    private static Affector makeBaseDamageRoleAffector() {
        return new SingleValueAffector(0);
    }

    private static Affector makeBaseDefenseRoleAffector() {
        return new SingleValueAffector(0);
    }

    private static Map<Integer, Integer> initializeDamageMap() {
        final Map<Integer, Integer> affections = new HashMap<>();
        affections.put(EquipmentKind.EK_SWORD.asInt(), 0);
        affections.put(EquipmentKind.EK_DAGGER.asInt(), 0);
        affections.put(EquipmentKind.EK_AXE.asInt(), 0);
        affections.put(EquipmentKind.EK_MACE.asInt(), 0);
        affections.put(EquipmentKind.EK_SPEAR.asInt(), 0);
        affections.put(EquipmentKind.EK_STAFF.asInt(), 0);
        affections.put(EquipmentKind.EK_BOW.asInt(), 0);
        affections.put(EquipmentKind.EK_CROSSBOW.asInt(), 0);
        return affections;
    }

    private static Map<Integer, Integer> initializeDefenseMap() {
        final Map<Integer, Integer> affections = new HashMap<>();
        affections.put(EquipmentKind.EK_CLOTH_ARMOUR.asInt(), 0);
        affections.put(EquipmentKind.EK_LEATHER_ARMOUR.asInt(), 0);
        affections.put(EquipmentKind.EK_CHAINMAIL_ARMOUR.asInt(), 0);
        affections.put(EquipmentKind.EK_PLATE_ARMOUR.asInt(), 0);
        return affections;
    }
}
