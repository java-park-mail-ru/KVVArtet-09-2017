package gamemechanics.aliveentities.npcs.ai;

import gamemechanics.battlefield.aliveentitiescontainers.Squad;
import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.battlefield.map.tilesets.FieldOfVision;
import gamemechanics.battlefield.map.tilesets.FoVTileset;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class AI implements DecisionMaker {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer aiID = instanceCounter.getAndIncrement();

    private final AliveEntity npc;

    private final Squad allies;
    private final Squad enemies;

    private final Map<Integer, Integer> aggro = new HashMap<>();
    private final Map<Integer, Integer> gotDamageFrom = new HashMap<>();

    private final Map<Integer, Ability> abilities;

    private final Map<Integer, BehaviorFunction> behaviours;
    private final Integer activeBehaviourID;

    private final BattleMap map;

    interface BehaviorFunction extends Function<AggregatedBattleState, Action> {}

    public static class AggregatedBattleState {
        AliveEntity self;

        final BattleMap map;
        final FieldOfVision fov;

        final Squad allies;
        final Squad enemies;

        final List<Integer> alliesWeaknessOrder;
        final List<Integer> alliesDistanceOrder;

        final List<Integer> enemiesWeaknessOrder;
        final List<Integer> enemiesDistanceOrder;
        final Map<Integer, Integer> aggroMap;
        final Map<Integer, Integer> gotDamageFromMap;

        final Map<Integer, Ability> abilities;

        AggregatedBattleState(@NotNull AliveEntity self, @NotNull BattleMap map,
                              @NotNull FieldOfVision fov, @NotNull Squad allies,
                              @NotNull Squad enemies, @NotNull List<Integer> alliesWeaknessOrder,
                              @NotNull List<Integer> alliesDistanceOrder,
                              @NotNull List<Integer> enemiesWeaknessOrder,
                              @NotNull List<Integer> enemiesDistanceOrder,
                              @NotNull Map<Integer, Integer> aggroMap,
                              @NotNull Map<Integer, Integer> gotDamageFromMap,
                              @NotNull Map<Integer, Ability> abilities) {
            this.self = self;
            this.map = map;
            this.fov = fov;
            this.allies = allies;
            this.enemies = enemies;
            this.alliesWeaknessOrder = alliesWeaknessOrder;
            this.alliesDistanceOrder = alliesDistanceOrder;
            this.enemiesWeaknessOrder = enemiesWeaknessOrder;
            this.enemiesDistanceOrder = enemiesDistanceOrder;
            this.aggroMap = aggroMap;
            this.gotDamageFromMap = gotDamageFromMap;
            this.abilities = abilities;
        }
    }

    public AI(@NotNull AliveEntity npc, @NotNull Squad allies,
              @NotNull Squad enemies, @NotNull BattleMap map,
              @NotNull Map<Integer, Ability> abilities,
              @NotNull Map<Integer, BehaviorFunction> behaviours,
              Integer activeBehaviourID) {
        this.npc = npc;
        this.allies = allies;
        this.enemies = enemies;
        this.map = map;
        this.abilities = abilities;
        this.behaviours = behaviours;
        this.activeBehaviourID = activeBehaviourID;

        initializeMap(aggro);
        initializeMap(gotDamageFrom);
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return aiID;
    }

    @Override
    public Action makeDecision() {
       return behaviours.get(activeBehaviourID).apply(aggregateBattleState());
    }

    public void updateAggro(Integer enemyID, Integer amount) {
        if (!aggro.containsKey(enemyID)) {
            return;
        }
        aggro.replace(enemyID, aggro.get(enemyID) + amount);
    }

    private AggregatedBattleState aggregateBattleState() {
        return new AggregatedBattleState(npc, map, lookAround(), allies, enemies,
                sortSquadByHitpoints(allies), sortSquadByDistance(allies),
                sortSquadByHitpoints(enemies), sortSquadByDistance(enemies),
                aggro, gotDamageFrom, abilities);
    }

    private FieldOfVision lookAround() {
        return new FoVTileset(map.getTile(npc.getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.ROW_COORD_INDEX),
                npc.getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.COL_COORD_INDEX)));
    }

    private List<Integer> sortSquadByHitpoints(@NotNull Squad squad) {
        if (squad.areAllDead()) {
            return null;
        }

        List<Integer> unitsWeaknessOrder = new ArrayList<>();
        Set<Integer> uncheckedMembers = getAliveMembersIndices(squad);

        while (!uncheckedMembers.isEmpty()) {
            Integer minimalHitpoints = Integer.MAX_VALUE;
            Integer weakestMemberIndex = squad.getSquadSize();

            for (Integer memberIndex : uncheckedMembers) {
                if (!uncheckedMembers.contains(memberIndex) && squad.getMember(memberIndex).isAlive()) {
                    Integer memberHitpoints = squad.getMember(memberIndex)
                            .getProperty(PropertyCategories.PC_HITPOINTS, DigitsPairIndices.CURRENT_VALUE_INDEX);
                    if (minimalHitpoints > memberHitpoints) {
                        weakestMemberIndex = memberIndex;
                        minimalHitpoints = memberHitpoints;
                    }
                }
            }
            if (!Objects.equals(weakestMemberIndex, squad.getSquadSize())) {
                uncheckedMembers.remove(weakestMemberIndex);
                unitsWeaknessOrder.add(weakestMemberIndex);
            }
        }
        return unitsWeaknessOrder;
    }

    private List<Integer> sortSquadByDistance(@NotNull Squad squad) {
        if(squad.areAllDead()) {
            return null;
        }

        List<Integer> unitsDistanceOrder = new ArrayList<>();
        Set<Integer> uncheckedUnits = getAliveMembersIndices(squad);

        while (!uncheckedUnits.isEmpty()) {
            Integer closestMemberIndex = squad.getSquadSize();
            Integer minimalDistance = Integer.MAX_VALUE;

            MapNode npcTile = map.getTile(npc.getProperty(PropertyCategories.PC_COORDINATES,
                    DigitsPairIndices.ROW_COORD_INDEX), npc.getProperty(PropertyCategories.PC_COORDINATES,
                    DigitsPairIndices.COL_COORD_INDEX));
            for (Integer memberIndex : uncheckedUnits) {
                if (npc.equals(squad.getMember(memberIndex))) {
                    closestMemberIndex = memberIndex;
                    break;
                }
                AliveEntity member = squad.getMember(memberIndex);
                MapNode memberTile = map.getTile(member.getProperty(PropertyCategories.PC_COORDINATES,
                        DigitsPairIndices.ROW_COORD_INDEX), member.getProperty(PropertyCategories.PC_COORDINATES,
                        DigitsPairIndices.COL_COORD_INDEX));
                if (minimalDistance > npcTile.getH(memberTile)) {
                    minimalDistance = npcTile.getH(memberTile);
                    closestMemberIndex = memberIndex;
                }
            }
            if (!Objects.equals(closestMemberIndex, squad.getSquadSize())) {
                uncheckedUnits.remove(closestMemberIndex);
                if (npc.equals(squad.getMember(closestMemberIndex))) {
                    unitsDistanceOrder.add(0, closestMemberIndex);
                } else {
                    unitsDistanceOrder.add(closestMemberIndex);
                }
            }
        }

        return unitsDistanceOrder;
    }

    private Set<Integer> getAliveMembersIndices(@NotNull Squad squad) {
        Set<Integer> aliveMembersSet = new HashSet<>();

        for (Integer memberIndex = 0; memberIndex < squad.getSquadSize(); ++memberIndex) {
            if (squad.getMember(memberIndex).isAlive()) {
                aliveMembersSet.add(memberIndex);
            }
        }

        return aliveMembersSet;
    }

    private void initializeMap(Map<Integer, Integer> map) {
        for (Integer memberIndex = 0; memberIndex < enemies.getSquadSize(); ++memberIndex) {
            AliveEntity member = enemies.getMember(memberIndex);
            if (member != null) {
                if (member.isAlive()) {
                    map.put(member.getID(), 0);
                }
            }
        }
    }
}
