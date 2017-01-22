package de.hsbo.fbg.systemdynamics.output;

import de.hsbo.fbg.systemdynamics.model.Model;

/**
 * Class that implements the {@link SimulationEventListener} interface and controls the chart plotting.
 *
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class ChartPlotter implements SimulationEventListener {
    @Override
    public void simulationInitialized(Model model) {
        ChartPlotterApplication.addSeries(model.getModelEntitiesKeys());
    }

    @Override
    public void timeStepCalculated(Model model) {
        ChartPlotterApplication.addValues(model.getModelEntitiesValues(), model.getCurrentTime());
    }

    @Override
    public void simulationFinished(Model model) {
        ChartViewerApplication.launch(ChartViewerApplication.class);
    }
}
