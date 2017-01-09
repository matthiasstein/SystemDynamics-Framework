package de.hsbo.fbg.systemdynamics.output;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handels the chart printing.
 *
 * @author Matthias Stein
 */
public class ChartPlotter extends Application {

    private static ArrayList<Series> series;
    private static double width = 800;
    private static double height = 600;

    @Override
    public void start(Stage stage) {

        // create root group
        Group root = new Group();

        // create scene
        Scene scene = new Scene(root, width, height);

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Value");
        //creating the chart
        LineChart lineChart = this.createLineChart("System Dynamics Chart");
        lineChart.getData().addAll(series);

        BorderPane borderPane = new BorderPane();
        // bind to take available space
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setCenter(lineChart);

        // add border pane to root
        root.getChildren().add(borderPane);

        // save to file
        ChartPlotter.saveToFile(scene);
        // stop application
        Platform.exit();
    }

    /**
     * Method to create a LineChart
     *
     * @param title line chart title
     * @return line chart
     */
    private LineChart createLineChart(String title) {
        //defining the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Value");
        //creating the chart
        LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);

        // disable chart animation
        lineChart.setAnimated(false);

        lineChart.setTitle(title);
        return lineChart;
    }

    /**
     * Set CSV String and add series to chart.
     *
     * @param csvFile CSV String
     */
    public static void setCSVFile(String csvFile) {
        ChartPlotter.series = new ArrayList<Series>();
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
                    Logger.getLogger(ChartPlotter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ChartPlotter.series.add(s);
        }
    }

    public static void setSize(double width, double height) {
        ChartPlotter.width = width;
        ChartPlotter.height = height;
    }

    /**
     * Method to save the chart as an image.
     *
     * @param scene Scene
     */
    private static void saveToFile(Scene scene) {
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
