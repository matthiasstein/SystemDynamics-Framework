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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * This class handels the chart printing.
 *
 * @author Matthias Stein
 */
public class ChartsViewer extends Application {

    private static ArrayList<Series> series;
    private static ArrayList<Series> series2;

    @Override
    public void start(Stage stage) {
        stage.setTitle("System Dynamics Charts");

        // create root group
        Group root = new Group();

        // create scene
        Scene scene = new Scene(root, 800, 600/*, Color.WHITE*/);

        // create first tab
        TabPane tabPane = new TabPane();
        Tab firstTab = new Tab();
        firstTab.setText("All Data");

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Value");
        //creating the chart
        LineChart lineChart = this.createLineChart("All Data");
        lineChart.getData().addAll(series);

        firstTab.setContent(lineChart);
        tabPane.getTabs().add(firstTab);

        ChartsViewer.series2.forEach((s) -> {
            //creating the chart
            LineChart chart = this.createLineChart(s.getName());
            chart.getData().add(s);
            // create tab
            Tab tab = new Tab();
            tab.setText(s.getName());
            tab.setContent(chart);
            tabPane.getTabs().add(tab);
        });

        BorderPane borderPane = new BorderPane();
        // bind to take available space
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setCenter(tabPane);

        // add border pane to root
        root.getChildren().add(borderPane);
        stage.setScene(scene);
        stage.show();
        ChartsViewer.saveToFile(scene);
        // stop application
        //Platform.exit();
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
        ChartsViewer.series = new ArrayList<Series>();
        ChartsViewer.series2 = new ArrayList<Series>();
        String[] lines = csvFile.split("\\r?\\n");
        String[] modelEntityNames = lines[0].split(";");

        for (int i = 0; i < modelEntityNames.length; i++) {
            Series s = new Series();
            Series s2 = new Series();
            s.setName(modelEntityNames[i]);
            s2.setName(modelEntityNames[i]);

            for (int j = 1; j < lines.length; j++) {
                String line = lines[j];
                String valueString = line.split(";")[i];
                NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
                Number number;
                try {
                    number = format.parse(valueString);
                    double value = number.doubleValue();
                    s.getData().add(new XYChart.Data(j - 1, value));
                    s2.getData().add(new XYChart.Data(j - 1, value));
                } catch (ParseException ex) {
                    Logger.getLogger(ChartsViewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ChartsViewer.series.add(s);
            ChartsViewer.series2.add(s2);
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
