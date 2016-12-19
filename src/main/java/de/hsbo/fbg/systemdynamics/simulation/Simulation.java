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
		while (!this.model.finalTimeReached()) {
			this.model.prepareValuesForTimestep();
			executeStockConverters();
			executeConverters();
			this.model.updateCurrentTime();
		}
	}

	private void executeStockConverters() {
		for (Converter stockConverter : this.model.getStockConverterList()) {
			stockConverter.convert();
		}
	}

	private void executeConverters() {
		for (Converter converters : this.model.getConverterList()) {
			converters.convert();
		}

	}

}
