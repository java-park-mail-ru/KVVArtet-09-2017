package gamemechanics.aliveentities.npcs.ai;

import gamemechanics.battlefield.map.actions.BattleAction;
import gamemechanics.battlefield.map.actions.MovementAction;
import gamemechanics.battlefield.map.actions.SkipTurnAction;
import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.globals.Constants;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.Action;
import gamemechanics.interfaces.AliveEntity;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * static storage class for AI behavior implementation <br/>
 * (note: it's in WIP state at this moment,
 * and requires some refactoring)<br/><br/>
 * --<br/><br/>
 * goal is to make behaviors JSON-serializable and human-readable to allow in-situ editing.
 */
public final class AIBehaviors {
    private static final Map<Integer, AI.BehaviorFunction> behaviors = initializeBehaviorFunctions();

    private AIBehaviors() {}

    /**
     * get an AI behavior by given ID
     * @param behaviorID ID of the wanted behavior
     * @return null if ID is invalid or {@link AI.BehaviorFunction} registered
     * under given ID otherwise
     */
    public static AI.BehaviorFunction getBehavior(Integer behaviorID) {
        return behaviors.getOrDefault(behaviorID, null);
    }

    /* TODO: find a better way to write and put behaviors in the map that that */
    private static Map<Integer, AI.BehaviorFunction> initializeBehaviorFunctions() {
        Map<Integer, AI.BehaviorFunction> behaviorFunctionMap = new HashMap<>();

        behaviorFunctionMap.put(BehaviorCategories.BC_COMMON_MONSTER_AI, aggregatedBattleState -> {
            Set<Ability> attacks = getDamagingAbilities(aggregatedBattleState
                    .self.getCharacterRole().getAllAbilities());
            if (!attacks.isEmpty()) {
                Action choice = null;
                Integer maximalScore = Integer.MIN_VALUE;
                Integer enemyIdWithMaximalScore = Constants.WRONG_INDEX;

                for (Integer enemyID = 0; enemyID < aggregatedBattleState.enemies.getSquadSize(); ++enemyID) {
                    AliveEntity enemy = aggregatedBattleState.enemies.getMember(enemyID);
                    if (enemy != null) {
                        if (enemy.isAlive()) {
                            Integer enemyDistanceScore = getDistanceScore(enemy, aggregatedBattleState.self);
                            Integer enemyHealthScore = getHitpointsScore(enemy, aggregatedBattleState.self);
                            if (enemyDistanceScore + enemyHealthScore > maximalScore) {
                                maximalScore = enemyDistanceScore + enemyHealthScore;
                                enemyIdWithMaximalScore = enemyID;
                            } else {
                                if (enemyDistanceScore + enemyHealthScore == maximalScore) {
                                    if (enemyDistanceScore > getDistanceScore(aggregatedBattleState
                                            .enemies.getMember(enemyIdWithMaximalScore),
                                            aggregatedBattleState.self)) {
                                        enemyIdWithMaximalScore = enemyID;
                                    }
                                }
                            }
                        }
                    }
                }

                AliveEntity targetEnemy = aggregatedBattleState.enemies.getMember(enemyIdWithMaximalScore);

                Integer maxAttackDamage = Integer.MIN_VALUE;
                Ability bestAttack = null;
                Integer distanceToTarget = aggregatedBattleState.map
                        .getTile(aggregatedBattleState.self.getProperty(PropertyCategories.PC_COORDINATES,
                                DigitsPairIndices.ROW_COORD_INDEX), aggregatedBattleState.self
                                .getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.COL_COORD_INDEX))
                        .getH(aggregatedBattleState.map.getTile(targetEnemy
                                .getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.ROW_COORD_INDEX),
                                targetEnemy.getProperty(PropertyCategories.PC_COORDINATES,
                                        DigitsPairIndices.COL_COORD_INDEX)));

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
                            bestAttack);
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
        Integer score = 0;
        /* TODO: for future use in more complicated AIs */
        return score;
    }

    private static Integer evaluateEnemy(@NotNull AliveEntity enemy, @NotNull AliveEntity npc) {
        Integer score = 0;
        /* TODO: for future use in more complicated AIs */
        return score;
    }

    private static Boolean areAllies(@NotNull AliveEntity lhs, @NotNull AliveEntity rhs) {
        return Objects.equals(lhs.getProperty(PropertyCategories.PC_SQUAD_ID),
                rhs.getProperty(PropertyCategories.PC_SQUAD_ID));
    }

    private static Integer getDistanceScore(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator) {
        return 0;
    }

    private static Integer getHitpointsScore(@NotNull AliveEntity unit, @NotNull AliveEntity evaluator) {
        return 0;
    }

    private static Set<Ability> getHealingAbilities(Map<Integer, Ability> abilities) {
        Set<Ability> healingAbilities = new HashSet<>();

        for (Integer abilityID : abilities.keySet()) {
            Ability ability = abilities.get(abilityID);
            if (ability.hasAffector(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR)) {
                if (ability.getAffection(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR,
                        DigitsPairIndices.MIN_VALUE_INDEX) > 0) {
                    healingAbilities.add(ability);
                }
            }
        }

        return healingAbilities;
    }

    private static Set<Ability> getDamagingAbilities(Map<Integer, Ability> abilities) {
        Set<Ability> damagingAbilities = new HashSet<>();

        for (Integer abilityID : abilities.keySet()) {
            Ability ability = abilities.get(abilityID);
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
