package de.hsbo.fbg.systemdynamics.functions;

import java.util.List;

import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Stock;

/**
 * Abstract class that describes an integration type.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public abstract class Integration {

    private double dt;
    private List<Stock> stocks;
    private List<Converter> variableConverter;

    /**
     * @return stocks.
     */
    public List<Stock> getStocks() {
        return stocks;
    }

    /**
     * @param stocks stocks to set.
     */
    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    /**
     * @return variable converters.
     */
    public List<Converter> getVariableConverter() {
        return variableConverter;
    }

    /**
     * @param variableConverter variable converters to set.
     */
    public void setVariableConverter(List<Converter> variableConverter) {
        this.variableConverter = variableConverter;
    }

    /**
     * @return dt.
     */
    public double getDt() {
        return dt;
    }

    /**
     * @param dt dt to set.
     */
    public void setDt(double dt) {
        this.dt = dt;
    }

    /**
     * This method has to be implemented, to calculate the integration.
     */
    public abstract void integrate();

}
