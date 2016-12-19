package de.hsbo.fbg.systemdynamics.exceptions;

public class ModelException extends Exception {

	public final static String DUPLICATE_MODEL_ENTITY_EXCEPTION = "Duplicate model entity.";
	public final static String DUPLICATE_FLOW_EXCEPTION = "Duplicate flow exception.";

	public ModelException(String message) {
		super(message);
	}
}
