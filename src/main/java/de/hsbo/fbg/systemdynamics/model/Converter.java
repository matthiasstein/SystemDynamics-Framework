package de.hsbo.fbg.systemdynamics.model;

import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.IFunction;

/**
 * This class represents a converter.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
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
	 * @param entity
	 *            target model entity.
	 * @param inputs
	 *            input model entities.
	 */
	protected Converter(ModelEntity entity, ModelEntity... inputs) {
		this.targetEntity = entity;
		this.inputs = new ArrayList<ModelEntity>();
		for (ModelEntity modelEntity : inputs) {
			this.inputs.add(modelEntity);
		}
	}

	/**
	 * Adds a function for calculating the current value of the target
	 * ModelEntity.
	 * 
	 * @param function
	 *            function for the target ModelEntity's current value
	 */
	public void setFunction(IFunction function) {
		this.function = function;
	}

	/**
	 * Method to convert the target model entity value.
	 */
	protected void convert() {
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

	}

	/**
	 * Method to add multiple input model entities.
	 *
	 * @param inputs
	 *            model entities.
	 * @throws ModelException
	 */
	protected void addInputs(ModelEntity... inputs) throws ModelException {
		for (ModelEntity f : inputs) {
			this.addInput(f);
		}
	}

	private void addInput(ModelEntity input) throws ModelException {
		if (inputAlreadyAdded(input)) {
			throw new ModelException(ModelException.DUPLICATE_VARIABLE_EXCEPTION);
		} else {
			this.inputs.add(input);
		}
	}

	private boolean inputAlreadyAdded(ModelEntity variable) {
		return inputs.contains(variable);
	}

	/**
	 *
	 * @return target entity.
	 */
	public ModelEntity getTargetEntity() {
		return this.targetEntity;
	}

	public double getMinLimitValue() {
		return minLimitValue;
	}

	public void setMinLimitValue(double minLimitValue) {
		this.minLimitValue = minLimitValue;
	}

	public double getMaxLimitValue() {
		return maxLimitValue;
	}

	public void setMaxLimitValue(double maxLimitValue) {
		this.maxLimitValue = maxLimitValue;
	}

}
