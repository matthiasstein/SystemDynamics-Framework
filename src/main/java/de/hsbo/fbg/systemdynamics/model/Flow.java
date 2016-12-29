package de.hsbo.fbg.systemdynamics.model;

/**
 * This class represents a flow.
 *
 * @author Sebastian Drost
 * @author Matthias Stein
 */
public class Flow extends ModelEntity {

    /**
     * Constructor.
     *
     * @param name flow name.
     */
    public Flow(String name) {
        super(name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Flow flow = new Flow(this.getName());
        flow.setCurrentValue(this.getCurrentValue());
        flow.setInitialValue(getInitialValue());
        flow.setPreviousValue(this.getPreviousValue());
        return flow;
    }

}
