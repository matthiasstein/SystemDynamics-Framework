package de.hsbo.fbg.systemdynamics.model;

import de.hsbo.fbg.systemdynamics.functions.IFunction;

public abstract class IntegrationType {
	public abstract IFunction getIntegrationFunction(Stock stock);
}
