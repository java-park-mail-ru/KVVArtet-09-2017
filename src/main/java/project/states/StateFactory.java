package project.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.statemachine.PendingStack;

@SuppressWarnings("SpellCheckingInspection")
public final class StateFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateFactory.class);

    private StateFactory() { }

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
                break;
            default:
                break;
        }
        return state;
    }

    private static State createTitleState(PendingStack stack) {
        final State state = new TitleState(stack);
        LOGGER.info("TitleState created");
        return state;
    }

    private static State createSignupState(PendingStack stack) {
        final State state = new SignupState(stack);
        LOGGER.info("SignupState created");
        return state;
    }

    private static State createCharacterListState(PendingStack stack) {
        final State state = new CharacterListState(stack);
        LOGGER.info("CharacterListState created");
        return state;
    }

    private static State createCityState(PendingStack stack) {
        final State state = new CityState(stack);
        LOGGER.info("PendingState created");
        return state;
    }

    private static State createDungeonState(PendingStack stack) {
        final State state = new DungeonState(stack);
        LOGGER.info("DungeonState created");
        return state;
    }

    private static State createCharacterCreationState(PendingStack stack) {
        final State state = new CharacterCreationState(stack);
        LOGGER.info("CharacterCreationState created");
        return state;
    }
}
