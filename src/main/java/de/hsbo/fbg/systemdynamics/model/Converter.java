package de.hsbo.fbg.systemdynamics.model;

import java.util.ArrayList;
import java.util.Collections;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.IFunction;

/**
 * This class represents a converter that calculates the current value of a target {@link ModelEntity}
 * if all inputs has been already calculated.
 * The calculation depends on the function that has been delivered to the converter.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class Converter {

    private IFunction function;

    private ModelEntity targetEntity;
    ArrayList<ModelEntity> inputs;
    private Double minLimitValue;
    private Double maxLimitValue;

    /**
     * Constructor.
     *
     * @param entity target model entity.
     * @param inputs input model entities.
     */
    protected Converter(ModelEntity entity, ModelEntity... inputs) {
        this.targetEntity = entity;
        this.inputs = new ArrayList<ModelEntity>();
        Collections.addAll(this.inputs, inputs);
    }

    /**
     * Adds a function for calculating the current value of the target
     * ModelEntity.
     *
     * @param function function for the target ModelEntity's current value.
     */
    public void setFunction(IFunction function) {
        this.function = function;
    }

    /**
     * @return function for the target ModelEntity's current value.
     */
    public IFunction getFunction() {
        return function;
    }


    /**
     * Method to convert the target model entity value.
     */
    public void convert() {
        for (ModelEntity input : this.inputs) {
            if (!input.isCurrentValueCalculated() && input.getConverter() != null) {
                input.getConverter().convert();
            }
        }
        double calculatedValue = function.calculateEntityValue();

        // Check if there are any limits for the value that has to be converted.
        if (minLimitValue != null && minLimitValue < calculatedValue) {
            this.targetEntity.setCurrentValue(this.minLimitValue);
        } else if (maxLimitValue != null && calculatedValue > maxLimitValue) {
            this.targetEntity.setCurrentValue(maxLimitValue);
        } else {
            this.targetEntity.setCurrentValue(calculatedValue);
        }
        this.targetEntity.setCurrentValueCalculated(true);

    }

    /**
     * Method to add multiple input model entities.
     *
     * @param inputs model entities.
     * @throws ModelException model exception.
     */
    protected void addInputs(ModelEntity... inputs) throws ModelException {
        for (ModelEntity f : inputs) {
            this.addInput(f);
        }
    }

    /**
     * Method to add an input for the target entity.
     *
     * @param input input entity.
     * @throws ModelException model exception.
     */
    private void addInput(ModelEntity input) throws ModelException {
        if (inputAlreadyAdded(input)) {
            throw new ModelException(ModelException.DUPLICATE_VARIABLE_EXCEPTION);
        } else {
            this.inputs.add(input);
        }
    }

    /**
     * Method to determine if a ModelEntity has been already added to the Converter.
     *
     * @param variable ModelEntity to check.
     * @return true if the ModelEntity already exists.
     */
    private boolean inputAlreadyAdded(ModelEntity variable) {
        return inputs.contains(variable);
    }

    /**
     * @return target entity.
     */
    public ModelEntity getTargetEntity() {
        return this.targetEntity;
    }

    /**
     * @return minimum limit that will be calculated by the converter.
     */
    public double getMinLimitValue() {
        return minLimitValue;
    }

    /**
     * @param minLimitValue minimum limit to set.
     */
    public void setMinLimitValue(double minLimitValue) {
        this.minLimitValue = minLimitValue;
    }

    /**
     * @return maximum limit that will be calculated by the converter.
     */
    public double getMaxLimitValue() {
        return maxLimitValue;
    }

    /**
     * @param maxLimitValue maximum limit to set.
     */
    public void setMaxLimitValue(double maxLimitValue) {
        this.maxLimitValue = maxLimitValue;
    }

}
