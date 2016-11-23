package de.hsbo.fbg;

import org.junit.Test;

import de.hsbo.fbg.systemdynamics.model.ModelEntityType;
import de.hsbo.fbg.systemdynamics.model.Stock;
import de.hsbo.fbg.systemdynamics.model.Variable;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import de.hsbo.fbg.systemdynamics.exceptions.DuplicateFlowException;
import de.hsbo.fbg.systemdynamics.exceptions.DuplicateModelEntityException;
import de.hsbo.fbg.systemdynamics.functions.AlgebraicFunction;
import de.hsbo.fbg.systemdynamics.model.Flow;
import de.hsbo.fbg.systemdynamics.model.Model;

public class SDModelTest {

	@Test
	public void modelCreationTest() {
		Model model = new Model();

		try {
			Stock populationPrey = (Stock) model.createModelEntity(ModelEntityType.STOCK, "Population_Prey");
			populationPrey.setInitialValue(5000);
			Flow birthsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Births_Prey");
			Flow deathsPrey = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Deaths_Prey");
			populationPrey.addInputFlow(birthsPrey);
			populationPrey.addOutputFlow(deathsPrey);
			Variable birthRatePrey=(Variable)model.createModelEntity(ModelEntityType.VARIABLE, "BirthRate_Prey");
			Variable deathRatePrey=(Variable)model.createModelEntity(ModelEntityType.VARIABLE, "DeathRate_Prey");
			

			Expression e = new ExpressionBuilder("x * y")
			        .variables("x", "y")
			        .build()
			        .setVariable("x", populationPrey.getValue())
			        .setVariable("y", birthRatePrey.getValue());

			AlgebraicFunction birthRatePreyFunction=new AlgebraicFunction(e);
			double result = e.evaluate();
			//birthRatePrey.createConverter();
							
			Stock populationPredator = (Stock) model.createModelEntity(ModelEntityType.STOCK, "Population_Predator");
			populationPredator.setInitialValue(200);
			Flow birthsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Births_Predator");
			Flow deathsPredator = (Flow) model.createModelEntity(ModelEntityType.FLOW, "Deaths_Predator");
			populationPredator.addInputFlow(birthsPredator);
			populationPredator.addOutputFlow(deathsPredator);
			
			
			

			
		} catch (DuplicateModelEntityException | DuplicateFlowException e) {
			e.printStackTrace();
		}

	}
}
