package de.hsbo.fbg.systemdynamics.simulation;

import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Model;

/**
 * class that represents a system dynamics simulation and controls the simulation execution
 *
 * @author Sebastian Drost, Matthias Stein
 */
public class Simulation {

    private Model model;

    /**
     *
     * @param model
     */
    public Simulation(Model model) {
        this.model = model;
    }

    /**
     * method to run the simulation
     */
    public void run() {
        this.model.prepareValuesForTimestep();
        executeConverters();
        this.model.updateCurrentTime();
        while (this.model.finalTimeReached()) {
            this.model.prepareValuesForTimestep();
            executeStockConverters();
            executeConverters();
            this.model.updateCurrentTime();
            System.out.println(this.model);
        }
    }

    /**
     * method to execute the stock converters
     */
    private void executeStockConverters() {
        for (Converter stockConverter : this.model.getStockConverterList()) {
            if (stockConverter != null && !stockConverter.getTargetEntity().isCurrentValueCalculated()) {
                stockConverter.convert();
            }
        }
    }

    /**
     * method to execute the converters
     */
    private void executeConverters() {
        for (Converter converter : this.model.getConverterList()) {
            if (converter != null && !converter.getTargetEntity().isCurrentValueCalculated()) {
                converter.convert();
            }
        }

    }

}
