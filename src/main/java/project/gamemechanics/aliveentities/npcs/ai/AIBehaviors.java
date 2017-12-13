package project.gamemechanics.aliveentities.npcs.ai;

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
public final class AIBehaviors {
    private static final Map<Integer, AI.BehaviorFunction> BEHAVIORS = initializeBehaviorFunctions();

    private static final int ONE_ACTION_STEP_BONUS = 10;
    private static final int STANDS_ON_ADJACENT_TILE = 17;
    private static final int ONE_STEP_COST = 2;

    private AIBehaviors() {
    }

    /**
     * get an AI behavior by given ID
     *
     * @param behaviorID ID of the wanted behavior
     * @return null if ID is invalid or {@link AI.BehaviorFunction} registered
     * under given ID otherwise
     */
    public static AI.BehaviorFunction getBehavior(Integer behaviorID) {
        return BEHAVIORS.getOrDefault(behaviorID, null);
    }

    public static Map<Integer, AI.BehaviorFunction> getAllBehaviors() {
        return BEHAVIORS;
    }

    /* TODO: find a better way to write and put BEHAVIORS in the map that that */
    @SuppressWarnings("OverlyComplexMethod")
    private static Map<Integer, AI.BehaviorFunction> initializeBehaviorFunctions() {
        final Map<Integer, AI.BehaviorFunction> behaviorFunctionMap = new HashMap<>();

        behaviorFunctionMap.put(BehaviorCategories.BC_COMMON_MONSTER_AI, aggregatedBattleState -> {
            final Set<Ability> attacks = getDamagingAbilities(aggregatedBattleState
                    .self.getCharacterRole().getAllAbilities());
            if (!attacks.isEmpty()) {
                Integer maximalScore = Integer.MIN_VALUE;
                Integer enemyIdWithMaximalScore = Constants.WRONG_INDEX;

                for (Integer enemyID = 0; enemyID < aggregatedBattleState.enemies.getSquadSize(); ++enemyID) {
                    final AliveEntity enemy = aggregatedBattleState.enemies.getMember(enemyID);
                    if (enemy != null) {
                        if (enemy.isAlive()) {
                            final Integer enemyDistanceScore = getDistanceScore(enemy, aggregatedBattleState.self);
                            final Integer enemyHealthScore = getHitpointsScore(enemy, aggregatedBattleState.self);
                            final Integer enemyAggroScore = getAggroScore(enemy, aggregatedBattleState.self,
                                    aggregatedBattleState.aggroMap);
                            if (enemyDistanceScore + enemyHealthScore + enemyAggroScore > maximalScore) {
                                maximalScore = enemyDistanceScore + enemyHealthScore + enemyAggroScore;
                                enemyIdWithMaximalScore = enemyID;
                            } else {
                                if (enemyDistanceScore + enemyHealthScore + enemyAggroScore == maximalScore) {
                                    if (enemyDistanceScore > getDistanceScore(Objects.requireNonNull(
                                            aggregatedBattleState.enemies.getMember(enemyIdWithMaximalScore)),
                                            aggregatedBattleState.self)) {
                                        enemyIdWithMaximalScore = enemyID;
                                    } else {
                                        if (aggregatedBattleState.aggroMap.get(enemy.getID())
                                                > aggregatedBattleState.aggroMap.get(
                                                        Objects.requireNonNull(aggregatedBattleState.enemies
                                                .getMember(enemyIdWithMaximalScore)).getID())) {
                                            enemyIdWithMaximalScore = enemyID;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                final AliveEntity targetEnemy = aggregatedBattleState.enemies.getMember(enemyIdWithMaximalScore);

                assert targetEnemy != null;
                final Integer distanceToTarget = aggregatedBattleState.map
                        .getTile(aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                                DigitsPairIndices.ROW_COORD_INDEX), aggregatedBattleState.self
                                .getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.COL_COORD_INDEX))
                        .getH(aggregatedBattleState.map.getTile(targetEnemy
                                        .getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.ROW_COORD_INDEX),
                                targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                        DigitsPairIndices.COL_COORD_INDEX)));

                Integer maxAttackDamage = Integer.MIN_VALUE;
                Ability bestAttack = null;
                for (Ability attack : attacks) {
                    if (aggregatedBattleState.self.getProperty(PropertyCategories.PC_ABILITIES_COOLDOWN,
                            attack.getID()) == 0) {
                        if (attack.getProperty(PropertyCategories.PC_MAX_DISTANCE) >= distanceToTarget) {
                            if (attack.getAffection(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
                                    DigitsPairIndices.MIN_VALUE_INDEX) > maxAttackDamage) {
                                bestAttack = attack;
                                maxAttackDamage = attack.getAffection(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
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

                Action choice = null;
                if (bestAttack == null) {
                    choice = new MovementAction(aggregatedBattleState.map.getTile(
                            aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                                    DigitsPairIndices.ROW_COORD_INDEX), aggregatedBattleState.self
                                    .getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.COL_COORD_INDEX)),
                            aggregatedBattleState.map.getTile(
                                    targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.ROW_COORD_INDEX),
                                    targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.COL_COORD_INDEX)),
                            aggregatedBattleState.pathfinder);
                } else {
                    choice = new BattleAction(aggregatedBattleState.map.getTile(
                            aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                                    DigitsPairIndices.ROW_COORD_INDEX), aggregatedBattleState.self
                                    .getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.COL_COORD_INDEX)),
                            aggregatedBattleState.map.getTile(
                                    targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.ROW_COORD_INDEX),
                                    targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                            DigitsPairIndices.COL_COORD_INDEX)),
                            bestAttack, aggregatedBattleState.pathfinder);
                }
                return choice;
            }

            return new SkipTurnAction(aggregatedBattleState.map
                    .getTile(aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                            DigitsPairIndices.ROW_COORD_INDEX),
                            aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                                    DigitsPairIndices.COL_COORD_INDEX)));
        });

        /* TODO: write more AIs for monsters and bosses */

        return behaviorFunctionMap;
    }


    private static Integer evaluateUnit(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator) {
        if (areAllies(unit, evaluator)) {
            return evaluateAlly(unit, evaluator);
        } else {
            return evaluateEnemy(unit, evaluator);
        }
    }

    private static Integer evaluateAlly(@NotNull AliveEntity ally, @NotNull AliveEntity evaluator) {
        //noinspection UnnecessaryLocalVariable
        final Integer score = 0;
        /* TODO: for future use in more complicated AIs */
        return score;
    }

    private static Integer evaluateEnemy(@NotNull AliveEntity enemy, @NotNull AliveEntity npc) {
        //noinspection UnnecessaryLocalVariable
        final Integer score = 0;
        /* TODO: for future use in more complicated AIs */
        return score;
    }

    private static Boolean areAllies(@NotNull AliveEntity lhs, @NotNull AliveEntity rhs) {
        return Objects.equals(lhs.getProperty(PropertyCategories.PC_SQUAD_ID),
                rhs.getProperty(PropertyCategories.PC_SQUAD_ID));
    }

    private static Integer getDistanceScore(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator) {
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

    private static Integer getHitpointsScore(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator) {
        return Math.round(unit.getProperty(PropertyCategories.PC_HITPOINTS,
                DigitsPairIndices.CURRENT_VALUE_INDEX).floatValue()
                / unit.getProperty(PropertyCategories.PC_HITPOINTS,
                DigitsPairIndices.MAX_VALUE_INDEX).floatValue()
                * Integer.valueOf(Constants.PERCENTAGE_CAP_INT).floatValue());
    }

    private static Integer getAggroScore(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator,
                                         @NotNull Map<Integer, Integer> aggroMap) {
        if (areAllies(unit, evaluator)) {
            return 0;
        }
        return Math.round(aggroMap.getOrDefault(unit.getID(), 0).floatValue()
                / evaluator.getProperty(PropertyCategories.PC_HITPOINTS,
                DigitsPairIndices.MAX_VALUE_INDEX).floatValue()
                * Integer.valueOf(Constants.PERCENTAGE_CAP_INT).floatValue());
    }

    private static Set<Ability> getHealingAbilities(@NotNull Map<Integer, Ability> abilities) {
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

    private static Set<Ability> getDamagingAbilities(@NotNull Map<Integer, Ability> abilities) {
        final Set<Ability> damagingAbilities = new HashSet<>();

        for (Integer abilityID : abilities.keySet()) {
            final Ability ability = abilities.get(abilityID);
            if (ability.hasAffector(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR)) {
                if (ability.getAffection(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
                        DigitsPairIndices.MIN_VALUE_INDEX) < 0) {
                    damagingAbilities.add(ability);
                }
            }
        }

        return damagingAbilities;
    }
}
