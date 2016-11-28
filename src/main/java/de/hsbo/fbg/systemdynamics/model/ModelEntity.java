package de.hsbo.fbg.systemdynamics.model;

public abstract class ModelEntity {

    private String name;
    private double value;
    private double previousValue;

    public ModelEntity(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    @Override
    public String toString() {
        return "ModelEntity{" + "name=" + name + ", value=" + value + ", previousValue=" + previousValue + '}';
    }

}
