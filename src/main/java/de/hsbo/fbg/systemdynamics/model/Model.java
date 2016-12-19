package de.hsbo.fbg.systemdynamics.model;

import java.util.HashMap;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import de.hsbo.fbg.systemdynamics.functions.IFunction;
import de.hsbo.fbg.systemdynamics.functions.IntegrationType;

import java.util.ArrayList;

public class Model {

	private HashMap<String, ModelEntity> modelEntities;
	private ArrayList<Converter> converterList;
	private ArrayList<Converter> stockConverterList;
	private double initialTime;
	private double finalTime;
	private double timeSteps;
	private double currentTime;
	private IntegrationType intergrationType;

	public Model() {
		modelEntities = new HashMap<String, ModelEntity>();
		converterList = new ArrayList<Converter>();
		stockConverterList = new ArrayList<Converter>();
		initialTime = 0;
		currentTime = initialTime;
		finalTime = 100;
		timeSteps = 1;
		intergrationType = new EulerCauchyIntegration();
	}

	public Model(double initialTime, double finalTime, double timeSteps, IntegrationType integrationType) {
		modelEntities = new HashMap<String, ModelEntity>();
		converterList = new ArrayList<Converter>();
		stockConverterList = new ArrayList<Converter>();
		this.initialTime = initialTime;
		currentTime = initialTime;
		this.finalTime = finalTime;
		this.timeSteps = timeSteps;
		this.intergrationType = integrationType;
	}

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

	public void prepareValuesForTimestep() {
		this.modelEntities.forEach((k, v) -> {
			v.setPreviousValue(v.getCurrentValue());
			v.setCurrentValueCalculated(false);
		});
	}

	public Converter createConverter(ModelEntity entity, IFunction function, ModelEntity... inputs) {
		Converter converter = new Converter(entity, function);
		this.addConverter(converter);
		entity.setConverter(converter);
		return converter;
	}

	public Converter createStockConverter(Stock stock) {
		Converter converter = new Converter(stock, intergrationType.getIntegrationFunction(stock));
		this.addStockConverter(converter);
		return converter;
	}

	private void addConverter(Converter converter) {
		converterList.add(converter);
	}

	private void addStockConverter(Converter converter) {
		stockConverterList.add(converter);
	}

	public void simulate() {
		// simulate model
		converterList.forEach((converter) -> {
			converter.convert();
		});
	}

	public HashMap<String, ModelEntity> getModelEntities() {
		return modelEntities;
	}

	public double getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(double initialTime) {
		this.initialTime = initialTime;
	}

	public double getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(double finalTime) {
		this.finalTime = finalTime;
	}

	public double getTimeSteps() {
		return timeSteps;
	}

	public void setTimeSteps(double timeSteps) {
		this.timeSteps = timeSteps;
	}

	public IntegrationType getIntergrationType() {
		return intergrationType;
	}

	public void setIntergrationType(IntegrationType intergrationType) {
		this.intergrationType = intergrationType;
	}

	public double getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
	}

	public ArrayList<Converter> getConverterList() {
		return converterList;
	}

	public ArrayList<Converter> getStockConverterList() {
		return stockConverterList;
	}

	public void updateCurrentTime() {
		this.currentTime = currentTime + timeSteps;
	}

	public boolean finalTimeReached() {
		return currentTime <= finalTime;
	}
	
	@Override
	public String toString() {
		return "Model [modelEntities=" + modelEntities + "]";
	}

}
