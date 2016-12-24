package de.hsbo.fbg;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbo.fbg.systemdynamics.model.ModelEntityType;
import de.hsbo.fbg.systemdynamics.model.Simulation;
import de.hsbo.fbg.systemdynamics.model.Stock;
import de.hsbo.fbg.systemdynamics.model.Variable;
import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import de.hsbo.fbg.systemdynamics.functions.IFunction;
import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Flow;
import de.hsbo.fbg.systemdynamics.model.Model;
import de.hsbo.fbg.systemdynamics.model.ModelEntity;
import java.util.HashMap;

/**
 * Class to test the simulation.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public class SimulationTest {

    private Model model;

    private final String POPULATION_PREY_KEY = "Population_Prey";
    private final String BIRTHS_PREY_KEY = "Births_Prey";
    private final String DEATHS_PREY_KEY = "Deaths_Prey";
    private final String BIRTH_RATE_PREY_KEY = "BirthRate_Prey";
    private final String DEATH_RATE_PREY_KEY = "DeathRate_Prey";

    private final String POPULATION_PREDATOR_KEY = "Population_Predator";
    private final String BIRTHS_PREDATOR_KEY = "Births_Predator";
    private final String DEATHS_PREDATOR_KEY = "Deaths_Predator";
    private final String BIRTH_RATE_PREDATOR_KEY = "BirthRate_Predator";
    private final String DEATH_RATE_PREDATOR_KEY = "DeathRate_Predator";

    private final String MEETINGS_KEY = "Meetings";

    /**
     * Method to prepare the test simulation model.
     */
    @Before
    public void prepareValues() {
        double populationPreyValue = 100;
        double expansionRatePreyValue = 0.001;
        double lossRatePreyValue = 0.001;

        double populationPredatorValue = 50;
        double expansionRatePredatorValue = 0.001;
        double lossRatePredatorValue = 0.001;

        double dt = 0.5;

        // Create a model with the parameters:
        // initialTime=0
        // finalTime=50
        // timeSteps=1
        // integrationType=Euler-Cauchy
        model = new Model(0, 5, dt, new EulerCauchyIntegration());
        try {
            // prey
            // Create prey population as stock
            Stock populationPrey = (Stock) model.createModelEntity(ModelEntityType.STOCK, POPULATION_PREY_KEY);
            populationPrey.setInitialValue(populationPreyValue);
            // Create prey births and deaths as flows
            Flow birthsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, BIRTHS_PREY_KEY);
            Flow deathsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, DEATHS_PREY_KEY);
            // Add flows to prey population
            populationPrey.addInputFlow(birthsPrey);
            populationPrey.addOutputFlow(deathsPrey);
            // Create prey birthrate and deathrate as variable
            Variable expansionRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE,
                    BIRTH_RATE_PREY_KEY);
            expansionRatePrey.setCurrentValue(expansionRatePreyValue);
            Variable lossRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, DEATH_RATE_PREY_KEY);
            lossRatePrey.setCurrentValue(lossRatePreyValue);

            // predator
            // Create predator population as stock
            Stock populationPredator = (Stock) model.createModelEntity(ModelEntityType.STOCK, POPULATION_PREDATOR_KEY);
            populationPredator.setInitialValue(populationPredatorValue);
            // Create prey births and deaths as flows
            Flow birthsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, BIRTHS_PREDATOR_KEY);
            Flow deathsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, DEATHS_PREDATOR_KEY);
            // Add flows to predator population
            populationPredator.addInputFlow(birthsPredator);
            populationPredator.addOutputFlow(deathsPredator);
            // Create prey birthrate and deathrate as variable
            Variable expansionRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE,
                    BIRTH_RATE_PREDATOR_KEY);
            expansionRatePredator.setCurrentValue(expansionRatePredatorValue);
            Variable lossRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE,
                    DEATH_RATE_PREDATOR_KEY);
            lossRatePredator.setCurrentValue(lossRatePredatorValue);

            // Create meetings as variable
            Variable meetings = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, MEETINGS_KEY);

            // Approach for converting entity values by implementing IFunction
            // with an inner class
            Converter meetingsConverter = model.createConverter(meetings, new IFunction() {
                @Override
                public double calculateEntityValue() {
                    return populationPrey.getCurrentValue() * populationPredator.getCurrentValue();
                }
            }, populationPrey, populationPredator);

            Converter birthsPreyConverter = model.createConverter(birthsPrey,
                    () -> populationPrey.getCurrentValue() * expansionRatePrey.getCurrentValue(), populationPrey,
                    populationPredator);

            Converter deathsPreyConverter = model.createConverter(deathsPrey,
                    () -> meetings.getCurrentValue() * lossRatePrey.getCurrentValue(), meetings, lossRatePrey);

            Converter birthsPredatorConverter = model.createConverter(birthsPredator,
                    () -> meetings.getCurrentValue() * expansionRatePredator.getCurrentValue(), meetings,
                    expansionRatePredator);

            Converter deathsPredatorConverter = model.createConverter(deathsPredator,
                    () -> populationPredator.getCurrentValue() * lossRatePredator.getCurrentValue(), populationPredator,
                    lossRatePredator);

            Converter preyPopulationConverter = model.createStockConverter(populationPrey);

            Converter predatorPopulationConverter = model.createStockConverter(populationPredator);
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to run simulation test.
     */
    @Test
    public void simulationRunTest() {
        double error = 0.001;

        Simulation simulation = new Simulation(model);

        model.setFinalTime(0);
        simulation.run();
        HashMap<String, ModelEntity> entities = model.getModelEntities();

        Assert.assertThat(entities.get(POPULATION_PREY_KEY).getCurrentValue(), Matchers.equalTo(100.));
        Assert.assertThat(entities.get(BIRTH_RATE_PREY_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(DEATH_RATE_PREY_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(BIRTHS_PREY_KEY).getCurrentValue(), Matchers.equalTo(0.1));
        Assert.assertThat(entities.get(DEATHS_PREY_KEY).getCurrentValue(), Matchers.equalTo(5.));

        Assert.assertThat(entities.get(POPULATION_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(50.));
        Assert.assertThat(entities.get(BIRTH_RATE_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(DEATH_RATE_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(BIRTHS_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(5.));
        Assert.assertThat(entities.get(DEATHS_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(0.05));

        Assert.assertThat(entities.get(MEETINGS_KEY).getCurrentValue(), Matchers.equalTo(5000.));

        model.setFinalTime(0.5);
        System.out.println("Run Simulation 1");
        simulation.run();
        entities = model.getModelEntities();

        Assert.assertThat(entities.get(POPULATION_PREY_KEY).getCurrentValue(), Matchers.closeTo(97.5500, error));
        Assert.assertThat(entities.get(BIRTH_RATE_PREY_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(DEATH_RATE_PREY_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(BIRTHS_PREY_KEY).getCurrentValue(), Matchers.closeTo(0.0976, error));
        Assert.assertThat(entities.get(DEATHS_PREY_KEY).getCurrentValue(), Matchers.closeTo(5.1189, error));

        Assert.assertThat(entities.get(POPULATION_PREDATOR_KEY).getCurrentValue(), Matchers.closeTo(52.4750, error));
        Assert.assertThat(entities.get(BIRTH_RATE_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(DEATH_RATE_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(BIRTHS_PREDATOR_KEY).getCurrentValue(), Matchers.closeTo(5.1189, error));
        Assert.assertThat(entities.get(DEATHS_PREDATOR_KEY).getCurrentValue(), Matchers.closeTo(0.0525, error));

        Assert.assertThat(entities.get(MEETINGS_KEY).getCurrentValue(), Matchers.closeTo(5118.9363, 0.001));

        model.setFinalTime(2);
        System.out.println("Run Simulation 2");
        simulation.run();
        entities = model.getModelEntities();

        Assert.assertThat(entities.get(POPULATION_PREY_KEY).getCurrentValue(), Matchers.closeTo(89.8561, error));
        Assert.assertThat(entities.get(BIRTH_RATE_PREY_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(DEATH_RATE_PREY_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(BIRTHS_PREY_KEY).getCurrentValue(), Matchers.closeTo(0.0899, error));
        Assert.assertThat(entities.get(DEATHS_PREY_KEY).getCurrentValue(), Matchers.closeTo(5.4119, error));

        Assert.assertThat(entities.get(POPULATION_PREDATOR_KEY).getCurrentValue(), Matchers.closeTo(60.2289, error));
        Assert.assertThat(entities.get(BIRTH_RATE_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(DEATH_RATE_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(0.001));
        Assert.assertThat(entities.get(BIRTHS_PREDATOR_KEY).getCurrentValue(), Matchers.closeTo(5.4119, error));
        Assert.assertThat(entities.get(DEATHS_PREDATOR_KEY).getCurrentValue(), Matchers.closeTo(0.0602, error));

        Assert.assertThat(entities.get(MEETINGS_KEY).getCurrentValue(), Matchers.closeTo(5411.9328, 0.001));

    }
}
