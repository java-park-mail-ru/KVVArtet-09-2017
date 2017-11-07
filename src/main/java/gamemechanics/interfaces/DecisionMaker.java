package gamemechanics.interfaces;

import gamemechanics.battlefield.BattleAction;

public interface DecisionMaker extends Countable {
    BattleAction makeDecision();
}
