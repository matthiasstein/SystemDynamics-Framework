package de.hsbo.fbg.systemdynamics.simulation;

import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Model;

public class Simulation {

    private Model model;

    public Simulation(Model model) {
        this.model = model;
    }

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

    private void executeStockConverters() {
        for (Converter stockConverter : this.model.getStockConverterList()) {
            if (stockConverter != null && !stockConverter.getTargetEntity().isCurrentValueCalculated()) {
                stockConverter.convert();
            }
        }
    }

    private void executeConverters() {
        for (Converter converter : this.model.getConverterList()) {
            if (converter != null && !converter.getTargetEntity().isCurrentValueCalculated()) {
                converter.convert();
            }
        }

    }

}
