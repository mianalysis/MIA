package io.github.mianalysis.mia.macro.imagemeasurements;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import ij.macro.MacroExtension;
import ij.measure.ResultsTable;
import io.github.mianalysis.mia.macro.MacroOperation;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.WorkspaceI;
import io.github.mianalysis.mia.object.refs.ImageMeasurementRef;
import io.github.mianalysis.mia.object.refs.collections.ImageMeasurementRefs;

@Plugin(type = MacroOperation.class, priority = Priority.LOW, visible = true)
public class MIA_ListImageMeasurementNames extends MacroOperation {
    public MIA_ListImageMeasurementNames(MacroExtension theHandler) {
        super(theHandler);
    }

    @Override
    public int[] getArgumentTypes() {
        return new int[] { ARG_STRING };
    }

    @Override
    public String action(Object[] objects, WorkspaceI workspace, Modules modules) {
        String imageName = (String) objects[0];

        // Creating a new ResultsTable to hold the measurement names
        ResultsTable rt = new ResultsTable();
        int row = 0;

        // Getting a list of Images in the Workspace
        ImageMeasurementRefs measurements = modules.getImageMeasurementRefs(imageName);
        for (ImageMeasurementRef measurement : measurements.values()) {
            if (row != 0)
                rt.incrementCounter();

            rt.setValue("Measurement name", row, measurement.getName());
            rt.setValue("Measurement nickname", row, measurement.getNickname());

            row++;

        }

        rt.show("Measurements for \"" + imageName + "\" image");

        return null;

    }

    @Override
    public String getArgumentsDescription() {
        return "String imageName";
    }

    @Override
    public String getVersionNumber() {
        return "1.0.0";
    }

    @Override
    public String getDescription() {
        return "Returns a list of available measurements for the specified image.  Use the macro Ext.MIA_ShowAllImageMeasurements(imageName) to get a table containing all measurement values for the image.  Use the macro Ext.MIA_GetImageMeasurement(imageName, measurementID) to get a specific measurement value for a specific image.";
    }
}
