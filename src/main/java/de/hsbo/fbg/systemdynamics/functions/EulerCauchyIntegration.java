package de.hsbo.fbg.systemdynamics.functions;

import de.hsbo.fbg.systemdynamics.model.Stock;

/**
 * This class extends the Integration interface and represents the Euler-Cauchy
 * method.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public class EulerCauchyIntegration extends Integration {

    @Override
    public void integrate() {
        for (Stock stock : this.getStocks()) {
            double calculatedValue = stock.getCurrentValue()
                    + stock.getFlowRateFunction().calculateEntityValue() * this.getDt();
            stock.setCurrentValue(calculatedValue);
            stock.setCurrentValueCalculated(true);
        }
    }

}
