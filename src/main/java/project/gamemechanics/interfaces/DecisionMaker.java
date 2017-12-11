package project.gamemechanics.interfaces;

/**
 * interface for various monster AI implementations
 */
public interface DecisionMaker extends Countable {
    /**
     * choose the best possible {@link Action} based on current
     * battle status
     *
     * @return chosen action
     * @see Action
     */
    Action makeDecision();

    /**
     * updates Aggro level for NPC's enemy on given amount by given index
     *
     * @param enemyID enemy ID to update aggro for
     * @param amount  aggro amount to add
     */
    void updateAggro(Integer enemyID, Integer amount);

    /**
     * updates amount of damage received from the enemy by given ID on given amount
     *
     * @param enemyID enemy ID to update damage for
     * @param amount  amount of damage to add
     */
    void updateDamageFrom(Integer enemyID, Integer amount);
}
