package de.hsbo.fbg.systemdynamics.model;

/**
 * Abstract class that represents a model entity.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public abstract class ModelEntity implements Cloneable {

    private String name;
    private double currentValue;
    private double previousValue;
    private boolean currentValueCalculated;
    private Converter converter;

    /**
     * Constructor.
     *
     * @param name model entity name.
     */
    protected ModelEntity(String name) {
        super();
        this.name = name;
    }

    /**
     *
     * @return model entity name.
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param name name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return current model entity value.
     */
    public double getCurrentValue() {
        return this.currentValue;
    }

    /**
     *
     * @param currentValue current value to set.
     */
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    /**
     *
     * @return previous model entity value.
     */
    public double getPreviousValue() {
        return this.previousValue;
    }

    /**
     *
     * @param previousValue previous value to set.
     */
    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    @Override
    public String toString() {
        return "ModelEntity{" + "name=" + this.name + ", value=" + this.currentValue + ", previousValue=" + this.previousValue + '}';
    }

    /**
     *
     * @param converter converter to set.
     */
    protected void setConverter(Converter converter) {
        this.converter = converter;

    }

    /**
     *
     * @return converter.
     */
    public Converter getConverter() {
        return this.converter;
    }

    /**
     *
     * @param b true or false.
     */
    public void setCurrentValueCalculated(boolean b) {
        this.currentValueCalculated = b;
    }

    /**
     *
     * @return true if value has already been calculated.
     */
    public boolean isCurrentValueCalculated() {
        return this.currentValueCalculated;
    }

    @Override
    public abstract Object clone() throws CloneNotSupportedException;

}
