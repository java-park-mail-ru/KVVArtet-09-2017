package gamemechanics.interfaces;

public interface Action extends Countable {
    MapNode getSender();
    MapNode getTarget();

    default Ability getAbility() {
        return null;
    }

    Boolean isMovement();

    Boolean execute();
}
