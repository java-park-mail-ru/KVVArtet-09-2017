package websocket;

import gamemechanics.smartcontroller.SmartController;

public interface ObjectivePool<T> {

    T getElement();

    void addMore();

    void addElement(SmartController smartController);

    Boolean isEmpty();

}
