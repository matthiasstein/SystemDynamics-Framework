package de.hsbo.fbg.systemdynamics.model;

/**
 * This class represents a flow. A flow can either be an input flow or an output flow for a stock.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
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
