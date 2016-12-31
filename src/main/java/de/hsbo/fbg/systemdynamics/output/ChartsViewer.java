package de.hsbo.fbg.systemdynamics.output;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * This class handels the chart printing.
 *
 * @author Matthias Stein
 */
public class ChartsViewer extends Application {
    
    private static ArrayList<Series> series;
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("System Dynamics Chart");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Value");
        //creating the chart
        final LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);
        
        lineChart.setAnimated(false);
        
        lineChart.setTitle("System Dynamics Chart");
        //defining a series

        Scene scene = new Scene(lineChart, 800, 600);
        
        for (Series s : ChartsViewer.series) {
            lineChart.getData().add(s);
        }
        
        stage.setScene(scene);
        stage.show();
        
        ChartsViewer.saveToFile(scene);
    }

    /**
     * Set CSV String and add series to chart.
     *
     * @param csvFile CSV String
     */
    public static void setCSVFile(String csvFile) {
        ChartsViewer.series = new ArrayList<Series>();
        String[] lines = csvFile.split("\\r?\\n");
        String[] modelEntityNames = lines[0].split(";");
        
        for (int i = 0; i < modelEntityNames.length; i++) {
            Series s = new Series();
            s.setName(modelEntityNames[i]);
            
            for (int j = 1; j < lines.length; j++) {
                String line = lines[j];
                String valueString = line.split(";")[i];
                NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
                Number number;
                try {
                    number = format.parse(valueString);
                    double value = number.doubleValue();
                    s.getData().add(new XYChart.Data(j - 1, value));
                } catch (ParseException ex) {
                    Logger.getLogger(ChartsViewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ChartsViewer.series.add(s);
        }
    }

    /**
     * Method to save the chart as an image.
     *
     * @param scene Scene
     */
    public static void saveToFile(Scene scene) {
        WritableImage image = scene.snapshot(null);
        File outputFile = new File("chart.png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
