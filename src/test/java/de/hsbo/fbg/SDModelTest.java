package de.hsbo.fbg;

import org.junit.Test;

import de.hsbo.fbg.systemdynamics.model.ModelEntityType;
import de.hsbo.fbg.systemdynamics.model.Stock;
import de.hsbo.fbg.systemdynamics.model.Variable;
import de.hsbo.fbg.systemdynamics.simulation.Simulation;
import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Flow;
import de.hsbo.fbg.systemdynamics.model.Model;
import de.hsbo.fbg.systemdynamics.model.ModelEntity;
import java.util.HashMap;

public class SDModelTest {

	@Test
	public void modelCreationTest() {
		// Create a model with the parameters:
		// initialTime=0
		// finalTime=50
		// timeSteps=1
		// integrationType=Euler-Cauchy
		Model model = new Model(0, 10, 1, new EulerCauchyIntegration());

		try {
			// prey
			// Create prey population as stock
			Stock populationPrey = (Stock) model.createModelEntity(ModelEntityType.STOCK, "Population_Prey");
			populationPrey.setInitialValue(500);
			// Create prey births and deaths as flows
			Flow birthsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Births_Prey");
			Flow deathsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Deaths_Prey");
			// Add flows to prey population
			populationPrey.addInputFlow(birthsPrey);
			populationPrey.addOutputFlow(deathsPrey);
			// Create prey birthrate and deathrate as variable
			Variable expansionRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "BirthRate_Prey");
			expansionRatePrey.setCurrentValue(0.05);
			Variable lossRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "DeathRate_Prey");
			lossRatePrey.setCurrentValue(0.001);

			// predator
			// Create predator population as stock
			Stock populationPredator = (Stock) model.createModelEntity(ModelEntityType.STOCK, "Population_Predator");
			populationPredator.setInitialValue(50);
			// Create prey births and deaths as flows
			Flow birthsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Births_Predator");
			Flow deathsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Deaths_Predator");
			// Add flows to predator population
			populationPredator.addInputFlow(birthsPredator);
			populationPredator.addOutputFlow(deathsPredator);
			// Create prey birthrate and deathrate as variable
			Variable expansionRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE,
					"BirthRate_Predator");
			expansionRatePredator.setCurrentValue(0.0002);
			Variable lossRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE,
					"DeathRate_Predator");
			lossRatePredator.setCurrentValue(0.1);

			// Create meetings as variable
			Variable meetings = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "Meetings");

			// Approach for converting entity values by implementing IFunction
			// with an inner class
			Converter meetingsConverter = model.createConverter(meetings,
					() -> populationPrey.getCurrentValue() * populationPredator.getCurrentValue(), populationPrey,
					populationPredator);

			Converter birthsPreyConverter = model.createConverter(birthsPrey,
					() -> populationPrey.getCurrentValue() * expansionRatePrey.getCurrentValue(), populationPrey, populationPredator);

			Converter deathsPreyConverter = model.createConverter(deathsPrey,
					() -> meetings.getCurrentValue() * lossRatePrey.getCurrentValue(), meetings, lossRatePrey);

			Converter birthsPredatorConverter = model.createConverter(birthsPredator,
					() -> meetings.getCurrentValue() * expansionRatePredator.getCurrentValue(), meetings, expansionRatePredator);

			Converter deathsPredatorConverter = model.createConverter(deathsPredator,
					() -> populationPredator.getCurrentValue() * lossRatePredator.getCurrentValue(), populationPredator,
					lossRatePredator);

			Converter preyPopulationConverter = model.createStockConverter(populationPrey);

			Converter predatorPopulationConverter = model.createStockConverter(populationPredator);

			Simulation simulation = new Simulation(model);
			simulation.run();
//			HashMap<String, ModelEntity> modelEntities = model.getModelEntities();
//			System.out.println(modelEntities);

		} catch (ModelException e) {
			e.printStackTrace();
		}

	}
}
