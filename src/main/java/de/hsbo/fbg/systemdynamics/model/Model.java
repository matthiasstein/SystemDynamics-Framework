package de.hsbo.fbg.systemdynamics.model;

import java.util.HashMap;
import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.IFunction;
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
    private ArrayList<Converter> stockConverterList;
    private double initialTime;
    private double finalTime;
    private double timeSteps;
    private double currentTime;

    /**
     * Constructor.
     */
    public Model() {
        this.modelEntities = new HashMap<String, ModelEntity>();
        this.converterList = new ArrayList<Converter>();
        this.stockConverterList = new ArrayList<Converter>();
        this.initialTime = 0;
        this.currentTime = this.initialTime;
        this.finalTime = 100;
        this.timeSteps = 1;

    }

    /**
     * Constructor.
     *
     * @param initialTime initial time.
     * @param finalTime final time.
     * @param timeSteps length of a time step.
     * @param integrationType integration type.
     */
    public Model(double initialTime, double finalTime, double timeSteps, Integration integrationType) {
        this.modelEntities = new HashMap<String, ModelEntity>();
        this.converterList = new ArrayList<Converter>();
        this.stockConverterList = new ArrayList<Converter>();
        this.initialTime = initialTime;
        this.currentTime = initialTime;
        this.finalTime = finalTime;
        this.timeSteps = timeSteps;
    }

    /**
     * Method to create a new model entity.
     *
     * @param entityType entity type.
     * @param name entity name.
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

    /**
     * Method to prepare all values for the first timestep.
     */
    protected void prepareValuesForTimestep() {
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
     * Method to create a new converter.
     *
     * @param entity model entity.
     * @param function calculation function.
     * @param inputs input model entities.
     * @return the created converter.
     */
    public Converter createConverter(ModelEntity entity, IFunction function, ModelEntity... inputs) {
        Converter converter = new Converter(entity, function, inputs);
        this.addConverter(converter);
        entity.setConverter(converter);
        return converter;
    }

    /**
     * Method to create a new stock converter.
     *
     * @param stock stock.
     * @return the created stock converter.
     */
    public Converter createStockConverter(Stock stock, Integration integration) {
        stock.setIntegration(integration);
        integration.setDt(timeSteps);
        Converter converter = new Converter(stock, stock.getIntegration().getIntegrationFunction(stock));
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
     * Method to simulate the model. It calculates all added converters.
     */
    public void simulate() {
        // simulate model
        this.converterList.forEach((converter) -> {
            converter.convert();
        });
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
     * @param initialTime initial time to set.
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
     * @param finalTime final time to set.
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
     * @param timeSteps time step length to set.
     */
    public void setTimeSteps(double timeSteps) {
        this.timeSteps = timeSteps;
    }

//	/**
//	 *
//	 * @return integration type.
//	 */
//	public Integration getIntergrationType() {
//		return this.integration;
//	}
//	/**
//	 *
//	 * @param intergrationType
//	 *            integration type to set.
//	 */
//	public void setIntergrationType(Integration intergrationType) {
//		this.integration = intergrationType;
//	}
    /**
     *
     * @return current time.
     */
    public double getCurrentTime() {
        return this.currentTime;
    }

    /**
     *
     * @param currentTime current time to set.
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

    /**
     *
     * @return list of stock converters.
     */
    public ArrayList<Converter> getStockConverterList() {
        return this.stockConverterList;
    }

    /**
     * Method to update the current time by adding one time step.
     */
    protected void updateCurrentTime() {
        this.currentTime = this.currentTime + this.timeSteps;
    }

    /**
     * Method that controls if the final time has been reached.
     *
     * @return <tt>true</tt> only if the final time has been reached.
     */
    public boolean finalTimeReached() {
        return this.currentTime <= this.finalTime;
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

    /**
     * Prepare all initial model values for running the simulation.
     */
    protected void prepareInitialValues() {
        this.currentTime = this.initialTime;
        this.modelEntities.forEach((k, v) -> {
            v.setCurrentValue(v.getInitialValue());
        });
        for (Converter converter : this.stockConverterList) {
            ((Stock) converter.getTargetEntity()).getIntegration().setDt(timeSteps);
        }
    }

}
