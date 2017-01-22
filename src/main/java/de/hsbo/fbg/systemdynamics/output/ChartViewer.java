package de.hsbo.fbg.systemdynamics.output;

import de.hsbo.fbg.systemdynamics.model.Model;

/**
 * Class that implements the {@link SimulationEventListener} interface and controls the chart printing.
 *
 * @author <a href="mailto:matthias.stein@hs-bochum.de">Matthias Stein</a>
 */
public class ChartViewer implements SimulationEventListener {
    @Override
    public void simulationInitialized(Model model) {
        ChartViewerApplication.addSeries(model.getModelEntitiesKeys());
    }

    @Override
    public void timeStepCalculated(Model model) {
        ChartViewerApplication.addValues(model.getModelEntitiesValues(), model.getCurrentTime());
    }

    @Override
    public void simulationFinished(Model model) {
        ChartViewerApplication.launch(ChartViewerApplication.class);
    }
}
