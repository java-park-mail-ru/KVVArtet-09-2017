package project.websocket;

import project.gamemechanics.smartcontroller.SmartController;

import javax.validation.constraints.NotNull;

interface ObjectivePool<T> {

    T getElement();

    void addElement(@NotNull SmartController smartController);

    Boolean isEmpty();

    interface SmartControllersPool extends ObjectivePool<SmartController> {
    }
}
