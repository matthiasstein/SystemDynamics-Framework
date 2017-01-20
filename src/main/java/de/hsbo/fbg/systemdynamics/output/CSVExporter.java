package de.hsbo.fbg.systemdynamics.output;

import de.hsbo.fbg.systemdynamics.model.Model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.EventListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles the CSV output.
 *
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class CSVExporter implements SimulationEventListener {

    private final String separator;
    private final String csvFile;
    private final StringBuilder sb;

    /**
     * Constructor.
     *
     * @param csvFile   the path to the csv file.
     * @param separator the separator string.
     */
    public CSVExporter(String csvFile, String separator) {
        this.separator = separator;
        this.csvFile = csvFile;
        this.sb = new StringBuilder();
    }

    private void writeTimeStepValues(List<String> values) {
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

    private void saveFile() {
        try {
            try (FileWriter writer = new FileWriter(this.csvFile)) {
                writer.append(this.getString());
                writer.flush();
                Logger.getLogger(CSVExporter.class.getName()).log(Level.SEVERE, null, "File saved");
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getString() {
        int last = sb.lastIndexOf("\n");
        if (last >= 0 && sb.length() - last == 1) {
            sb.delete(last, sb.length());
        }
        return sb.toString();
    }

    private void clearContent() {
        sb.delete(0, sb.length());
    }

    @Override
    public void simulationInitialized(Model model) {
        clearContent();
        writeTimeStepValues(model.getModelEntitiesKeys());

    }

    @Override
    public void timeStepCalculated(Model model) {
        writeTimeStepValues(model.getModelEntitiesValues());
    }

    @Override
    public void simulationFinished(Model model) {
        saveFile();
    }
}
