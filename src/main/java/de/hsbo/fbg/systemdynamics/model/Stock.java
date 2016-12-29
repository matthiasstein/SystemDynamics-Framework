package de.hsbo.fbg.systemdynamics.model;

import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;
import de.hsbo.fbg.systemdynamics.functions.Integration;

/**
 * This class represents a stock.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public class Stock extends ModelEntity {

    private ArrayList<Flow> inputFlows;
    private ArrayList<Flow> outputFlows;
    private Integration integration;

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
     * @throws ModelException
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
     * @throws ModelException
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
     * @throws ModelException
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
     *
     * @return input flows.
     */
    public ArrayList<Flow> getInputFlows() {
        return inputFlows;
    }

    /**
     *
     * @return output flows.
     */
    public ArrayList<Flow> getOutputFlows() {
        return outputFlows;
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

    public Integration getIntegration() {
        return integration;
    }

    protected void setIntegration(Integration integration) {
        this.integration = integration;
    }

}
