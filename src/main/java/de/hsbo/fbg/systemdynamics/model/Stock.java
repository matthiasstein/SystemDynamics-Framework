package de.hsbo.fbg.systemdynamics.model;

import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.ModelException;

public class Stock extends ModelEntity {

    private ArrayList<Flow> inputFlows;
    private ArrayList<Flow> outputFlows;

    public Stock(String name) {
        super(name);
        inputFlows = new ArrayList<Flow>();
        outputFlows = new ArrayList<Flow>();
    }

    public void setInitialValue(double value) {
        setValue(value);
    }

    public void addInputFlow(Flow flow) throws ModelException {
        if (flowAlreadyAdded(flow)) {
            throw new ModelException(ModelException.DUPLICATE_FLOW_EXCEPTION);
        } else {
            this.inputFlows.add(flow);
        }
    }

    public void addInputFlows(Flow... flow) throws ModelException {
        for (Flow f : flow) {
            this.addInputFlow(f);
        }
    }

    public void addOutputFlow(Flow flow) {
        this.outputFlows.add(flow);
    }

    private boolean flowAlreadyAdded(Flow flow) {
        if (inputFlows.contains(flow) || outputFlows.contains(flow)) {
            return true;
        } else {
            return false;
        }
    }

	public ArrayList<Flow> getInputFlows() {
		return inputFlows;
	}

	public ArrayList<Flow> getOutputFlows() {
		return outputFlows;
	}


}
