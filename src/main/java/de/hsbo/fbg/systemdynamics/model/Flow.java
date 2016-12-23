package de.hsbo.fbg.systemdynamics.model;

/**
 * class that represents a flow
 * 
 * @author Sebastian Drost, Matthias Stein
 */
public class Flow extends ModelEntity {

    /**
     *
     * @param name
     */
    public Flow(String name) {
        super(name);
    }

	@Override
	public Object clone() throws CloneNotSupportedException {
		Flow flow=new Flow(this.getName());
		flow.setCurrentValue(this.getCurrentValue());
		flow.setPreviousValue(this.getPreviousValue());
		return flow;
	}
    
    

}
