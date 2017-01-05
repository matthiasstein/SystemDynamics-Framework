package de.hsbo.fbg.systemdynamics.functions;

import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Stock;

/**
 * This class extends the IntegrationType and represents the classical
 * Runge–Kutta method (RK4).
 * 
 * @author Sebastian Drost
 *
 */
public class RungeKuttaIntegration extends Integration {

	@Override
	public void integrate() {
		double[][] k = new double[this.getStockConverter().size()][4];
		for (int i = 0; i <= 3; i++) {
			int j = 0;
			for (Converter converter : this.getStockConverter()) {
				k[j][i] = converter.getFunction().calculateEntityValue() * this.getDt();
				Stock stock = (Stock) converter.getTargetEntity();
				if (i < 2)
					stock.setCurrentValue(stock.getPreviousValue() + k[j][i] / 2);
				else if (i == 2)
					stock.setCurrentValue(stock.getPreviousValue() + k[j][i]);
				else {
					double calculatedValue = stock.getPreviousValue() + k[j][0] / 6 + k[j][1] / 3 + k[j][2] / 3
							+ k[j][3] / 6;
					stock.setCurrentValue(calculatedValue);
				}
				stock.setCurrentValueCalculated(true);
				j++;
			}
			for (Converter converter : this.getVariableConverter()) {
				converter.convert();
			}
			this.prepareValuesForStep();
		}
	}

	/**
	 * Method to prepare all values for the next step.
	 */
	private void prepareValuesForStep() {
		for (Converter converter : this.getVariableConverter()) {
			converter.getTargetEntity().setCurrentValueCalculated(false);
		}
	}

}
