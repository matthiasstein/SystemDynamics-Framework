package de.hsbo.fbg.systemdynamics.output;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles the CSV output.
 *
 * @author Matthias Stein
 */
public class CSVExporter {

    private final String separator;
    private final String csvFile;
    private final StringBuilder sb;

    /**
     * Constructor.
     *
     * @param csvFile the path to the csv file.
     * @param separator the separator string.
     */
    public CSVExporter(String csvFile, String separator) {
        this.separator = separator;
        this.csvFile = csvFile;
        this.sb = new StringBuilder();
    }

    /**
     * Method to write on line.
     *
     * @param values list of values that should be added to the csv file.
     */
    public void writeLine(List<String> values) {

        boolean first = true;

        for (String value : values) {
            if (!first) {
                this.sb.append(this.separator);
            }
            this.sb.append(value);

            first = false;
        }
        sb.append("\n");

    }

    /**
     * This method writes the data to the csv file.
     */
    public void saveFile() {
        try {
            int last = sb.lastIndexOf("\n");
            if (last >= 0) {
                sb.delete(last, sb.length());
            }
            try (FileWriter writer = new FileWriter(this.csvFile)) {
                writer.append(sb.toString());
                writer.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
