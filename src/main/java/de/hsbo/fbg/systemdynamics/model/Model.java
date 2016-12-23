package de.hsbo.fbg.systemdynamics.model;

import java.util.HashMap;
import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import de.hsbo.fbg.systemdynamics.functions.IFunction;
import de.hsbo.fbg.systemdynamics.functions.Integration;
import java.util.List;

/**
 * class that represents the model
 *
 * @author Sebastian Drost, Matthias Stein
 */
public class Model {

	private HashMap<String, ModelEntity> modelEntities;
	private HashMap<String, ModelEntity> initialModelEntities;
	private ArrayList<Converter> converterList;
	private ArrayList<Converter> stockConverterList;
	private double initialTime;
	private double finalTime;
	private double timeSteps;
	private double currentTime;
	private Integration integration;

	/**
	 *
	 */
	public Model() {
		this.modelEntities = new HashMap<String, ModelEntity>();
		this.initialModelEntities = new HashMap<String, ModelEntity>();
		this.converterList = new ArrayList<Converter>();
		this.stockConverterList = new ArrayList<Converter>();
		this.initialTime = 0;
		this.currentTime = this.initialTime;
		this.finalTime = 100;
		this.timeSteps = 1;
		this.integration = new EulerCauchyIntegration();
		this.integration.setDt(timeSteps);

	}

	/**
	 *
	 * @param initialTime
	 * @param finalTime
	 * @param timeSteps
	 * @param integrationType
	 */
	public Model(double initialTime, double finalTime, double timeSteps, Integration integrationType) {
		this.modelEntities = new HashMap<String, ModelEntity>();
		this.initialModelEntities = new HashMap<String, ModelEntity>();
		this.converterList = new ArrayList<Converter>();
		this.stockConverterList = new ArrayList<Converter>();
		this.initialTime = initialTime;
		this.currentTime = initialTime;
		this.finalTime = finalTime;
		this.timeSteps = timeSteps;
		this.integration = integrationType;
		this.integration.setDt(timeSteps);
	}

	/**
	 *
	 * @param entityType
	 * @param name
	 * @return
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

	/**
	 *
	 */
	public void prepareValuesForTimestep() {
		this.modelEntities.forEach((k, v) -> {
			v.setPreviousValue(v.getCurrentValue());
			if (v instanceof Stock && this.currentTime == this.initialTime) {
				v.setCurrentValueCalculated(true);
			} else {
				v.setCurrentValueCalculated(false);
			}
		});
	}

	/**
	 *
	 * @param entity
	 * @param function
	 * @param inputs
	 * @return
	 */
	public Converter createConverter(ModelEntity entity, IFunction function, ModelEntity... inputs) {
		Converter converter = new Converter(entity, function, inputs);
		this.addConverter(converter);
		entity.setConverter(converter);
		return converter;
	}

	/**
	 *
	 * @param stock
	 * @return
	 */
	public Converter createStockConverter(Stock stock) {
		Converter converter = new Converter(stock, integration.getIntegrationFunction(stock));
		this.addStockConverter(converter);
		return converter;
	}

	private void addConverter(Converter converter) {
		this.converterList.add(converter);
	}

	private void addStockConverter(Converter converter) {
		this.stockConverterList.add(converter);
	}

	/**
	 *
	 */
	public void simulate() {
		// simulate model
		this.converterList.forEach((converter) -> {
			converter.convert();
		});
	}

	/**
	 *
	 * @return
	 */
	public HashMap<String, ModelEntity> getModelEntities() {
		return this.modelEntities;
	}

	/**
	 *
	 * @return
	 */
	public double getInitialTime() {
		return this.initialTime;
	}

	/**
	 *
	 * @param initialTime
	 */
	public void setInitialTime(double initialTime) {
		this.initialTime = initialTime;
	}

	/**
	 *
	 * @return
	 */
	public double getFinalTime() {
		return finalTime;
	}

	/**
	 *
	 * @param finalTime
	 */
	public void setFinalTime(double finalTime) {
		this.finalTime = finalTime;
	}

	/**
	 *
	 * @return
	 */
	public double getTimeSteps() {
		return timeSteps;
	}

	/**
	 *
	 * @param timeSteps
	 */
	public void setTimeSteps(double timeSteps) {
		this.timeSteps = timeSteps;
	}

	/**
	 *
	 * @return
	 */
	public Integration getIntergrationType() {
		return this.integration;
	}

	/**
	 *
	 * @param intergrationType
	 */
	public void setIntergrationType(Integration intergrationType) {
		this.integration = intergrationType;
	}

	/**
	 *
	 * @return
	 */
	public double getCurrentTime() {
		return this.currentTime;
	}

	/**
	 *
	 * @param currentTime
	 */
	public void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<Converter> getConverterList() {
		return this.converterList;
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<Converter> getStockConverterList() {
		return this.stockConverterList;
	}

	/**
	 *
	 */
	public void updateCurrentTime() {
		this.currentTime = this.currentTime + this.timeSteps;
	}

	/**
	 *
	 * @return
	 */
	public boolean finalTimeReached() {
		return this.currentTime <= this.finalTime;
	}

	@Override
	public String toString() {
		return "Model [modelEntities=" + this.modelEntities + "]";
	}

	public List<String> getModelEntitiesKeys() {
		return new ArrayList<String>(this.modelEntities.keySet());
	}

	public List<String> getModelEntitiesValues() {
		ArrayList<ModelEntity> modelEntities = new ArrayList<ModelEntity>(this.modelEntities.values());
		ArrayList<String> modelEntitiesValues = new ArrayList<String>();
		modelEntities.forEach((modelEntity) -> {
			modelEntitiesValues.add(String.valueOf(modelEntity.getCurrentValue()));
		});
		return modelEntitiesValues;
	}

	public void resetValues() {
		this.currentTime=this.initialTime;
		this.modelEntities.forEach((k, v) -> {
			v.setCurrentValue(initialModelEntities.get(k).getCurrentValue());
			v.setPreviousValue(initialModelEntities.get(k).getPreviousValue());
		});
	}

	public void saveInitialValues() {
		this.modelEntities.forEach((k, v) -> {
			try {
				this.initialModelEntities.put(k, (ModelEntity) v.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

}
