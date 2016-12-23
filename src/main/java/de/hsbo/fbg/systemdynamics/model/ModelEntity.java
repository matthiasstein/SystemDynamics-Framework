package de.hsbo.fbg.systemdynamics.model;

/**
 * abstract class that represents a model entity
 * 
 * @author Sebastian Drost, Matthias Stein
 */
public abstract class ModelEntity implements Cloneable{

    private String name;
    private double currentValue;
    private double previousValue;
    private boolean currentValueCalculated;
    private Converter converter;

    /**
     *
     * @param name
     */
    protected ModelEntity(String name) {
        super();
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public double getCurrentValue() {
        return this.currentValue;
    }

    /**
     *
     * @param currentValue
     */
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    /**
     *
     * @return
     */
    public double getPreviousValue() {
        return this.previousValue;
    }

    /**
     *
     * @param previousValue
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
     * @param converter
     */
    protected void setConverter(Converter converter) {
        this.converter = converter;

    }

    /**
     *
     * @return
     */
    public Converter getConverter() {
        return this.converter;
    }

    /**
     *
     * @param b
     */
    public void setCurrentValueCalculated(boolean b) {
        this.currentValueCalculated = b;
    }

    /**
     *
     * @return
     */
    public boolean isCurrentValueCalculated() {
        return this.currentValueCalculated;
    }

	@Override
	public abstract  Object clone() throws CloneNotSupportedException;

    
    
    
}
