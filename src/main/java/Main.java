import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statemachine.PendingStack;
import statemachine.StateStack;
import states.State;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // образец создания стека(сразу вкладываем изначальное состояние)
        final PendingStack stack = new StateStack();
        stack.pushState(State.StateId.SI_TITLE);
        stack.update();
    }
}