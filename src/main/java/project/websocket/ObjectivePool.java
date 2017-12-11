package project.websocket;

import project.gamemechanics.smartcontroller.SmartController;

public interface ObjectivePool<T> {

    T getElement();

    void addMore();

    void addElement(SmartController smartController);

    Boolean isEmpty();

}
