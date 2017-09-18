package states;

import statemachine.PendingStack;

public final class StateFactory {
    private StateFactory() {}

    public static AbstractState createState(AbstractState.StateId stateId, PendingStack stack) {
        AbstractState state = null;
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

    private static AbstractState createTitleState(PendingStack stack) {
        return new TitleState(stack);
    }

    private static AbstractState createSignupState(PendingStack stack) {
        return new SignupState(stack);
    }

    private static AbstractState createCharacterListState(PendingStack stack) {
        return new CharacterListState(stack);
    }

    private static AbstractState createCityState(PendingStack stack) {
        return new CityState(stack);
    }

    private static AbstractState createDungeonState(PendingStack stack) {
        return new DungeonState(stack);
    }

    private static AbstractState createCharacterCreationState(PendingStack stack) { return new CharacterCreationState(stack); }
}