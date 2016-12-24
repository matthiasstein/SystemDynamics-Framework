package de.hsbo.fbg.systemdynamics.model;

import de.hsbo.fbg.systemdynamics.output.CSVExporter;

/**
 * This class represents a system dynamics simulation and controls the
 * simulation execution.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public class Simulation {

	private Model model;

	/**
	 * Constructor.
	 *
	 * @param model
	 *            the model object that describes the system dynamic model.
	 */
	public Simulation(Model model) {
		this.model = model;
	}

	/**
	 * This method runs the simulation.
	 */
	public void run() {
		CSVExporter csvExporter = new CSVExporter("output.csv", ";");
		// if (initialRun) {
		// this.model.saveInitialValues();
		// } else {
		// this.model.resetValues();
		// }
		this.model.prepareInitialValues();
		this.model.prepareValuesForTimestep();
		executeConverters();
		this.model.updateCurrentTime();
		// add keys and first values to csv
		csvExporter.writeLine(this.model.getModelEntitiesKeys());
		csvExporter.writeLine(this.model.getModelEntitiesValues());
		while (this.model.finalTimeReached()) {
			this.model.prepareValuesForTimestep();
			executeStockConverters();
			executeConverters();
			this.model.updateCurrentTime();
			System.out.println(this.model);
			// add values to csv
			csvExporter.writeLine(this.model.getModelEntitiesValues());
		}
		csvExporter.saveFile();
	}

	/**
	 * Method to execute the stock converters.
	 */
	private void executeStockConverters() {
		for (Converter stockConverter : this.model.getStockConverterList()) {
			if (stockConverter != null && !stockConverter.getTargetEntity().isCurrentValueCalculated()) {
				stockConverter.convert();
			}
		}
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

}
