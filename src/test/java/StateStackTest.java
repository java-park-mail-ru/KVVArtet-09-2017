import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import statemachine.StateStack;
import states.State;

import java.util.Random;

import static org.junit.Assert.*;

public class StateStackTest {
    private StateStack stack;
    private final Random random = new Random();

    private State.StateId pickRandomState() {
        final State.StateId[] values = State.StateId.values();
        return values[random.nextInt(values.length)];
    }

    @Before
    public void init() {
        stack = new StateStack();
    }

    @After
    public void endUp() {
        stack.clearState();
        stack.update();
    }

    @Test
    public void freshStackTest() {
        assertFalse("in newly made stack shall be no pending changes enqueued", stack.hasPendingChanges());
        assertTrue("newly made stack shall contain no states", stack.isStackEmpty());
    }

    @Test
    public void addingChangeRequestsTest() {
        assertFalse("shall be no pending changes before test", stack.hasPendingChanges());
        final int actionsCount = 3;
        final int testsCount = 10;
        for (Integer i = 0; i < testsCount; ++i) {
            final Integer action = random.nextInt(100) % actionsCount;
            switch (action) {
                case 0:
                    stack.pushState(pickRandomState());
                    break;
                case 1:
                    stack.popState();
                    break;
                case 2:
                    stack.clearState();
                    break;
                default:
                    break;
            }
        }
        assertTrue("there shall be some pending changes after adding", stack.hasPendingChanges());
        assertEquals("there shall be " + Integer.toString(testsCount) + " pending changes",
                testsCount, stack.pendingChangesCount() );
    }

    @Test
    public void pushChangeImplementationTest() {
        assertTrue("state stack shall be empty before test", stack.isStackEmpty());
        final Integer testsCount = 10;
        for (Integer i = 0; i < testsCount; ++i) {
            final State.StateId randomState = pickRandomState();
            stack.pushState(randomState);
            stack.update();
            if (randomState == State.StateId.SI_NONE) {
                assertTrue("there shall be no state created for None state", stack.isStackEmpty());
            } else {
                assertEquals("there shall be one state in stack", 1, stack.stackSize());
            }
            stack.popState();
        }
    }

    @Test
    public void popChangeImplementationTest() {
        assertTrue("stack shall be empty before testing", stack.isStackEmpty());
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
        assertTrue("stack shall be empty after implementing pop change", stack.isStackEmpty());
        assertFalse("there shall be no pending changes after test", stack.hasPendingChanges());
    }

    @Test
    public void clearChangeImplementationTest() {
        final int statesToAdd = 15;
        for (Integer i = 0; i < statesToAdd; ++i) {
            stack.pushState(State.StateId.SI_TITLE);
        }
        stack.update();
        assertFalse("state stack shall contain states before testing", stack.isStackEmpty());
        stack.clearState();
        stack.update();
        assertTrue("stack shall be empty after clear change implementation", stack.isStackEmpty());
        assertFalse("there shall be no pending changes after test", stack.hasPendingChanges());
    }

    @Test
    public void updateMethodTest() {
        assertFalse("changes queue shall be empty before testing", stack.hasPendingChanges());
        final int changesToAdd = 20;
        for (Integer i = 0; i < changesToAdd; ++i) {
            stack.popState();
        }
        stack.update();
        assertFalse("changes queue shall be empty after changes implementation",
                stack.hasPendingChanges());
    }
}
