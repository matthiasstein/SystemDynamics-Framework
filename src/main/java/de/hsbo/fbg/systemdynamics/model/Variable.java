package de.hsbo.fbg.systemdynamics.model;

/**
 * class that represents a variable
 * 
 * @author Sebastian Drost, Matthias Stein
 */
public class Variable extends ModelEntity {

    /**
     *
     * @param name
     */
    public Variable(String name) {
        super(name);
    }

	@Override
	public Object clone() throws CloneNotSupportedException {
		Variable variable=new Variable(this.getName());
		variable.setCurrentValue(this.getCurrentValue());
		variable.setPreviousValue(this.getPreviousValue());
		return variable;
	}
    
    
}
