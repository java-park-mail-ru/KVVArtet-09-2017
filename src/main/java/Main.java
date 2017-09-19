import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statemachine.StateStack;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final Result testResult = JUnitCore.runClasses(StateStack.StateStackTest.class);
        for (Failure fail : testResult.getFailures()) {
            LOGGER.error(fail.toString());
        }
        LOGGER.info("====");
        LOGGER.info("tests made: " + Integer.toString(testResult.getRunCount()));
        LOGGER.info("tests failed: " + Integer.toString(testResult.getFailureCount()));
    }
}