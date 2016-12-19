package de.hsbo.fbg.systemdynamics.functions;

import de.hsbo.fbg.systemdynamics.model.Stock;

public abstract class IntegrationType {
	public abstract IFunction getIntegrationFunction(Stock stock);
}
