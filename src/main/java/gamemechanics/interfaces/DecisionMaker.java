package gamemechanics.interfaces;

/**
 * interface for various monster AI implementations
 */
public interface DecisionMaker extends Countable {
    /**
     * choose the best possible {@link Action} based on current
     * battle status
     * @return chosen action
     * @see Action
     */
    Action makeDecision();
}
