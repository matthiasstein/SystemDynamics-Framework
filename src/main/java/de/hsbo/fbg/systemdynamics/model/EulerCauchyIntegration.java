package de.hsbo.fbg.systemdynamics.model;

import de.hsbo.fbg.systemdynamics.functions.IFunction;

public class EulerCauchyIntegration extends IntegrationType {

	@Override
	public IFunction getIntegrationFunction(Stock stock) {
		return new IFunction() {
			@Override
			public double calculateEntityValue() {
				double result = stock.getValue();
				for (Flow input : stock.getInputFlows()) {
					result += input.getValue();
				}
				for (Flow output : stock.getOutputFlows()) {
					result -= output.getValue();
				}
				return result;
			}
		};
	}
}
