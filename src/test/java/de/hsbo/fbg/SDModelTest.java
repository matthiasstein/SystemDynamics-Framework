package de.hsbo.fbg;

import org.junit.Test;

import de.hsbo.fbg.systemdynamics.model.ModelEntityType;
import de.hsbo.fbg.systemdynamics.model.Stock;
import de.hsbo.fbg.systemdynamics.model.Variable;
import de.hsbo.fbg.systemdynamics.exceptions.DuplicateFlowException;
import de.hsbo.fbg.systemdynamics.exceptions.DuplicateModelEntityException;
import de.hsbo.fbg.systemdynamics.functions.IFunction;
import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Flow;
import de.hsbo.fbg.systemdynamics.model.Model;
import de.hsbo.fbg.systemdynamics.model.ModelEntity;
import java.util.HashMap;

public class SDModelTest {

    @Test
    public void modelCreationTest() {
        Model model = new Model();

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
            expansionRatePrey.setValue(0.05);
            Variable lossRatePrey = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "DeathRate_Prey");
            lossRatePrey.setValue(0.001);

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
            Variable expansionRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "BirthRate_Predator");
            expansionRatePredator.setValue(0.0002);
            Variable lossRatePredator = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "DeathRate_Predator");
            lossRatePredator.setValue(0.1);

            // Create meetings as variable
            Variable meetings = (Variable) model.createModelEntity(ModelEntityType.VARIABLE, "Meetings");

            // Approach for converting entity values by implementing IFunction
            // with an inner class
            Converter meetingsConverter = new Converter(meetings, () -> populationPrey.getValue() * populationPredator.getValue());

            Converter birthsPreyConverter = new Converter(birthsPrey, () -> populationPrey.getValue() * expansionRatePrey.getValue());

            Converter deathsPreyConverter = new Converter(deathsPrey, () -> meetings.getValue() * lossRatePrey.getValue());

            Converter birthsPredatorConverter = new Converter(birthsPredator, () -> meetings.getValue() * expansionRatePredator.getValue());

            Converter deathsPredatorConverter = new Converter(deathsPredator, () -> populationPredator.getValue() * lossRatePredator.getValue());

            model.addConverters(meetingsConverter, birthsPreyConverter, deathsPreyConverter, birthsPredatorConverter, deathsPredatorConverter);
            model.simulate();
            HashMap<String, ModelEntity> modelEntities = model.getModelEntities();
            System.out.println(modelEntities);

        } catch (DuplicateModelEntityException | DuplicateFlowException e) {
            e.printStackTrace();
        }

    }
}
