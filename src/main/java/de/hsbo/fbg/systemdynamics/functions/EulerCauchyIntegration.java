package de.hsbo.fbg.systemdynamics.functions;

import de.hsbo.fbg.systemdynamics.model.Flow;
import de.hsbo.fbg.systemdynamics.model.Stock;

public class EulerCauchyIntegration extends IntegrationType {

    @Override
    public IFunction getIntegrationFunction(Stock stock) {
        return new IFunction() {
            @Override
            public double calculateEntityValue() {
                double result = stock.getCurrentValue();
                for (Flow input : stock.getInputFlows()) {
                    result += input.getPreviousValue();
                }
                for (Flow output : stock.getOutputFlows()) {
                    result -= output.getPreviousValue();
                }
                return result;
            }
        };
    }
}
