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
	List <Converter> stockConverter;
    List <Converter> variableConverter;
    
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



//    /**
//     * Abstract method to get the integration function.
//     *
//     * @param stock stock.
//     * @return integration function.
//     */
//    public abstract IFunction getIntegrationFunction(Stock stock);

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
        
    
    public abstract void integrate ();

}
