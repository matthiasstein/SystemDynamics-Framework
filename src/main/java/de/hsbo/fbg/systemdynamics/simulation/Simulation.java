package de.hsbo.fbg.systemdynamics.simulation;

import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Model;
import de.hsbo.fbg.systemdynamics.output.CSVExporter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class that represents a system dynamics simulation and controls the
 * simulation execution
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
        CSVExporter csvExporter = new CSVExporter(";", "output.csv");
        this.model.prepareValuesForTimestep();
        executeConverters();
        this.model.updateCurrentTime();
        // add keys and first values to csv
        try {
            csvExporter.writeLine(this.model.getModelEntitiesKeys());
            csvExporter.writeLine(this.model.getModelEntitiesValues());
        } catch (IOException ex) {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (this.model.finalTimeReached()) {
            this.model.prepareValuesForTimestep();
            executeStockConverters();
            executeConverters();
            this.model.updateCurrentTime();
            System.out.println(this.model);
            // add values to csv
            try {
                csvExporter.writeLine(this.model.getModelEntitiesValues());
            } catch (IOException ex) {
                Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        csvExporter.saveFile();
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
