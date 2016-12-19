package de.hsbo.fbg.systemdynamics.model;

public abstract class ModelEntity {

    private String name;
    private double currentValue;
    private double previousValue;
    private boolean currentValueCalculated;
    private Converter converter;

    protected ModelEntity(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    @Override
    public String toString() {
        return "ModelEntity{" + "name=" + name + ", value=" + currentValue + ", previousValue=" + previousValue + '}';
    }

    protected void setConverter(Converter converter) {
        this.converter = converter;

    }

    public Converter getConverter() {
        return converter;
    }

    public void setCurrentValueCalculated(boolean b) {
        this.currentValueCalculated = b;
    }

    public boolean isCurrentValueCalculated() {
        return currentValueCalculated;
    }
}
