import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statemachine.StateStack;
import states.State;

import java.util.Random;

import static junit.framework.Assert.*;

public class StateStackTest {
    private StateStack stack;
    private Random random = new Random();
    private static final Logger TEST_LOGGER = LoggerFactory.getLogger(StateStackTest.class);

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
        TEST_LOGGER.info("====");
        TEST_LOGGER.info("checking newly created state machine");
        assertFalse("in newly made stack shall be no pending changes enqueued", stack.hasPendingChanges());
        assertTrue("newly made stack shall contain no states", stack.isStackEmpty());
        TEST_LOGGER.info("====");
    }

    @Test
    public void addingChangeRequestsTest() {
        TEST_LOGGER.info("====");
        final int testsCount = 10;
        TEST_LOGGER.info("performing " + Integer.toString(testsCount) + " attempts to enqueue random action in the stack");
        assertFalse("shall be no pending changes before test", stack.hasPendingChanges());
        final int actionsCount = 3;
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
        stack.clearState();
        stack.update();
        assertTrue("stack shall be clear after test", stack.isStackEmpty());
        assertFalse("there shall remain no pending changes after test", stack.hasPendingChanges());
        TEST_LOGGER.info("====");
    }

    @Test
    public void pushChangeImplementationTest() {
        TEST_LOGGER.info("====");
        TEST_LOGGER.info("testing push change implementation on random states");
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
        stack.clearState();
        stack.update();
        assertTrue("stack shall be clear after test", stack.isStackEmpty());
        assertFalse("there shall be no pending changes after test", stack.hasPendingChanges());
        TEST_LOGGER.info("====");
    }

    @Test
    public void popChangeImplementationTest() {
        TEST_LOGGER.info("====");
        TEST_LOGGER.info("testing pop change implementation");
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
        TEST_LOGGER.info("====");
    }

    @Test
    public void clearChangeImplementationTest() {
        TEST_LOGGER.info("====");
        TEST_LOGGER.info("testing clear change implementation");
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
        TEST_LOGGER.info("====");
    }

    @Test
    public void updateMethodTest() {
        TEST_LOGGER.info("====");
        TEST_LOGGER.info("testing pending changes application in update() method");
        assertFalse("changes queue shall be empty before testing", stack.hasPendingChanges());
        final int changesToAdd = 20;
        for (Integer i = 0; i < changesToAdd; ++i) {
            stack.popState();
        }
        stack.update();
        assertFalse("changes queue shall be empty after changes implementation",
                stack.hasPendingChanges());
        TEST_LOGGER.info("====");
    }

    public static void main(String[] args) {

        final Result testResult = JUnitCore.runClasses(StateStackTest.class);
        for (Failure fail : testResult.getFailures()) {
            TEST_LOGGER.error(fail.toString());
        }
        TEST_LOGGER.info("====");
        TEST_LOGGER.info("tests made: " + Integer.toString(testResult.getRunCount()));
        TEST_LOGGER.info("tests failed: " + Integer.toString(testResult.getFailureCount()));
        TEST_LOGGER.info("====");
    }
}
