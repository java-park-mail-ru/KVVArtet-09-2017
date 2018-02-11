package project.websocket;

import project.gamemechanics.smartcontroller.SmartController;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
interface ObjectivePool<T> {

    @NotNull T getElement();

    void addElement(@NotNull SmartController smartController);

    @NotNull Boolean isEmpty();

    interface SmartControllersPool extends ObjectivePool<SmartController> {
    }
}
