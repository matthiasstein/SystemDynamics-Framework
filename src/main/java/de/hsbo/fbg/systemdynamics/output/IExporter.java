package de.hsbo.fbg.systemdynamics.output;

import java.util.List;

/**
 * Exporter interface.
 *
 * @author Matthias Stein
 */
public interface IExporter {

    /**
     * Method to write a line.
     *
     * @param values list of values that should be added to the csv file.
     */
    public void writeLine(List<String> values);

    /**
     * This method writes the data to the file.
     */
    public void saveFile();

    /**
     *
     * @return file content as string.
     */
    public String getString();
}
