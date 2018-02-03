package project.gamemechanics.aliveentities.npcs.ai;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.battlefield.map.actions.BattleAction;
import project.gamemechanics.battlefield.map.actions.MovementAction;
import project.gamemechanics.battlefield.map.actions.SkipTurnAction;
import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.Action;
import project.gamemechanics.interfaces.AliveEntity;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * static storage class for AI behavior implementation <br/>
 * (note: it's in WIP state at this moment,
 * and requires some refactoring)<br/><br/>
 * --<br/><br/>
 * goal is to make BEHAVIORS JSON-serializable and human-readable to allow in-situ editing.
 */
@SuppressWarnings("UnusedAssignment")
public final class AIBehaviors {
    private static final Map<Integer, AI.BehaviorFunction> BEHAVIORS = initializeBehaviorFunctions();

    private static final int ONE_ACTION_STEP_BONUS = 10;
    private static final int STANDS_ON_ADJACENT_TILE = 17;
    private static final int ONE_STEP_COST = 2;

    private AIBehaviors() {
    }

    /**
     * get an AI behavior by given ID.
     *
     * @param behaviorID ID of the wanted behavior
     * @return null if ID is invalid or {@link AI.BehaviorFunction} registered
     *     under given ID otherwise
     */
    @SuppressWarnings("unused")
    public static @Nullable AI.BehaviorFunction getBehavior(@NotNull Integer behaviorID) {
        return BEHAVIORS.getOrDefault(behaviorID, null);
    }

    public static @NotNull Map<Integer, AI.BehaviorFunction> getAllBehaviors() {
        return BEHAVIORS;
    }
    // CHECKSTYLE:OFF
    /* TODO: find a better way to write and put BEHAVIORS in the map that that */
    // CHECKSTYLE:ON

    private static @NotNull Map<Integer, AI.BehaviorFunction> initializeBehaviorFunctions() {
        final Map<Integer, AI.BehaviorFunction> behaviorFunctionMap = new HashMap<>();

        behaviorFunctionMap.put(BehaviorCategories.BC_COMMON_MONSTER_AI, aggregatedBattleState -> {
            final Set<Ability> attacks = getDamagingAbilities(aggregatedBattleState
                    .self.getCharacterRole().getAllAbilities());
            if (!attacks.isEmpty() && aggregatedBattleState.enemies != null) {
                final AliveEntity targetEnemy = selectTarget(aggregatedBattleState.self,
                        aggregatedBattleState.aggroMap, aggregatedBattleState.enemies);

                final List<Integer> enemyCoords = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
                enemyCoords.add(DigitsPairIndices.ROW_COORD_INDEX,
                        Objects.requireNonNull(targetEnemy).getProperty(PropertyCategories.PC_COORDINATES,
                                DigitsPairIndices.ROW_COORD_INDEX));
                enemyCoords.add(DigitsPairIndices.COL_COORD_INDEX,
                        targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                DigitsPairIndices.COL_COORD_INDEX));
                final Ability bestAttack = aggregatedBattleState.fov.isVisible(enemyCoords)
                        ? selectAbility(aggregatedBattleState.self, aggregatedBattleState.map, attacks, targetEnemy)
                        : null;

                Action choice = null;
                if (bestAttack == null) {
                    choice = new MovementAction(Objects.requireNonNull(aggregatedBattleState.map.getTile(
                            aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                                    DigitsPairIndices.ROW_COORD_INDEX), aggregatedBattleState.self
                                    .getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.COL_COORD_INDEX))),
                            Objects.requireNonNull(aggregatedBattleState.map.getTile(
                                    targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.ROW_COORD_INDEX),
                                    targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.COL_COORD_INDEX))),
                            aggregatedBattleState.pathfinder);
                } else {
                    choice = new BattleAction(Objects.requireNonNull(aggregatedBattleState.map.getTile(
                            aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                                    DigitsPairIndices.ROW_COORD_INDEX), aggregatedBattleState.self
                                    .getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.COL_COORD_INDEX))),
                            Objects.requireNonNull(aggregatedBattleState.map.getTile(
                                    targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.ROW_COORD_INDEX),
                                    targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.COL_COORD_INDEX))),
                            bestAttack, aggregatedBattleState.pathfinder);
                }
                return choice;
            }

            return new SkipTurnAction(Objects.requireNonNull(aggregatedBattleState.map
                    .getTile(aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                            DigitsPairIndices.ROW_COORD_INDEX),
                            aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                                    DigitsPairIndices.COL_COORD_INDEX))));
        });
        // CHECKSTYLE:OFF
        /* TODO: write more AIs for monsters and bosses */
        // CHECKSTYLE:ON
        return behaviorFunctionMap;
    }


    @SuppressWarnings("unused")
    private static @NotNull Integer evaluateUnit(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator) {
        if (areAllies(unit, evaluator)) {
            return evaluateAlly(unit, evaluator);
        } else {
            return evaluateEnemy(unit, evaluator);
        }
    }

    @SuppressWarnings("unused")
    private static @NotNull Integer evaluateAlly(@NotNull AliveEntity ally, @NotNull AliveEntity evaluator) {
        //noinspection UnnecessaryLocalVariable
        final Integer score = 0;
        // CHECKSTYLE:OFF
        /* TODO: for future use in more complicated AIs */
        // CHECKSTYLE:ON
        return score;
    }

    @SuppressWarnings("unused")
    private static @NotNull Integer evaluateEnemy(@NotNull AliveEntity enemy, @NotNull AliveEntity npc) {
        //noinspection UnnecessaryLocalVariable
        final Integer score = 0;
        // CHECKSTYLE:OFF
        /* TODO: for future use in more complicated AIs */
        // CHECKSTYLE:ON
        return score;
    }

    private static @NotNull Boolean areAllies(@NotNull AliveEntity lhs, @NotNull AliveEntity rhs) {
        return Objects.equals(lhs.getProperty(PropertyCategories.PC_SQUAD_ID),
                rhs.getProperty(PropertyCategories.PC_SQUAD_ID));
    }

    private static @NotNull Integer getDistanceScore(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator) {
        Integer distance = Math.abs(unit.getProperty(PropertyCategories.PC_COORDINATES,
                DigitsPairIndices.ROW_COORD_INDEX) - evaluator.getProperty(PropertyCategories.PC_COORDINATES,
                DigitsPairIndices.ROW_COORD_INDEX)) + Math.abs(unit.getProperty(PropertyCategories.PC_COORDINATES,
                DigitsPairIndices.COL_COORD_INDEX) - evaluator.getProperty(PropertyCategories.PC_COORDINATES,
                DigitsPairIndices.COL_COORD_INDEX));
        if (distance > 0) {
            --distance;
        }
        Integer score = STANDS_ON_ADJACENT_TILE - ONE_STEP_COST * distance;
        if (distance <= Constants.DEFAULT_ALIVE_ENTITY_SPEED) {
            score += ONE_ACTION_STEP_BONUS;
        }
        return score;
    }

    @SuppressWarnings("unused")
    private static @NotNull Integer getHitpointsScore(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator) {
        return Constants.PERCENTAGE_CAP_INT
                - Math.round(unit.getProperty(PropertyCategories.PC_HITPOINTS,
                DigitsPairIndices.CURRENT_VALUE_INDEX).floatValue()
                / unit.getProperty(PropertyCategories.PC_HITPOINTS,
                DigitsPairIndices.MAX_VALUE_INDEX).floatValue()
                * Integer.valueOf(Constants.PERCENTAGE_CAP_INT).floatValue());
    }

    private static @NotNull Integer getAggroScore(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator,
                                                  @NotNull Map<Integer, Integer> aggroMap) {
        if (areAllies(unit, evaluator)) {
            return 0;
        }
        return Math.round(aggroMap.getOrDefault(unit.getID(), 0).floatValue()
                / evaluator.getProperty(PropertyCategories.PC_HITPOINTS,
                DigitsPairIndices.MAX_VALUE_INDEX).floatValue()
                * Integer.valueOf(Constants.PERCENTAGE_CAP_INT).floatValue());
    }

    @SuppressWarnings("unused")
    private static @NotNull Set<Ability> getHealingAbilities(@NotNull Map<Integer, Ability> abilities) {
        final Set<Ability> healingAbilities = new HashSet<>();

        for (Integer abilityID : abilities.keySet()) {
            final Ability ability = abilities.get(abilityID);
            if (ability.hasAffector(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR)) {
                if (ability.getAffection(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
                        DigitsPairIndices.MIN_VALUE_INDEX) > 0) {
                    healingAbilities.add(ability);
                }
            }
        }

        return healingAbilities;
    }

    private static @NotNull Set<Ability> getDamagingAbilities(@NotNull Map<Integer, Ability> abilities) {
        final Set<Ability> damagingAbilities = new HashSet<>();

        for (Integer abilityID : abilities.keySet()) {
            final Ability ability = abilities.get(abilityID);
            if (ability.hasAffector(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR)) {
                if (ability.getAffection(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
                        DigitsPairIndices.MIN_VALUE_INDEX) <= 0) {
                    damagingAbilities.add(ability);
                }
            }
        }

        return damagingAbilities;
    }

    private static @Nullable AliveEntity selectTarget(@NotNull AliveEntity self,
                                                      @NotNull Map<Integer, Integer> aggroMap,
                                                      @NotNull Squad enemies) {

        Integer enemyIdWithMaximalScore = Constants.WRONG_INDEX;
        Integer maximalScore = Integer.MIN_VALUE;
        for (Integer enemyID = 0; enemyID < enemies.getSquadSize(); ++enemyID) {
            final AliveEntity enemy = enemies.getMember(enemyID);
            if (enemy != null) {
                if (enemy.isAlive()) {
                    final Integer enemyDistanceScore = getDistanceScore(enemy, self);
                    final Integer enemyHealthScore = getHitpointsScore(enemy, self);
                    final Integer enemyAggroScore = getAggroScore(enemy, self,
                            aggroMap);
                    if (enemyDistanceScore + enemyHealthScore + enemyAggroScore > maximalScore) {
                        maximalScore = enemyDistanceScore + enemyHealthScore + enemyAggroScore;
                        enemyIdWithMaximalScore = enemyID;
                    } else {
                        if (enemyDistanceScore + enemyHealthScore + enemyAggroScore == maximalScore) {
                            if (enemyDistanceScore > getDistanceScore(Objects.requireNonNull(
                                    enemies.getMember(enemyIdWithMaximalScore)), self)) {
                                enemyIdWithMaximalScore = enemyID;
                            } else {
                                if (aggroMap.get(enemy.getID()) > aggroMap.get(
                                        Objects.requireNonNull(enemies
                                                .getMember(enemyIdWithMaximalScore)).getID())) {
                                    enemyIdWithMaximalScore = enemyID;
                                }
                            }
                        }
                    }
                }
            }
        }

        return enemies.getMember(enemyIdWithMaximalScore);
    }

    private static @Nullable Ability selectAbility(@NotNull AliveEntity self, @NotNull BattleMap map,
                                                   @NotNull Set<Ability> attacks,
                                                   @NotNull AliveEntity targetEnemy) {
        final Integer distanceToTarget = Objects.requireNonNull(map
                .getTile(self.getProperty(PropertyCategories.PC_COORDINATES,
                        DigitsPairIndices.ROW_COORD_INDEX), self
                        .getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.COL_COORD_INDEX)))
                .getH(Objects.requireNonNull(map.getTile(
                        Objects.requireNonNull(targetEnemy)
                                .getProperty(PropertyCategories.PC_COORDINATES,
                                        DigitsPairIndices.ROW_COORD_INDEX),
                        targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                DigitsPairIndices.COL_COORD_INDEX))));

        Ability bestAttack = null;
        Integer maxAttackDamage = Integer.MIN_VALUE;
        for (Ability attack : attacks) {
            if (self.getProperty(PropertyCategories.PC_ABILITIES_COOLDOWN,
                    attack.getID()) == 0) {
                if (attack.getProperty(PropertyCategories.PC_MAX_DISTANCE) >= distanceToTarget) {
                    if (attack.getAffection(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
                            DigitsPairIndices.MIN_VALUE_INDEX) > maxAttackDamage) {
                        bestAttack = attack;
                        maxAttackDamage = attack.getAffection(
                                AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
                                DigitsPairIndices.MIN_VALUE_INDEX);
                    } else {
                        if (attack.getAffection(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
                                DigitsPairIndices.MAX_VALUE_INDEX) > maxAttackDamage) {
                            bestAttack = attack;
                            maxAttackDamage = attack.getAffection(
                                    AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
                                    DigitsPairIndices.MAX_VALUE_INDEX);
                        }
                    }
                }
            }
        }
        return bestAttack;
    }
}
