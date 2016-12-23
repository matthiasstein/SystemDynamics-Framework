package de.hsbo.fbg.systemdynamics.functions;

import de.hsbo.fbg.systemdynamics.model.Stock;

/**
 * Abstract class that describes an integration type.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public abstract class Integration {

    double dt;

    /**
     * Abstract method to get the integration function.
     *
     * @param stock stock.
     * @return integration function.
     */
    public abstract IFunction getIntegrationFunction(Stock stock);

    /**
     *
     * @return dt.
     */
    public double getDt() {
        return dt;
    }

    /**
     *
     * @param dt dt to set.
     */
    public void setDt(double dt) {
        this.dt = dt;
    }

}
