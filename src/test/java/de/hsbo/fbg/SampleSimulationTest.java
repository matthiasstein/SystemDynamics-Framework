package de.hsbo.fbg;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import de.hsbo.fbg.systemdynamics.model.*;
import de.hsbo.fbg.systemdynamics.output.CSVExporter;
import de.hsbo.fbg.systemdynamics.output.ChartViewer;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * Class to test the simulation.
 *
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class SampleSimulationTest {

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
     * Method to run the sample simulation test.
     */
    @Test
    public void simulationRunTest() {

        // Create a model with the parameters:
        Model model = new Model(0, 10, 0.1, new EulerCauchyIntegration());
        try {
            // prey
            // Create prey population as stock
            Stock populationPrey = (Stock) model.createModelEntity(ModelEntityType.STOCK, POPULATION_PREY_KEY);
            populationPrey.setInitialValue(100);
            // Create prey births and deaths as flows
            Flow birthsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, BIRTHS_PREY_KEY);
            Flow deathsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, DEATHS_PREY_KEY);
            // Add flows to prey population
            populationPrey.addInputFlows(birthsPrey);
            populationPrey.addOutputFlows(deathsPrey);
            // Create prey birthrate and deathrate as variable
            Variable expansionRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, BIRTH_RATE_PREY_KEY);
            expansionRatePrey.setInitialValue(0.1);
            Variable lossRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, DEATH_RATE_PREY_KEY);
            lossRatePrey.setInitialValue(0.01);

            // predator
            // Create predator population as stock
            Stock populationPredator = (Stock) model.createModelEntity(ModelEntityType.STOCK, POPULATION_PREDATOR_KEY);
            populationPredator.setInitialValue(1);
            // Create prey births and deaths as flows
            Flow birthsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, BIRTHS_PREDATOR_KEY);
            Flow deathsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, DEATHS_PREDATOR_KEY);
            // Add flows to predator population
            populationPredator.addInputFlows(birthsPredator);
            populationPredator.addOutputFlows(deathsPredator);
            // Create prey birthrate and deathrate as variable
            Variable expansionRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE,
                    BIRTH_RATE_PREDATOR_KEY);
            expansionRatePredator.setInitialValue(0.01);
            Variable lossRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, DEATH_RATE_PREDATOR_KEY);
            lossRatePredator.setInitialValue(0.1);

            // Create meetings as variable
            Variable meetings = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, MEETINGS_KEY);

            // Create converters
            Converter deathsPreyConverter = model.createConverter(deathsPrey, meetings, lossRatePrey);
            deathsPreyConverter.setFunction(() -> meetings.getCurrentValue() * lossRatePrey.getCurrentValue());

            Converter birthsPreyConverter = model.createConverter(birthsPrey, populationPrey, expansionRatePrey);
            birthsPreyConverter
                    .setFunction(() -> populationPrey.getCurrentValue() * expansionRatePrey.getCurrentValue());

            Converter deathsPredatorConverter = model.createConverter(deathsPredator, populationPredator,
                    lossRatePredator);
            deathsPredatorConverter
                    .setFunction(() -> populationPredator.getCurrentValue() * lossRatePredator.getCurrentValue());

            // Approach for converting entity values by implementing IFunction
            // with an inner class
            Converter meetingsConverter = model.createConverter(meetings, populationPrey, populationPredator);
            meetingsConverter
                    .setFunction(() -> populationPrey.getCurrentValue() * populationPredator.getCurrentValue());

            Converter birthsPredatorConverter = model.createConverter(birthsPredator, meetings, expansionRatePredator);
            birthsPredatorConverter
                    .setFunction(() -> meetings.getCurrentValue() * expansionRatePredator.getCurrentValue());

            populationPrey.setChangeRateFunction(() -> birthsPrey.getCurrentValue() - deathsPrey.getCurrentValue());
            populationPredator
                    .setChangeRateFunction(() -> birthsPredator.getCurrentValue() - deathsPredator.getCurrentValue());

        } catch (ModelException e) {
            e.printStackTrace();
        }

        CSVExporter csvExporter = new CSVExporter("output.csv", ";");

        double error = 0.001;

        Simulation simulation = new Simulation(model);
        simulation.setExporter(csvExporter);

        model.setFinalTime(10);
        simulation.run();

        HashMap<String, ModelEntity> entities = model.getModelEntities();

        Assert.assertThat(entities.get(POPULATION_PREY_KEY).getCurrentValue(), Matchers.closeTo(0.2826, error));
        Assert.assertThat(entities.get(BIRTH_RATE_PREY_KEY).getCurrentValue(), Matchers.equalTo(0.1));
        Assert.assertThat(entities.get(DEATH_RATE_PREY_KEY).getCurrentValue(), Matchers.equalTo(0.01));
        Assert.assertThat(entities.get(BIRTHS_PREY_KEY).getCurrentValue(), Matchers.closeTo(0.0283, error));
        Assert.assertThat(entities.get(DEATHS_PREY_KEY).getCurrentValue(), Matchers.closeTo(0.2608, error));

        Assert.assertThat(entities.get(POPULATION_PREDATOR_KEY).getCurrentValue(), Matchers.closeTo(92.2736, error));
        Assert.assertThat(entities.get(BIRTH_RATE_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(0.01));
        Assert.assertThat(entities.get(DEATH_RATE_PREDATOR_KEY).getCurrentValue(), Matchers.equalTo(0.1));
        Assert.assertThat(entities.get(BIRTHS_PREDATOR_KEY).getCurrentValue(), Matchers.closeTo(0.2608, error));
        Assert.assertThat(entities.get(DEATHS_PREDATOR_KEY).getCurrentValue(), Matchers.closeTo(9.2274, error));

        Assert.assertThat(entities.get(MEETINGS_KEY).getCurrentValue(), Matchers.closeTo(26.0779, error));


        String file = simulation.getExporter().getString();
        ChartViewer.setCSVFile(file);
        ChartViewer.setSize(1000, 800);
        ChartViewer.launch(ChartViewer.class);

        //ChartPlotter.setCSVFile(file);
        //ChartPlotter.setSize(1000, 800);
        //ChartPlotter.launch(ChartPlotter.class);
    }

}
