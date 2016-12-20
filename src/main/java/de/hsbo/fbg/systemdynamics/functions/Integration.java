package de.hsbo.fbg.systemdynamics.functions;

import de.hsbo.fbg.systemdynamics.model.Stock;

/**
 * abstract class
 * 
 * @author Sebastian Drost, Matthias Stein
 */
public abstract class Integration {

	double dt;
	
    public abstract IFunction getIntegrationFunction(Stock stock);

	public double getDt() {
		return dt;
	}

	public void setDt(double dt) {
		this.dt = dt;
	}
    
    
}
