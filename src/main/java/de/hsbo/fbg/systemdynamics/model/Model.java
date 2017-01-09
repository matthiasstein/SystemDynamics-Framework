package de.hsbo.fbg.systemdynamics.model;

import java.util.HashMap;
import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import de.hsbo.fbg.systemdynamics.functions.Integration;
import java.text.DecimalFormat;
import java.util.List;

/**
 * This class represents a model.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public class Model {

	private HashMap<String, ModelEntity> modelEntities;
	private ArrayList<Converter> converterList;
	private double initialTime;
	private double finalTime;
	private double timeSteps;
	private double currentTime;
	private Integration integration;

	/**
	 * Constructor.
	 */
	public Model() {
		this.modelEntities = new HashMap<String, ModelEntity>();
		this.converterList = new ArrayList<Converter>();
		this.initialTime = 0;
		this.currentTime = this.initialTime;
		this.finalTime = 100;
		this.timeSteps = 1;
		this.integration = new EulerCauchyIntegration();
	}

	/**
	 * Constructor.
	 *
	 * @param initialTime
	 *            initial time.
	 * @param finalTime
	 *            final time.
	 * @param timeSteps
	 *            length of a time step.
	 */
	public Model(double initialTime, double finalTime, double timeSteps, Integration integration) {
		this.modelEntities = new HashMap<String, ModelEntity>();
		this.converterList = new ArrayList<Converter>();
		this.initialTime = initialTime;
		this.currentTime = initialTime;
		this.finalTime = finalTime;
		this.timeSteps = timeSteps;
		this.integration = integration;
	}

	/**
	 * Method to create a new model entity.
	 *
	 * @param entityType
	 *            entity type.
	 * @param name
	 *            entity name.
	 * @return the created model entity.
	 * @throws ModelException
	 */
	public ModelEntity createModelEntity(ModelEntityType entityType, String name) throws ModelException {
		ModelEntity modelEntity;
		switch (entityType) {
		case STOCK:
			modelEntity = new Stock(name);
			break;
		case FLOW:
			modelEntity = new Flow(name);
			break;
		case VARIABLE:
			modelEntity = new Variable(name);
			break;
		default:
			return null;
		}
		this.addModelEntity(modelEntity);
		return modelEntity;
	}

	private void addModelEntity(ModelEntity modelEntity) throws ModelException {
		if (!existsModelEntity(modelEntity)) {
			this.modelEntities.put(modelEntity.getName(), modelEntity);
		} else {
			throw new ModelException(ModelException.DUPLICATE_MODEL_ENTITY_EXCEPTION);
		}
	}

	private boolean existsModelEntity(ModelEntity modelEntity) {
		return this.modelEntities.containsKey(modelEntity.getName());
	}

	public List<Stock> getStocks() {
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		ArrayList<ModelEntity> modelEntities = new ArrayList<ModelEntity>(this.modelEntities.values());
		modelEntities.forEach((modelEntity) -> {
			if (modelEntity instanceof Stock) {
				stocks.add((Stock) modelEntity);
			}

		});
		return stocks;
	}

	/**
	 * Method to create a new converter.
	 *
	 * @param entity
	 *            model entity.
	 * @param inputs
	 *            input model entities.
	 * @return the created converter.
	 */
	public Converter createConverter(ModelEntity entity, ModelEntity... inputs) {
		Converter converter = new Converter(entity, inputs);
		this.addConverter(converter);
		entity.setConverter(converter);
		return converter;
	}

	private void addConverter(Converter converter) {
		this.converterList.add(converter);
	}

	/**
	 *
	 * @return model entities.
	 */
	public HashMap<String, ModelEntity> getModelEntities() {
		return this.modelEntities;
	}

	/**
	 *
	 * @return initial time.
	 */
	public double getInitialTime() {
		return this.initialTime;
	}

	/**
	 *
	 * @param initialTime
	 *            initial time to set.
	 */
	public void setInitialTime(double initialTime) {
		this.initialTime = initialTime;
	}

	/**
	 *
	 * @return final time.
	 */
	public double getFinalTime() {
		return finalTime;
	}

	/**
	 *
	 * @param finalTime
	 *            final time to set.
	 */
	public void setFinalTime(double finalTime) {
		this.finalTime = finalTime;
	}

	/**
	 *
	 * @return time step length.
	 */
	public double getTimeSteps() {
		return timeSteps;
	}

	/**
	 *
	 * @param timeSteps
	 *            time step length to set.
	 */
	public void setTimeSteps(double timeSteps) {
		this.timeSteps = timeSteps;
	}

	/**
	 *
	 * @return current time.
	 */
	public double getCurrentTime() {
		return this.currentTime;
	}

	/**
	 *
	 * @param currentTime
	 *            current time to set.
	 */
	protected void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 *
	 * @return list of converters.
	 */
	public ArrayList<Converter> getConverterList() {
		return this.converterList;
	}

	@Override
	public String toString() {
		return "Model [modelEntities=" + this.modelEntities + "]";
	}

	/**
	 *
	 * @return list of model entities keys.
	 */
	public List<String> getModelEntitiesKeys() {
		return new ArrayList<String>(this.modelEntities.keySet());
	}

	/**
	 *
	 * @return list of model entity values.
	 */
	public List<String> getModelEntitiesValues() {
		ArrayList<ModelEntity> modelEntities = new ArrayList<ModelEntity>(this.modelEntities.values());
		ArrayList<String> modelEntitiesValues = new ArrayList<String>();
		modelEntities.forEach((modelEntity) -> {
			modelEntitiesValues.add(new DecimalFormat("#.######").format(modelEntity.getCurrentValue()));
		});
		return modelEntitiesValues;
	}

	public Integration getIntegration() {
		return integration;
	}

	public void setIntegration(Integration integration) {
		this.integration = integration;
	}

}
