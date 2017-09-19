package statemachine;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import packets.Packet;
import states.State;
import states.StateFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import static junit.framework.Assert.*;

public class StateStack implements PendingStack {
    private final Deque<PendingChange> pendingChanges = new LinkedList<>();
    private final Deque<State> stack = new ArrayDeque<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(StateStack.class);

    private enum StackActions { SA_PUSH, SA_POP, SA_CLEAR }

    private static class PendingChange {
        private final StackActions action;
        private final State.StateId state;

        PendingChange(StackActions action, State.StateId stateId) {
            this.action = action;
            this.state = stateId;
        }

        PendingChange(StackActions action) {
            this(action, State.StateId.SI_NONE);
        }

        StackActions getAction() {
            return action;
        }

        State.StateId getState() {
            return state;
        }
    }

    public static class StateStackTest {
        private StateStack stack;
        private Random random = new Random();
        private static final Logger TEST_LOGGER = LoggerFactory.getLogger(StateStackTest.class);

        private StackActions pickRandomAction() {
            final StackActions[] values = StackActions.values();
            return values[random.nextInt(values.length)];
        }

        private State.StateId pickRandomState() {
            final State.StateId[] values = State.StateId.values();
            return values[random.nextInt(values.length)];
        }

        @Before
        public void init() {
            stack = new StateStack();
        }

        @Test
        public void freshStackTest() {
            assertTrue("in newly made stack shall be no pending changes enqueued", stack.pendingChanges.isEmpty());
            assertTrue("newly made stack shall contain no states", stack.stack.isEmpty());
            TEST_LOGGER.info("====");
        }

        @Test
        public void addingChangeRequestsTest() {
            final Integer testsCount = 10;
            TEST_LOGGER.info("performing " + testsCount.toString() + " attempts to enqueue random action in the stack");
            for (Integer i = 0; i < testsCount; ++i) {
                final StackActions randomAction = pickRandomAction();
                switch (randomAction) {
                    case SA_PUSH:
                        final State.StateId randomState = pickRandomState();
                        stack.pushState(randomState);
                        assertEquals("added change shall have exactly same state as generated", randomState, stack.pendingChanges.peekLast().getState());
                        break;
                    case SA_POP:
                        stack.popState();
                        assertEquals("added change shall have exactly same state as generated", State.StateId.SI_NONE, stack.pendingChanges.peekLast().getState());
                        break;
                    case SA_CLEAR:
                        stack.clearState();
                        assertEquals("added change shall have exactly same state as generated", State.StateId.SI_NONE, stack.pendingChanges.peekLast().getState());
                        break;
                    default:
                        break;
                }
                assertEquals("added change shall have exactly same action as generated", stack.pendingChanges.peekLast().getAction(), randomAction);
            }
            stack.update();
            assertTrue("after update() call changes queue shall be empty", stack.pendingChanges.isEmpty());
            stack.stack.clear();
            TEST_LOGGER.info("====");
        }

        @Test
        public void pushChangeImplementationTest() {
            TEST_LOGGER.info("testing push change implementation on random states");
            assertTrue("state stack shall be empty before test", stack.stack.isEmpty());
            final Integer testsCount = 10;
            for (Integer i = 0; i < testsCount; ++i) {
                final State.StateId randomState = pickRandomState();
                stack.pushState(randomState);
                stack.update();
                final State generatedState = stack.stack.pop();
                final String statesPackageName = "states.";
                switch (randomState) {
                    case SI_NONE:
                        assertNull("None state can not be created", generatedState);
                        break;
                    case SI_TITLE:
                        assertEquals("Title state shall be created",  statesPackageName + "TitleState", generatedState.getClass().getName());
                        break;
                    case SI_SIGNUP:
                        assertEquals("Signup state shall be created", statesPackageName + "SignupState", generatedState.getClass().getName());
                        break;
                    case SI_CHARACTER_LIST:
                        assertEquals("CharacterList state shall be created", statesPackageName + "CharacterListState", generatedState.getClass().getName());
                        break;
                    case SI_CITY:
                        assertEquals("City state shall be created", statesPackageName + "CityState", generatedState.getClass().getName());
                        break;
                    case SI_DUNGEON:
                        assertEquals("Dungeon state shall be created", statesPackageName + "DungeonState", generatedState.getClass().getName());
                        break;
                    case SI_CHARACTER_CREATION:
                        assertEquals("CharacterCreation state shall be created", statesPackageName + "CharacterCreationState", generatedState.getClass().getName());
                        break;
                    default:
                        break;
                }
            }
            TEST_LOGGER.info("====");
        }

        @Test
        public void popChangeImplementationTest() {
            TEST_LOGGER.info("testing pop change implementation");
            assertTrue("stack shall be empty before testing", stack.stack.isEmpty());
            State.StateId randomState = State.StateId.SI_NONE;
            final Integer itersCount = 10;
            final Integer counter = 0;
            while (randomState == State.StateId.SI_NONE && counter <= itersCount) {
                randomState = pickRandomState();
            }
            if (randomState == State.StateId.SI_NONE) {
                randomState = State.StateId.SI_TITLE;
            }
            stack.pushState(randomState);
            stack.popState();
            stack.update();
            assertTrue("stack shall be empty after implementing pop change", stack.stack.isEmpty());
            TEST_LOGGER.info("====");
        }

        @Test
        public void clearChangeImplementationTest() {
            TEST_LOGGER.info("testing clear change implementation");
            final Integer statesToAdd = 15;
            for (Integer i = 0; i < statesToAdd; ++i) {
                stack.pushState(State.StateId.SI_TITLE);
            }
            stack.update();
            assertFalse("state stack shall contain states before testing", stack.stack.isEmpty());
            stack.clearState();
            stack.update();
            assertTrue("stack shall be empty after clear change implementation", stack.stack.isEmpty());
            TEST_LOGGER.info("====");
        }

        @Test
        public void updateMethodTest() {
            TEST_LOGGER.info("testing pending changes application in update() method");
            assertTrue("changes queue shall be empty before testing", stack.pendingChanges.isEmpty());
            final int changesToAdd = 20;
            for (Integer i = 0; i < changesToAdd; ++i) {
                stack.popState();
            }
            stack.update();
            assertTrue("changes queue shall be empty after changes implementation", stack.pendingChanges.isEmpty());
            TEST_LOGGER.info("====");
        }
    }

    @Override
    public void pushState(State.StateId stateId) {
        pendingChanges.add(new PendingChange(StackActions.SA_PUSH, stateId));
        LOGGER.info("push request for state " + stateId.toString() + " enqueued");
    }

    @Override
    public void popState() {
        pendingChanges.add(new PendingChange(StackActions.SA_POP));
        LOGGER.info("pop request enqueued");
    }

    @Override
    public void clearState() {
        pendingChanges.add(new PendingChange(StackActions.SA_CLEAR));
        LOGGER.info("clear request enqueued");
    }

    @Override
    public void update() {
        if (!stack.isEmpty()) {
            LOGGER.info("active state update call: ");
            stack.peek().update();
        }
        applyPendingChanges();
    }

    @Override
    public void handlePacket(final Packet packet) {
        LOGGER.info("state handlePacket call: ");
        for (State state : stack) {
            if (!state.handlePacket(packet)) {
                break;
            }
        }
        applyPendingChanges();
    }

    private void applyPendingChanges() {
        while (!pendingChanges.isEmpty()) {
            final PendingChange change = pendingChanges.poll();
            implementChange(change);
        }
    }

    private void implementChange(PendingChange change) {
        switch (change.getAction()) {
            case SA_PUSH:
                implementPush(change.getState());
                break;
            case SA_POP:
                implementPop();
                break;
            case SA_CLEAR:
                implementClear();
                break;
            default:
                break;
        }
    }

    private void implementPush(State.StateId stateId) {
        final State state = StateFactory.createState(stateId, this);
        if (state != null) {
            stack.push(state);
        }
        LOGGER.info("push request for state " + stateId.toString() + " done");
    }

    private void implementPop() {
        stack.pop();
        LOGGER.info("pop request done");
    }

    private void implementClear() {
        stack.clear();
        LOGGER.info("clear request done");
    }
}
