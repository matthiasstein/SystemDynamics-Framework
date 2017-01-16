package de.hsbo.fbg.systemdynamics.model;

/**
 * This class represents a variable.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class Variable extends ModelEntity {

    /**
     * Constructor.
     *
     * @param name the name of the variable.
     */
    public Variable(String name) {
        super(name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Variable variable = new Variable(this.getName());
        variable.setCurrentValue(this.getCurrentValue());
        variable.setInitialValue(getInitialValue());
        variable.setPreviousValue(this.getPreviousValue());
        return variable;
    }

}
