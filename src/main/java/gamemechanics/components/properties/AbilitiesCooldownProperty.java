package gamemechanics.components.properties;

import java.util.Map;

public class AbilitiesCooldownProperty extends MapProperty {
    public AbilitiesCooldownProperty(Map<Integer, Integer> properties) {
        super(properties);
    }

    @Override
    public Boolean modifyByAddition(Integer toAdd) {
        for (Integer key : getPropertyMap().keySet()) {
            if (getPropertyMap().get(key) > 0) {
                getPropertyMap().replace(key, getPropertyMap().get(key) + toAdd);
            }
        }
        return true;
    }
}
