package project.gamemechanics.aliveentities.npcs.ai;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import project.gamemechanics.battlefield.map.tilesets.FieldOfVision;
import project.gamemechanics.battlefield.map.tilesets.FoVTileset;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@SuppressWarnings("ALL")
public class AI implements DecisionMaker {
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer aiID = INSTANCE_COUNTER.getAndIncrement();

    private final AliveEntity npc;

    private final Squad allies;
    private final Squad enemies;

    private final Map<Integer, Integer> aggro = new HashMap<>();
    private final Map<Integer, Integer> gotDamageFrom = new HashMap<>();

    private final Map<Integer, Ability> abilities;

    private final Map<Integer, BehaviorFunction> behaviours;
    private final Integer activeBehaviourID;

    private final BattleMap map;
    private final PathfindingAlgorithm pathfinder;

    public interface BehaviorFunction extends Function<AggregatedBattleState, Action> {
    }

    static class AggregatedBattleState {
        // CHECKSTYLE:OFF
        final AliveEntity self;

        final BattleMap map;
        final PathfindingAlgorithm pathfinder;
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
                              @NotNull PathfindingAlgorithm pathfinder,
                              @NotNull FieldOfVision fov, @NotNull Squad allies,
                              @NotNull Squad enemies, @NotNull List<Integer> alliesWeaknessOrder,
                              @NotNull List<Integer> alliesDistanceOrder,
                              @NotNull List<Integer> enemiesWeaknessOrder,
                              @NotNull List<Integer> enemiesDistanceOrder,
                              @NotNull Map<Integer, Integer> aggroMap,
                              @NotNull Map<Integer, Integer> gotDamageFromMap,
                              @NotNull Map<Integer, Ability> abilities) {
            // CHECKSTYLE:ON
            this.self = self;
            this.map = map;
            this.pathfinder = pathfinder;
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
    // CHECKSTYLE:OFF

    public AI(@NotNull AliveEntity npc, @NotNull Squad allies,
              @NotNull Squad enemies, @NotNull BattleMap map,
              @NotNull PathfindingAlgorithm pathfinder,
              @NotNull Map<Integer, Ability> abilities,
              @NotNull Map<Integer, BehaviorFunction> behaviours,
              @NotNull Integer activeBehaviourID) {
        this.npc = npc;
        this.allies = allies;
        this.enemies = enemies;
        this.map = map;
        this.pathfinder = pathfinder;
        this.abilities = abilities;
        this.behaviours = behaviours;
        this.activeBehaviourID = activeBehaviourID;

        initializeMap(aggro);
        initializeMap(gotDamageFrom);
    }
    // CHECKSTYLE:ON

    @Override
    public @NotNull Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public @NotNull Integer getID() {
        return aiID;
    }

    @Override
    public @NotNull Action makeDecision() {
        return behaviours.get(activeBehaviourID).apply(aggregateBattleState());
    }

    @Override
    public void updateAggro(@NotNull Integer enemyID, @NotNull Integer amount) {
        if (!aggro.containsKey(enemyID)) {
            return;
        }
        aggro.replace(enemyID, aggro.get(enemyID) + amount);
    }

    @Override
    public void updateDamageFrom(@NotNull Integer enemyID, @NotNull Integer amount) {
        if (!gotDamageFrom.containsKey(enemyID)) {
            return;
        }
        gotDamageFrom.replace(enemyID, gotDamageFrom.get(enemyID) + amount);
    }

    private @NotNull AggregatedBattleState aggregateBattleState() {
        return new AggregatedBattleState(npc, map, pathfinder, lookAround(), allies, enemies,
                sortSquadByHitpoints(allies), sortSquadByDistance(allies),
                sortSquadByHitpoints(enemies), sortSquadByDistance(enemies),
                aggro, gotDamageFrom, abilities);
    }

    private @NotNull FieldOfVision lookAround() {
        return new FoVTileset(map.getTile(npc.getProperty(PropertyCategories.PC_COORDINATES,
                DigitsPairIndices.ROW_COORD_INDEX), npc.getProperty(PropertyCategories.PC_COORDINATES,
                DigitsPairIndices.COL_COORD_INDEX)), map);
    }

    private @Nullable List<Integer> sortSquadByHitpoints(@NotNull Squad squad) {
        if (squad.areAllDead() || squad.getSquadSize() == 0) {
            return null;
        }

        final List<Integer> unitsWeaknessOrder = new ArrayList<>();
        final Set<Integer> uncheckedMembers = getAliveMembersIndices(squad);

        while (!uncheckedMembers.isEmpty()) {
            Integer minimalHitpoints = Integer.MAX_VALUE;
            Integer weakestMemberIndex = squad.getSquadSize();

            for (Integer memberIndex : uncheckedMembers) {
                if (squad.getMember(memberIndex).isAlive()) {
                    final Integer memberHitpoints = squad.getMember(memberIndex)
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

    private @Nullable List<Integer> sortSquadByDistance(@NotNull Squad squad) {
        if (squad.areAllDead() || squad.getSquadSize() == 0) {
            return null;
        }

        final List<Integer> unitsDistanceOrder = new ArrayList<>();
        final Set<Integer> uncheckedUnits = getAliveMembersIndices(squad);

        while (!uncheckedUnits.isEmpty()) {
            Integer closestMemberIndex = squad.getSquadSize();
            Integer minimalDistance = Integer.MAX_VALUE;

            final MapNode npcTile = map.getTile(npc.getProperty(PropertyCategories.PC_COORDINATES,
                    DigitsPairIndices.ROW_COORD_INDEX), npc.getProperty(PropertyCategories.PC_COORDINATES,
                    DigitsPairIndices.COL_COORD_INDEX));
            for (Integer memberIndex : uncheckedUnits) {
                if (npc.equals(squad.getMember(memberIndex))) {
                    closestMemberIndex = memberIndex;
                    break;
                }
                final AliveEntity member = squad.getMember(memberIndex);
                final MapNode memberTile = map.getTile(member.getProperty(PropertyCategories.PC_COORDINATES,
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

    private @Nullable Set<Integer> getAliveMembersIndices(@NotNull Squad squad) {
        final Set<Integer> aliveMembersSet = new HashSet<>();

        for (Integer memberIndex = 0; memberIndex < squad.getSquadSize(); ++memberIndex) {
            if (squad.getMember(memberIndex).isAlive()) {
                aliveMembersSet.add(memberIndex);
            }
        }

        return aliveMembersSet;
    }
    // CHECKSTYLE:OFF
    @SuppressWarnings("ParameterHidesMemberVariable")
    private void initializeMap(@NotNull Map<Integer, Integer> map) {
    // CHECKSTYLE:ON
        for (Integer memberIndex = 0; memberIndex < enemies.getSquadSize(); ++memberIndex) {
            final AliveEntity member = enemies.getMember(memberIndex);
            if (member != null) {
                if (member.isAlive()) {
                    map.put(member.getID(), 0);
                }
            }
        }
    }
}
