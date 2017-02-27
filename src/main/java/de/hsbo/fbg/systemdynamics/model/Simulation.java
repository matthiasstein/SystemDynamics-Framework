package de.hsbo.fbg.systemdynamics.model;

import de.hsbo.fbg.systemdynamics.output.SimulationEventListener;

import java.util.ArrayList;

/**
 * This class represents a system dynamics simulation and controls the
 * simulation execution.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class Simulation {

    private Model model;
    private ArrayList<SimulationEventListener> simulationListener;


    /**
     * Constructor.
     *
     * @param model the model object that describes the system dynamic model.
     */
    public Simulation(Model model) {
        this.model = model;
        this.simulationListener = new ArrayList<>();
    }

    /**
     * This method runs the simulation.
     */
    public void run() {
        this.prepareInitialValues();

        // Prepare all values for the first time step and run the simulation for
        // it. The integration don't have to be executed, because for the
        // first time step the stocks current value is the same as their initial
        // value.
        this.prepareValuesForFirstTimestep();
        this.fireSimulationInitializedEvent(this.model);
        this.executeConverters();
        this.fireTimeStepCalculatedEvent(this.model);
        while (this.finalTimeReached()) {
            this.updateCurrentTime();
            this.prepareValuesForTimestep();
            this.model.getIntegration().integrate();
            this.executeConverters();
            System.out.println(this.model);
            this.fireTimeStepCalculatedEvent(model);
        }
        this.fireSimulationFinishedEvent(model);
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
     * Adds an listener that handles simulation events.
     *
     * @param listener {@link SimulationEventListener}
     */
    public void addSimulationEventListener(SimulationEventListener listener) {
        this.simulationListener.add(listener);
    }

    /**
     * Removes a {@link SimulationEventListener}.
     *
     * @param listener {@link SimulationEventListener}
     */
    public void removeSimulationEventListener(SimulationEventListener listener) {
        this.simulationListener.remove(listener);
    }

    /**
     * Fires an event for the initialization of the simulation.
     *
     * @param model {@link Model} for the simulation
     */
    private void fireSimulationInitializedEvent(Model model) {
        this.simulationListener.forEach(listener -> listener.simulationInitialized(model));
    }

    /**
     * Fires an event for a finished calculation of a time step.
     *
     * @param model {@link Model} for the simulation
     */
    private void fireTimeStepCalculatedEvent(Model model) {
        this.simulationListener.forEach(listener -> listener.timeStepCalculated(model));
    }

    /**
     * Fires an event for a finished simulation.
     *
     * @param model {@link Model} for the simulation
     */
    private void fireSimulationFinishedEvent(Model model) {
        this.simulationListener.forEach(listener -> listener.simulationFinished(model));
    }

}
