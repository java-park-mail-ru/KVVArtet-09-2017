package gamemechanics.interfaces;

import gamemechanics.battlefield.Action;

public interface DecisionMaker extends Countable {
    Action makeDecision();
}
