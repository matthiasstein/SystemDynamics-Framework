package de.hsbo.fbg.systemdynamics.output;

import de.hsbo.fbg.systemdynamics.model.Model;
import de.hsbo.fbg.systemdynamics.model.Simulation;

/**
 * Interface that defines the methods for dealing with simulation events.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Matthias Stein</a>
 */
public interface SimulationEventListener {
    /**
     * Handles an event for the initialization of the simulation.
     *
     * @param model {@link Model} for the {@link Simulation}
     */
    void simulationInitialized(Model model);

    /**
     * Handles an event for a finished calculation of a time step.
     *
     * @param model {@link Model} for the {@link Simulation}
     */
    void timeStepCalculated(Model model);

    /**
     * Handles an event for a finished simulation.
     *
     * @param model {@link Model} for the {@link Simulation}
     */
    void simulationFinished(Model model);

}
