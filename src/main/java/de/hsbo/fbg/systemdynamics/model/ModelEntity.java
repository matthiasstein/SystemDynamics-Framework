package de.hsbo.fbg.systemdynamics.model;

/**
 * Abstract class that represents an entity of the simulation model.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public abstract class ModelEntity implements Cloneable {

    private String name;
    private double currentValue;
    private double previousValue;
    private double initialValue;
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
     * @return model entity name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name name to set.
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * @return current model entity value.
     */
    public double getCurrentValue() {
        return this.currentValue;
    }

    /**
     * @param currentValue current value to set.
     */
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * @return previous model entity value.
     */
    public double getPreviousValue() {
        return this.previousValue;
    }

    /**
     * @param previousValue previous value to set.
     */
    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    /**
     * @param converter converter to set.
     */
    protected void setConverter(Converter converter) {
        this.converter = converter;
    }

    /**
     * @return converter.
     */
    public Converter getConverter() {
        return this.converter;
    }

    /**
     * @param b true or false.
     */
    public void setCurrentValueCalculated(boolean b) {
        this.currentValueCalculated = b;
    }

    /**
     * @return true if value has already been calculated.
     */
    protected boolean isCurrentValueCalculated() {
        return this.currentValueCalculated;
    }

    /**
     * @return intitial value.
     */
    public double getInitialValue() {
        return initialValue;
    }

    /**
     * @param initialValue initial value to set.
     */
    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    @Override
    public abstract Object clone() throws CloneNotSupportedException;

    @Override
    public String toString() {
        return "ModelEntity{" + "name=" + this.name + ", value=" + this.currentValue + ", previousValue=" + this.previousValue + '}';
    }

}
