package de.hsbo.fbg.systemdynamics.exceptions;

/**
 * This class extents the Exception class and offers static strings to describe
 * model exceptions.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class ModelException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * String that descibes the dublicate model entity model exception.
     */
    public final static String DUPLICATE_MODEL_ENTITY_EXCEPTION = "Duplicate model entity.";

    /**
     * String that descibes the dublicate flow model exception.
     */
    public final static String DUPLICATE_FLOW_EXCEPTION = "Duplicate flow exception.";

    /**
     * String that descibes the dublicate variable model exception.
     */
    public final static String DUPLICATE_VARIABLE_EXCEPTION = "Duplicate variable exception.";

    /**
     * Constructor that uses the super constructor.
     *
     * @param message the error message.
     */
    public ModelException(String message) {
        super(message);
    }
}
