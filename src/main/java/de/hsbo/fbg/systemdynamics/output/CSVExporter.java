/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hsbo.fbg.systemdynamics.output;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class that handles the CSV output
 *
 * @author Matthias Stein
 */
public class CSVExporter {

    private String separator;
    private String csvFile;
    private StringBuilder sb;

    public CSVExporter(String separator, String csvFile) {
        this.separator = separator;
        this.csvFile = csvFile;
        this.sb = new StringBuilder();
    }

    public void writeLine(List<String> values) throws IOException {

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

    public void saveFile() {
        try {
            int last = sb.lastIndexOf("\n");
            if (last >= 0) {
                sb.delete(last, sb.length());
            }
            FileWriter writer = new FileWriter(this.csvFile);
            writer.append(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(CSVExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
