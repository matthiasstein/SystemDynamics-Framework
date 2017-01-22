package de.hsbo.fbg.systemdynamics.output;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

/**
 * This class handels the chart printing.
 *
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class ChartViewerApplication extends Application {

    private static Scene scene;
    private static ArrayList<Series> series;
    private static ArrayList<CheckBox> checkBoxes;
    private static double width = 800;
    private static double height = 600;
    private static LineChart lineChart;

    @Override
    public void start(Stage stage) {
        stage.setTitle("System Dynamics Chart");

        BorderPane root = new BorderPane();

        // create scene
        scene = new Scene(root, width, height);

        //creating the chart
        lineChart = this.createLineChart("");
        lineChart.setCursor(Cursor.CROSSHAIR);
        lineChart.getData().addAll(series);
        lineChart.setAnimated(false);
        addTooltips();
        addLineChartContextMenu();

        // add checkboxes
        checkBoxes = new ArrayList<>();
        for (Series s : series) {
            CheckBox cb = new CheckBox(s.getName());
            cb.setOnAction(this::refreshChart);
            cb.setSelected(true);
            checkBoxes.add(cb);
        }

        Label dataLabel = new Label("Data:");
        VBox vbox = new VBox(8); // spacing = 8
        vbox.getChildren().addAll(dataLabel);
        vbox.getChildren().addAll(checkBoxes);
        vbox.setPadding(new Insets(20, 10, 20, 0));

        root.setCenter(lineChart);
        root.setRight(vbox);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to add series to chart.
     *
     * @param modelEntityNames list of model entity names.
     */
    public static void addSeries(List<String> modelEntityNames) {
        ChartViewerApplication.series = new ArrayList<>();
        for (String modelEntityName : modelEntityNames) {
            Series s = new Series();
            s.setName(modelEntityName);
            ChartViewerApplication.series.add(s);
        }
    }

    /**
     * Method to add values to chart series.
     *
     * @param modelEntityValues list of model entity values.
     * @param currentTime       current model time.
     */
    public static void addValues(List<String> modelEntityValues, double currentTime) {
        for (int i = 0; i < modelEntityValues.size(); i++) {
            String valueString = modelEntityValues.get(i);
            NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
            Number number;
            try {
                number = format.parse(valueString);
                double value = number.doubleValue();
                ChartViewerApplication.series.get(i).getData().add(new XYChart.Data(currentTime, value));
            } catch (ParseException ex) {
                Logger.getLogger(ChartViewerApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Method to add / remove chart data.
     *
     * @param event ActionEvent
     */
    private void refreshChart(ActionEvent event) {
        CheckBox cb = (CheckBox) event.getSource();
        String text = cb.getText();
        for (Series s : series) {
            if (text.equals(s.getName())) {
                if (cb.isSelected()) {
                    lineChart.getData().add(s);
                } else {
                    lineChart.getData().remove(s);
                }
            }
        }
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
        xAxis.setLabel("Timestep");
        yAxis.setLabel("Value");
        //creating the chart
        LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle(title);
        return lineChart;
    }

    /**
     * Method to add a context menu to line chart.
     */
    private void addLineChartContextMenu() {
        final MenuItem saveAsFile = new MenuItem("Save as file");
        saveAsFile.setOnAction(event -> saveToFile(scene));

        final ContextMenu menu = new ContextMenu(
                saveAsFile
        );

        lineChart.setOnMouseClicked(event -> {
            if (MouseButton.SECONDARY.equals(event.getButton())) {
                menu.show(lineChart, event.getScreenX(), event.getScreenY());
            }
        });
    }

    /**
     * Method to add tooltips to chart.
     */
    private void addTooltips() {
        /*
         * Browsing through the Data and applying ToolTip as well as the class on hover
         */
        for (int i = 0; i < lineChart.getData().size(); i++) {
            for (int j = 0; j < ((Series) lineChart.getData().get(i)).getData().size(); j++) {
                XYChart.Data<Number, Number> d = (XYChart.Data<Number, Number>) ((Series) lineChart.getData().get(i)).getData().get(j);
                Tooltip.install(d.getNode(), new Tooltip("timestep: " + d.getXValue() + "\nvalue: " + d.getYValue()));
                // Adding class on hover
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));
                // Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }
    }

    /**
     * Set scene width and height.
     *
     * @param width  width.
     * @param height height.
     */
    public static void setSize(double width, double height) {
        ChartViewerApplication.width = width;
        ChartViewerApplication.height = height;
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
