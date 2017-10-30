package gamemechanics.components.properties;

public class SingleValueProperty implements Property {
    private Integer property;

    public SingleValueProperty(Integer property) {
        this.property = property;
    }

    @Override
    public Integer getProperty() {
        return property;
    }

    @Override
    public Boolean setSingleProperty(Integer property) {
        if (property == null) {
            return false;
        }
        this.property = property;
        return true;
    }
}
