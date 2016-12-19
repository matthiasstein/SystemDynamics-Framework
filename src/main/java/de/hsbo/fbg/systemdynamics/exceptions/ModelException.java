package de.hsbo.fbg.systemdynamics.exceptions;

public class ModelException extends Exception {

    private static final long serialVersionUID = 1L;
    public final static String DUPLICATE_MODEL_ENTITY_EXCEPTION = "Duplicate model entity.";
    public final static String DUPLICATE_FLOW_EXCEPTION = "Duplicate flow exception.";
    public final static String DUPLICATE_VARIABLE_EXCEPTION = "Duplicate variable exception.";

    public ModelException(String message) {
        super(message);
    }
}
