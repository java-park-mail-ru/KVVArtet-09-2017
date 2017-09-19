package states;

import statemachine.PendingStack;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public final class StateFactory {
    private StateFactory() {}

    public static State createState(State.StateId stateId, PendingStack stack) {
        State state = null;
        switch (stateId) {
            case SI_NONE:
                break;
            case SI_TITLE:
                state = StateFactory.createTitleState(stack);
                break;
            case SI_SIGNUP:
                state = StateFactory.createSignupState(stack);
                break;
            case SI_CHARACTER_LIST:
                state = StateFactory.createCharacterListState(stack);
                break;
            case SI_CITY:
                state = StateFactory.createCityState(stack);
                break;
            case SI_DUNGEON:
                state = StateFactory.createDungeonState(stack);
                break;
            case SI_CHARACTER_CREATION:
                state = StateFactory.createCharacterCreationState(stack);
            default:
                break;
        }
        return state;
    }

    private static State createTitleState(PendingStack stack) {
        State state = new TitleState(stack);
        logger.info("TitleState created");
        return state;
    }

    private static State createSignupState(PendingStack stack) {
        State state = new SignupState(stack);
        logger.info("SignupState created");
        return state;
    }

    private static State createCharacterListState(PendingStack stack) {
        State state = new CharacterListState(stack);
        logger.info("CharacterListState created");
        return state;
    }

    private static State createCityState(PendingStack stack) {
        State state = new CityState(stack);
        logger.info("PendingState created");
        return state;
    }

    private static State createDungeonState(PendingStack stack) {
        State state = new DungeonState(stack);
        logger.info("DungeonState created");
        return state;
    }

    private static State createCharacterCreationState(PendingStack stack) {
        State state = new CharacterCreationState(stack);
        logger.info("CharacterCreationState created");
        return state;
    }

    private static final Logger logger = LoggerFactory.getLogger(StateFactory.class);
}
