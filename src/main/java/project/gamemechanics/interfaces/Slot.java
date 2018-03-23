package project.gamemechanics.interfaces;

import project.gamemechanics.components.properties.SingleValueProperty;

import java.util.Map;

public interface Slot extends PropertyProvider {

    Map<Integer, SingleValueProperty> getAllProperties();


}
