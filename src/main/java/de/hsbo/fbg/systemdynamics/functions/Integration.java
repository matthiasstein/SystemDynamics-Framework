package de.hsbo.fbg.systemdynamics.functions;

import java.util.List;

import de.hsbo.fbg.systemdynamics.model.Converter;
import de.hsbo.fbg.systemdynamics.model.Stock;

/**
 * Abstract class that describes an integration method.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public abstract class Integration {

    private double dt;
    private List<Stock> stocks;
    private List<Converter> variableConverter;

    /**
     * @return stocks.
     */
    protected List<Stock> getStocks() {
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
    protected List<Converter> getVariableConverter() {
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
    protected double getDt() {
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
