package de.hsbo.fbg.systemdynamics.functions;

import java.util.List;

import de.hsbo.fbg.systemdynamics.model.Converter;

/**
 * Abstract class that describes an integration type.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public abstract class Integration {

	double dt;
	List<Converter> stockConverter;
	List<Converter> variableConverter;

	public List<Converter> getStockConverter() {
		return stockConverter;
	}

	public void setStockConverter(List<Converter> stockConverter) {
		this.stockConverter = stockConverter;
	}

	public List<Converter> getVariableConverter() {
		return variableConverter;
	}

	public void setVariableConverter(List<Converter> variableConverter) {
		this.variableConverter = variableConverter;
	}

	/**
	 *
	 * @return dt.
	 */
	public double getDt() {
		return dt;
	}

	/**
	 *
	 * @param dt
	 *            dt to set.
	 */
	public void setDt(double dt) {
		this.dt = dt;
	}

	/**
	 * This method has to be implemented, to calculate the integration.
	 */
	public abstract void integrate();

}
