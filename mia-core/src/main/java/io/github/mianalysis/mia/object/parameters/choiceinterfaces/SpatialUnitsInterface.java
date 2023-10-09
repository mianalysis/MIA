package io.github.mianalysis.mia.object.parameters.choiceinterfaces;

import io.github.mianalysis.mia.module.core.InputControl;

public interface SpatialUnitsInterface {
    public static String CALIBRATED = "Calibrated";
    public static String PIXELS = "Pixel/slice";

    public static String[] ALL = new String[] { CALIBRATED, PIXELS };

    public static String getDescription() {
        return "Controls whether spatial values are assumed to be specified in calibrated units (as defined by the \""
        + new InputControl(null).getName() + "\" parameter \"" + InputControl.SPATIAL_UNIT
        + "\") or pixel units.";
    }
}