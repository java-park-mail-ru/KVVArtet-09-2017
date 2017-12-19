package project.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CharacterListState extends AbstractState {
    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterListState.class);

    public CharacterListState() {
        super();

    }
}
