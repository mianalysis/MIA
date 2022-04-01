package io.github.mianalysis.mia.module.objects.measure.miscellaneous;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.module.Categories;
import io.github.mianalysis.mia.module.Category;
import io.github.mianalysis.mia.module.IL2Support;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.object.Obj;
import io.github.mianalysis.mia.object.Objs;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.measurements.Measurement;
import io.github.mianalysis.mia.object.parameters.InputObjectsP;
import io.github.mianalysis.mia.object.parameters.ObjectMeasurementP;
import io.github.mianalysis.mia.object.parameters.Parameters;
import io.github.mianalysis.mia.object.parameters.SeparatorP;
import io.github.mianalysis.mia.object.parameters.text.DoubleP;
import io.github.mianalysis.mia.object.parameters.text.IntegerP;
import io.github.mianalysis.mia.object.refs.ObjMeasurementRef;
import io.github.mianalysis.mia.object.refs.collections.ImageMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.MetadataRefs;
import io.github.mianalysis.mia.object.refs.collections.ObjMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.ParentChildRefs;
import io.github.mianalysis.mia.object.refs.collections.PartnerRefs;
import io.github.mianalysis.mia.object.system.Preferences;
import io.github.mianalysis.mia.object.system.Status;

@Plugin(type = Module.class, priority=Priority.LOW, visible=true)
public class BinObjectsByMeasurement extends Module {
    public static final String INPUT_SEPARATOR = "Object input";
    public static final String INPUT_OBJECTS = "Input objects";

    public static final String BIN_SEPARATOR = "Binning controls";
    public static final String MEASUREMENT = "Measurement";
    public static final String SMALLEST_BIN_CENTRE = "Smallest bin centre";
    public static final String LARGEST_BIN_CENTRE = "Largest bin centre";
    public static final String NUMBER_OF_BINS = "Number of bins";

    public BinObjectsByMeasurement(Modules modules) {
        super("Bin objects by measurement", modules);
    }

    interface Measurements {
        String BIN = "Bin";
    }

    public static String getFullName(String measurement) {
        return "BIN // " + measurement;
    }

    @Override
    public Category getCategory() {
        return Categories.OBJECTS_MEASURE_MISCELLANEOUS;
    }

    @Override
    public String getDescription() {
        return "Distribute objects into a set of \"bins\" based on a measurement associated with each object.  Bins are evenly distributed between manually-specified smallest and largest bin centres.  The assigned bin for each object is stored as a new measurement associated with that object.";
    }

    @Override
    public Status process(Workspace workspace) {
        // Getting input objects
        String inputObjectName = parameters.getValue(INPUT_OBJECTS);
        Objs inputObjects = workspace.getObjects().get(inputObjectName);

        // Getting parameters
        String measurementName = parameters.getValue(MEASUREMENT);
        double smallestBin = parameters.getValue(SMALLEST_BIN_CENTRE);
        double largestBin = parameters.getValue(LARGEST_BIN_CENTRE);
        int numberOfBins = parameters.getValue(NUMBER_OF_BINS);

        double binWidth = (largestBin - smallestBin) / (numberOfBins - 1);

        int count = 0;
        int total = inputObjects.size();
        for (Obj inputObject : inputObjects.values()) {
            Measurement measurement = inputObject.getMeasurement(measurementName);
            if (measurement == null) {
                inputObject.addMeasurement(new Measurement(getFullName(measurementName), Double.NaN));
                continue;
            }

            double value = measurement.getValue();
            double bin = Math.round((value - smallestBin) / binWidth) * binWidth + smallestBin;

            // Ensuring the bin is within the specified range
            bin = Math.min(bin, largestBin);
            bin = Math.max(bin, smallestBin);

            inputObject.addMeasurement(new Measurement(getFullName(measurementName), bin));

            writeProgressStatus(++count, total, "objects");

        }

        if (showOutput)
            inputObjects.showMeasurements(this, modules);

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        parameters.add(new SeparatorP(INPUT_SEPARATOR, this));
        parameters.add(new InputObjectsP(INPUT_OBJECTS, this));

        parameters.add(new SeparatorP(BIN_SEPARATOR, this));
        parameters.add(new ObjectMeasurementP(MEASUREMENT, this));
        parameters.add(new DoubleP(SMALLEST_BIN_CENTRE, this, 0d));
        parameters.add(new DoubleP(LARGEST_BIN_CENTRE, this, 1d));
        parameters.add(new IntegerP(NUMBER_OF_BINS, this, 1));

        addParameterDescriptions();

    }

    @Override
    public Parameters updateAndGetParameters() {
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        ((ObjectMeasurementP) parameters.getParameter(MEASUREMENT)).setObjectName(inputObjectsName);

        return parameters;
    }

    @Override
    public ImageMeasurementRefs updateAndGetImageMeasurementRefs() {
        return null;
    }

    @Override
    public ObjMeasurementRefs updateAndGetObjectMeasurementRefs() {
        ObjMeasurementRefs returnedRefs = new ObjMeasurementRefs();

        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        String measurement = parameters.getValue(MEASUREMENT);

        String name = getFullName(measurement);
        ObjMeasurementRef binMeasurement = objectMeasurementRefs.getOrPut(name);
        binMeasurement.setObjectsName(inputObjectsName);
        returnedRefs.add(binMeasurement);

        return returnedRefs;

    }

    @Override
    public MetadataRefs updateAndGetMetadataReferences() {
        return null;
    }

    @Override
    public ParentChildRefs updateAndGetParentChildRefs() {
        return null;
    }

    @Override
    public PartnerRefs updateAndGetPartnerRefs() {
        return null;
    }

    @Override
    public boolean verify() {
        String storageMode = MIA.preferences.getDataStorageMode();
        if (storageMode.equals(Preferences.DataStorageModes.STREAM_FROM_DRIVE) & il2Support.equals(IL2Support.NONE))
            return false;

        return true;
    }

    void addParameterDescriptions() {
        parameters.get(INPUT_OBJECTS).setDescription(
                "Objects from the workspace.  The specified measurement for each of these will be binned according to various parameters.  The assigned bin will be stored as a new measurement associated with this object.");

        parameters.get(MEASUREMENT).setDescription(
                "Measurement associated with the input objects that will be binned according to the other parameters.");

        parameters.get(SMALLEST_BIN_CENTRE).setDescription(
                "Centre value associated with the smallest bin.  Bins will be evenly distributed in bins between this value and the upper bin centre (specified by \""
                        + LARGEST_BIN_CENTRE + "\".");

        parameters.get(LARGEST_BIN_CENTRE).setDescription(
                "Centre value associated with the largest bin.  Bins will be evenly distributed in bins between this value and the lower bin centre (specified by \""
                        + SMALLEST_BIN_CENTRE + "\".");

        parameters.get(NUMBER_OF_BINS).setDescription(
                "Number of bins to divide measurements into.  These will be evenly distributed in the range between \""
                        + SMALLEST_BIN_CENTRE + "\" and \"" + LARGEST_BIN_CENTRE + "\".");

    }
}
