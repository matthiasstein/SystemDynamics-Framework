package de.hsbo.fbg.systemdynamics.functions;

import de.hsbo.fbg.systemdynamics.model.Stock;

/**
 * abstract class
 *
 * @author Sebastian Drost, Matthias Stein
 */
public abstract class Integration {

    double dt;

    /**
     *
     * @param stock
     * @return
     */
    public abstract IFunction getIntegrationFunction(Stock stock);

    /**
     *
     * @return
     */
    public double getDt() {
        return dt;
    }

    /**
     *
     * @param dt
     */
    public void setDt(double dt) {
        this.dt = dt;
    }

}
