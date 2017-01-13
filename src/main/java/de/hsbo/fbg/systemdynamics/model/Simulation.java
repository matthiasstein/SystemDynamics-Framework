package de.hsbo.fbg.systemdynamics.model;

import de.hsbo.fbg.systemdynamics.output.CSVExporter;
import de.hsbo.fbg.systemdynamics.output.IExporter;

/**
 * This class represents a system dynamics simulation and controls the
 * simulation execution.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public class Simulation {

    private Model model;
    private IExporter exporter;

    /**
     * Constructor.
     *
     * @param model the model object that describes the system dynamic model.
     */
    public Simulation(Model model, IExporter exporter) {
        this.model = model;
        this.exporter = exporter;
    }

    /**
     * Constructor.
     *
     * @param model the model object that describes the system dynamic model.
     */
    public Simulation(Model model) {
        this.model = model;
        this.exporter = new CSVExporter("output.csv", ";");
    }

    /**
     * This method runs the simulation.
     */
    public void run() {
        this.prepareInitialValues();

        // Prepare all values for the first time step and run the simulation for
        // it. The stock converters don't have to be execute, because for the
        // first time step their current value is the same as their initial
        // value.
        this.prepareValuesForFirstTimestep();
        executeConverters();

        this.exporter.clearContent();
        // add keys and first values to csv
        this.exporter.writeTimeStepValues(this.model.getModelEntitiesKeys());
        this.exporter.writeTimeStepValues(this.model.getModelEntitiesValues());
        while (this.finalTimeReached()) {
            this.updateCurrentTime();
            this.prepareValuesForTimestep();
            executeStockConverters();
            executeConverters();
            System.out.println(this.model);
            // add values to csv
            this.exporter.writeTimeStepValues(this.model.getModelEntitiesValues());
        }
        this.exporter.saveFile();
    }

    /**
     * Prepare all initial model values for running the simulation.
     */
    private void prepareInitialValues() {
        this.model.setCurrentTime(this.model.getInitialTime());
        this.model.getModelEntities().forEach((k, v) -> {
            v.setCurrentValue(v.getInitialValue());
            v.setCurrentValueCalculated(false);
        });
        this.model.getIntegration().setStocks(model.getStocks());
        this.model.getIntegration().setVariableConverter(model.getConverterList());
        this.model.getIntegration().setDt(this.model.getTimeSteps());
    }

    /**
     * Method to prepare all Stocks whose current value is already calculated
     * for the first timestep.
     */
    private void prepareValuesForFirstTimestep() {
        this.model.getModelEntities().forEach((k, v) -> {
            if (v instanceof Stock && this.model.getCurrentTime() == this.model.getInitialTime()) {
                v.setCurrentValueCalculated(true);
            }
        });

    }

    /**
     * Method to prepare all values for the next timestep.
     */
    private void prepareValuesForTimestep() {
        this.model.getModelEntities().forEach((k, v) -> {
            v.setPreviousValue(v.getCurrentValue());
            v.setCurrentValueCalculated(false);
        });
    }

    /**
     * Method to update the current time by adding one time step.
     */
    private void updateCurrentTime() {
        this.model.setCurrentTime(this.model.getCurrentTime() + this.model.getTimeSteps());
    }

    /**
     * Method that controls if the final time has been reached.
     *
     * @return <tt>true</tt> only if the final time has been reached.
     */
    public boolean finalTimeReached() {
        return this.model.getCurrentTime() < this.model.getFinalTime();
    }

    /**
     * Method to execute the stock converters.
     */
    private void executeStockConverters() {
        model.getIntegration().integrate();
    }

    /**
     * Method to execute the converters.
     */
    private void executeConverters() {
        for (Converter converter : this.model.getConverterList()) {
            if (converter != null && !converter.getTargetEntity().isCurrentValueCalculated()) {
                converter.convert();
            }
        }

    }

    /**
     * Method to set exporter class
     *
     * @param exporter exporter class
     */
    public void setExporter(IExporter exporter) {
        this.exporter = exporter;
    }

    /**
     * @return exporter.
     */
    public IExporter getExporter() {
        return this.exporter;
    }

}
