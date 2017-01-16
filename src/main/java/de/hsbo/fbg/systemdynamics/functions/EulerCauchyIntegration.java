package de.hsbo.fbg.systemdynamics.functions;

import de.hsbo.fbg.systemdynamics.model.Stock;

/**
 * This class extends the {@link Integration} interface and represents the Euler-Cauchy
 * method.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class EulerCauchyIntegration extends Integration {

    @Override
    public void integrate() {
        for (Stock stock : this.getStocks()) {
            double calculatedValue = stock.getCurrentValue()
                    + stock.getChangeRateFunction().calculateEntityValue() * this.getDt();
            stock.setCurrentValue(calculatedValue);
            stock.setCurrentValueCalculated(true);
        }
    }

}
