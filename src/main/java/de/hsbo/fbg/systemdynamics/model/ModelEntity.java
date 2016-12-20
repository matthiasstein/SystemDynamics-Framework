package de.hsbo.fbg.systemdynamics.model;

/**
 * abstract class that represents a model entity
 * 
 * @author Sebastian Drost, Matthias Stein
 */
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
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentValue() {
        return this.currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getPreviousValue() {
        return this.previousValue;
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    @Override
    public String toString() {
        return "ModelEntity{" + "name=" + this.name + ", value=" + this.currentValue + ", previousValue=" + this.previousValue + '}';
    }

    protected void setConverter(Converter converter) {
        this.converter = converter;

    }

    public Converter getConverter() {
        return this.converter;
    }

    public void setCurrentValueCalculated(boolean b) {
        this.currentValueCalculated = b;
    }

    public boolean isCurrentValueCalculated() {
        return this.currentValueCalculated;
    }
}
