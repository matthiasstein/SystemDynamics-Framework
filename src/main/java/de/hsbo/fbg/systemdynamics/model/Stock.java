package de.hsbo.fbg.systemdynamics.model;

import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;

/**
 * class that represents a stock
 * 
 * @author Sebastian Drost, Matthias Stein
 */
public class Stock extends ModelEntity {

    private ArrayList<Flow> inputFlows;
    private ArrayList<Flow> outputFlows;

    /**
     *
     * @param name
     */
    public Stock(String name) {
        super(name);
        this.setCurrentValueCalculated(true);
        this.inputFlows = new ArrayList<Flow>();
        this.outputFlows = new ArrayList<Flow>();
    }

    /**
     *
     * @param value
     */
    public void setInitialValue(double value) {
        setCurrentValue(value);
        setPreviousValue(value);
    }

    /**
     *
     * @param flow
     * @throws ModelException
     */
    public void addInputFlows(Flow... flow) throws ModelException {
        for (Flow f : flow) {
            this.addInputFlow(f);
        }
    }

    /**
     *
     * @param flow
     * @throws ModelException
     */
    public void addInputFlow(Flow flow) throws ModelException {
        if (flowAlreadyAdded(flow)) {
            throw new ModelException(ModelException.DUPLICATE_FLOW_EXCEPTION);
        } else {
            this.inputFlows.add(flow);
        }
    }

    /**
     *
     * @param flow
     * @throws ModelException
     */
    public void addOutputFlows(Flow... flow) throws ModelException {
        for (Flow f : flow) {
            this.addOutputFlow(f);
        }
    }

    /**
     *
     * @param flow
     */
    public void addOutputFlow(Flow flow) {
        this.outputFlows.add(flow);
    }

    private boolean flowAlreadyAdded(Flow flow) {
        return inputFlows.contains(flow) || outputFlows.contains(flow);
    }

    /**
     *
     * @return
     */
    public ArrayList<Flow> getInputFlows() {
        return inputFlows;
    }

    /**
     *
     * @return
     */
    public ArrayList<Flow> getOutputFlows() {
        return outputFlows;
    }

}
