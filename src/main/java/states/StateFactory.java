package states;

import statemachine.PendingStack;

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
        return new TitleState(stack);
    }

    private static State createSignupState(PendingStack stack) {
        return new SignupState(stack);
    }

    private static State createCharacterListState(PendingStack stack) {
        return new CharacterListState(stack);
    }

    private static State createCityState(PendingStack stack) {
        return new CityState(stack);
    }

    private static State createDungeonState(PendingStack stack) {
        return new DungeonState(stack);
    }

    private static State createCharacterCreationState(PendingStack stack) { return new CharacterCreationState(stack); }
}