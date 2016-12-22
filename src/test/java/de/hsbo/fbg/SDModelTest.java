package de.hsbo.fbg;

import org.junit.Test;

import de.hsbo.fbg.systemdynamics.model.ModelEntityType;
import de.hsbo.fbg.systemdynamics.model.Stock;
import de.hsbo.fbg.systemdynamics.model.Variable;
import de.hsbo.fbg.systemdynamics.simulation.Simulation;
import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import de.hsbo.fbg.systemdynamics.functions.IFunction;
import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Flow;
import de.hsbo.fbg.systemdynamics.model.Model;
import de.hsbo.fbg.systemdynamics.output.CSVExporter;
import java.io.IOException;

/**
 * sample
 *
 * @author Sebastian Drost, Matthias Stein
 */
public class SDModelTest {

    /**
     *
     */
    @Test
    public void modelCreationTest() {
        // Create a model with the parameters:
        // initialTime=0
        // finalTime=50
        // timeSteps=1
        // integrationType=Euler-Cauchy

        double populationPreyValue = 100;
        double expansionRatePreyValue = 0.001;
        double lossRatePreyValue = 0.001;

        double populationPredatorValue = 50;
        double expansionRatePredatorValue = 0.001;
        double lossRatePredatorValue = 0.001;

        double dt = 0.5;

        Model model = new Model(0, 5, dt, new EulerCauchyIntegration());

        try {
            // prey
            // Create prey population as stock
            Stock populationPrey = (Stock) model.createModelEntity(ModelEntityType.STOCK, "Population_Prey");
            populationPrey.setInitialValue(populationPreyValue);
            // Create prey births and deaths as flows
            Flow birthsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Births_Prey");
            Flow deathsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Deaths_Prey");
            // Add flows to prey population
            populationPrey.addInputFlow(birthsPrey);
            populationPrey.addOutputFlow(deathsPrey);
            // Create prey birthrate and deathrate as variable
            Variable expansionRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "BirthRate_Prey");
            expansionRatePrey.setCurrentValue(expansionRatePreyValue);
            Variable lossRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "DeathRate_Prey");
            lossRatePrey.setCurrentValue(lossRatePreyValue);

            // predator
            // Create predator population as stock
            Stock populationPredator = (Stock) model.createModelEntity(ModelEntityType.STOCK, "Population_Predator");
            populationPredator.setInitialValue(populationPredatorValue);
            // Create prey births and deaths as flows
            Flow birthsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Births_Predator");
            Flow deathsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Deaths_Predator");
            // Add flows to predator population
            populationPredator.addInputFlow(birthsPredator);
            populationPredator.addOutputFlow(deathsPredator);
            // Create prey birthrate and deathrate as variable
            Variable expansionRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE,
                    "BirthRate_Predator");
            expansionRatePredator.setCurrentValue(expansionRatePredatorValue);
            Variable lossRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE,
                    "DeathRate_Predator");
            lossRatePredator.setCurrentValue(lossRatePredatorValue);

            // Create meetings as variable
            Variable meetings = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "Meetings");

            // Approach for converting entity values by implementing IFunction
            // with an inner class
         
            Converter meetingsConverter = model.createConverter(meetings,new IFunction() {		
				@Override
				public double calculateEntityValue() {
					return populationPrey.getCurrentValue() * populationPredator.getCurrentValue();
				}
			}, populationPrey, populationPredator);
            
//          Converter meetingsConverter = model.createConverter(meetings,
//          () -> populationPrey.getCurrentValue() * populationPredator.getCurrentValue(), populationPrey,
//          populationPredator);
            
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

        } catch (ModelException e) {
            e.printStackTrace();
        }

    }
}
