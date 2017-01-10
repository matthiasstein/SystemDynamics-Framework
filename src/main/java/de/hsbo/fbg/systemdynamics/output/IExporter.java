package de.hsbo.fbg.systemdynamics.output;

import java.util.List;

/**
 * Exporter interface.
 *
 * @author Matthias Stein
 */
public interface IExporter {

    /**
     * Method to write all time step values to the file.
     *
     * @param values list of values that should be added to the csv file.
     */
    void writeTimeStepValues(List<String> values);

    /**
     * This method writes the data to the file.
     */
    void saveFile();

    /**
     * @return file content as string.
     */
    String getString();
}
