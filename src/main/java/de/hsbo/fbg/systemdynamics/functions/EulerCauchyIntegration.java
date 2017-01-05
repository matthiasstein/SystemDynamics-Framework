package de.hsbo.fbg.systemdynamics.functions;

import de.hsbo.fbg.systemdynamics.model.Converter;

/**
 * This class extends the IntegrationType and represents the euler cauchy
 * calculation.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public class EulerCauchyIntegration extends Integration {

	public void integrate() {
		for (Converter converter : this.getStockConverter()){
			double calculatedValue=converter.getTargetEntity().getCurrentValue()+converter.getFunction().calculateEntityValue()*this.getDt();
			converter.getTargetEntity().setCurrentValue(calculatedValue);
			converter.getTargetEntity().setCurrentValueCalculated(true);
		}
	}


}
