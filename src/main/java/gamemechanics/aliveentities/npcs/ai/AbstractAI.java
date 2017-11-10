package gamemechanics.aliveentities.npcs.ai;

import gamemechanics.battlefield.aliveentitiescontainers.Squad;
import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.battlefield.map.tilesets.FieldOfVision;
import gamemechanics.battlefield.map.tilesets.FoVTileset;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.Action;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.DecisionMaker;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AbstractAI implements DecisionMaker {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer aiID = instanceCounter.getAndIncrement();

    private final AliveEntity npc;

    private final Squad allies;
    private final Squad enemies;

    private final BattleMap map;

    public AbstractAI(@NotNull AliveEntity npc, @NotNull Squad allies,
                      @NotNull Squad enemies, @NotNull BattleMap map) {
        this.npc = npc;
        this.allies = allies;
        this.enemies = enemies;
        this.map = map;
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
        Action action = null;

        return action;
    }

    FieldOfVision lookAround() {
        return new FoVTileset(map.getTile(npc.getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.ROW_COORD_INDEX),
                npc.getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.COL_COORD_INDEX)));
    }

    List<Integer> sortSquadByHitpoints(@NotNull Squad squad) {
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

    List<Integer> sortSquadByDistance(@NotNull Squad squad) {
        if(squad.areAllDead()) {
            return null;
        }

        List<Integer> unitsDistanceOrder = new ArrayList<>();
        Set<Integer> uncheckedUnits = getAliveMembersIndices(squad);

        while (!uncheckedUnits.isEmpty()) {
            Integer closestMemberIndex = squad.getSquadSize();
            Integer minimalDistance = Integer.MAX_VALUE;

            for (Integer memberIndex : uncheckedUnits) {
                if (npc.equals(squad.getMember(memberIndex))) {
                    closestMemberIndex = memberIndex;
                    break;
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
}
