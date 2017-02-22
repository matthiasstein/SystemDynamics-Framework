package de.hsbo.fbg.systemdynamics.model;

import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.IFunction;

/**
 * This class represents a stock.
 * A stock remembers the current state of the dynamic system, has one or more
 * input and output {@link Flow} instances that represent it's changing rate
 * and will be calculated by an integration method.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class Stock extends ModelEntity {

    private ArrayList<Flow> inputFlows;
    private ArrayList<Flow> outputFlows;
    private IFunction changeRateFunction;

    /**
     * Constructor.
     *
     * @param name the name of the stock.
     */
    public Stock(String name) {
        super(name);
        this.setCurrentValueCalculated(true);
        this.inputFlows = new ArrayList<Flow>();
        this.outputFlows = new ArrayList<Flow>();
    }

    /**
     * Method to add multiple input flows.
     *
     * @param flow flows that should be added.
     * @throws ModelException model exception.
     */
    public void addInputFlows(Flow... flow) throws ModelException {
        for (Flow f : flow) {
            this.addInputFlow(f);
        }
    }

    /**
     * Method to add one input flow.
     *
     * @param flow flow that should be added.
     * @throws ModelException model exception.
     */
    private void addInputFlow(Flow flow) throws ModelException {
        if (flowAlreadyAdded(flow)) {
            throw new ModelException(ModelException.DUPLICATE_FLOW_EXCEPTION);
        } else {
            this.inputFlows.add(flow);
        }
    }

    /**
     * Method to add multiple output flow.
     *
     * @param flow flow that should be added.
     * @throws ModelException model exception.
     */
    public void addOutputFlows(Flow... flow) throws ModelException {
        for (Flow f : flow) {
            this.addOutputFlow(f);
        }
    }

    /**
     * Method to add one output flow.
     *
     * @param flow flow that should be added.
     */
    protected void addOutputFlow(Flow flow) {
        this.outputFlows.add(flow);
    }

    /**
     * Method that controls if the flow has already been added.
     *
     * @param flow flow.
     * @return <tt>true</tt> only if the has already been added.
     */
    private boolean flowAlreadyAdded(Flow flow) {
        return inputFlows.contains(flow) || outputFlows.contains(flow);
    }

    /**
     * @return input flows.
     */
    public ArrayList<Flow> getInputFlows() {
        return inputFlows;
    }

    /**
     * @return output flows.
     */
    public ArrayList<Flow> getOutputFlows() {
        return outputFlows;
    }

    /**
     * @return function for the flow rate.
     */
    public IFunction getChangeRateFunction() {
        return changeRateFunction;
    }

    /**
     * @param changeRateFunction function for the flow rate.
     */
    public void setChangeRateFunction(IFunction changeRateFunction) {
        this.changeRateFunction = changeRateFunction;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Stock stock = new Stock(this.getName());
        stock.setCurrentValue(this.getCurrentValue());
        stock.setInitialValue(getInitialValue());
        stock.setPreviousValue(this.getPreviousValue());
        ArrayList<Flow> inputFlows = new ArrayList<Flow>();
        ArrayList<Flow> outputFlows = new ArrayList<Flow>();
        for (Flow flow : this.getInputFlows()) {
            inputFlows.add((Flow) flow.clone());
        }
        for (Flow flow : this.getOutputFlows()) {
            outputFlows.add((Flow) flow.clone());
        }
        return stock;
    }
}
