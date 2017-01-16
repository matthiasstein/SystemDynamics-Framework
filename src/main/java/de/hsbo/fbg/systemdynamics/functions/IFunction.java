package de.hsbo.fbg.systemdynamics.functions;

/**
 * Function interface.
 *
 * @author <a href="mailto:sebastian.drost@hs-bochum.de">Sebastian Drost</a>
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public interface IFunction {

    /**
     * @return calculated entity value
     */
    double calculateEntityValue();

}
