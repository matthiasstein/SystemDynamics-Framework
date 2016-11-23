package de.hsbo.fbg.systemdynamics.model;

import java.util.ArrayList;

import de.hsbo.fbg.systemdynamics.exceptions.DuplicateFlowException;

public class Stock extends ModelEntity {

	private double initialValue;
	private ArrayList<Flow> inputFlows;
	private ArrayList<Flow> outputFlows;

	public Stock(String name) {
		super(name);
		inputFlows = new ArrayList<Flow>();
		outputFlows = new ArrayList<Flow>();
	}

	public void setInitialValue(double value) {
		this.initialValue = value;

	}

	public double getInitialValue() {
		return initialValue;
	}

	public void addInputFlow(Flow flow) throws DuplicateFlowException{
		if (flowAlreadyAdded(flow)){
			throw new DuplicateFlowException("Flow has already been added");
		}
		else{
			this.inputFlows.add(flow);
		}
	}

	public void addInputFlows(Flow... flow) throws DuplicateFlowException {
		for (Flow f : flow)
			this.addInputFlow(f);
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

}
