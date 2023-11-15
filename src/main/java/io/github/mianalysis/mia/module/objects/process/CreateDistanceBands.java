// TODO: Distance limits

package io.github.mianalysis.mia.module.objects.process;

import java.util.HashMap;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import ij.IJ;
import io.github.mianalysis.mia.module.Categories;
import io.github.mianalysis.mia.module.Category;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.module.images.process.ImageCalculator;
import io.github.mianalysis.mia.module.images.process.ImageMath;
import io.github.mianalysis.mia.module.images.process.ImageTypeConverter;
import io.github.mianalysis.mia.module.images.process.InvertIntensity;
import io.github.mianalysis.mia.module.images.process.binary.DistanceMap;
import io.github.mianalysis.mia.module.objects.detect.IdentifyObjects;
import io.github.mianalysis.mia.module.objects.transform.MaskObjects;
import io.github.mianalysis.mia.object.Measurement;
import io.github.mianalysis.mia.object.Obj;
import io.github.mianalysis.mia.object.Objs;
import io.github.mianalysis.mia.object.VolumeTypesInterface;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.parameters.BooleanP;
import io.github.mianalysis.mia.object.parameters.ChoiceP;
import io.github.mianalysis.mia.object.parameters.InputObjectsP;
import io.github.mianalysis.mia.object.parameters.Parameters;
import io.github.mianalysis.mia.object.parameters.ParentObjectsP;
import io.github.mianalysis.mia.object.parameters.SeparatorP;
import io.github.mianalysis.mia.object.parameters.choiceinterfaces.SpatialUnitsInterface;
import io.github.mianalysis.mia.object.parameters.objects.OutputObjectsP;
import io.github.mianalysis.mia.object.parameters.text.DoubleP;
import io.github.mianalysis.mia.object.refs.ObjMeasurementRef;
import io.github.mianalysis.mia.object.refs.collections.ImageMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.MetadataRefs;
import io.github.mianalysis.mia.object.refs.collections.ObjMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.ParentChildRefs;
import io.github.mianalysis.mia.object.refs.collections.PartnerRefs;
import io.github.mianalysis.mia.object.system.Status;
import io.github.mianalysis.mia.process.ColourFactory;
import net.imagej.ImgPlus;
import net.imglib2.Point;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

/**
 * Created by Stephen Cross on 13/11/2023.
 */

/**
* 
*/
@Plugin(type = Module.class, priority = Priority.LOW, visible = true)
public class CreateDistanceBands<T extends RealType<T> & NativeType<T>> extends Module {

    /**
    * 
    */
    public static final String INPUT_SEPARATOR = "Input/output controls";

    /**
    * 
    */
    public static final String INPUT_OBJECTS = "Input objects";

    /**
    * 
    */
    public static final String OUTPUT_OBJECTS = "Output objects";

    /**
    * 
    */
    public static final String VOLUME_TYPE = "Volume type";

    /**
    * 
    */
    public static final String BAND_SEPARATOR = "Band controls";

    /**
     * 
     */
    public static final String RELATIVE_MODE = "Relative mode";

    /**
     * 
     */
    public static final String PARENT_OBJECTS = "Parent objects";

    /**
    * 
    */
    public static final String BAND_MODE = "Band mode";

    /**
    * 
    */
    public static final String MATCH_Z_TO_X = "Match Z to XY";

    /**
    * 
    */
    public static final String WEIGHT_MODE = "Weight mode";

    /**
    * 
    */
    public static final String BAND_WIDTH = "Band width";

    /**
    * 
    */
    public static final String SPATIAL_UNITS_MODE = "Spatial units mode";

    /**
    * 
    */
    public static final String APPLY_MINIMUM_BAND_DISTANCE = "Apply minimum band distance";

    /**
    * 
    */
    public static final String MINIMUM_BAND_DISTANCE = "Minimum band distance";

    /**
    * 
    */
    public static final String APPLY_MAXIMUM_BAND_DISTANCE = "Apply maximum band distance";

    /**
    * 
    */
    public static final String MAXIMUM_BAND_DISTANCE = "Maximum band distance";

    public CreateDistanceBands(Modules modules) {
        super("Create distance bands", modules);
    }

    public interface VolumeTypes extends VolumeTypesInterface {
    }

    public interface RelativeModes {
        String OBJECT_CENTROID = "Object centroid";
        String OBJECT_SURFACE = "Object surface";
        String PARENT_CENTROID = "Parent centroid";

        String[] ALL = new String[] { OBJECT_CENTROID, OBJECT_SURFACE, PARENT_CENTROID };

    }

    public interface BandModes {
        String INSIDE_AND_OUTSIDE = "Inside and outside";
        String INSIDE_OBJECTS = "Inside objects";
        String OUTSIDE_OBJECTS = "Outside objects";

        String[] ALL = new String[] { INSIDE_AND_OUTSIDE, INSIDE_OBJECTS, OUTSIDE_OBJECTS };

    }

    public interface WeightModes extends DistanceMap.WeightModes {
    }

    public interface SpatialUnitsModes extends SpatialUnitsInterface {
    }

    public interface DetectionModes extends IdentifyObjects.DetectionModes {
    }

    public interface Measurements {
        String CENTRAL_DISTANCE_PX = "CENTRAL_DIST_(PX)";
        String CENTRAL_DISTANCE_CAL = "CENTRAL_DIST_(${SCAL})";
        String MIN_DISTANCE_PX = "MIN_DIST_(PX)";
        String MIN_DISTANCE_CAL = "MIN_DIST_(${SCAL})";
        String MAX_DISTANCE_PX = "MAX_DIST_(PX)";
        String MAX_DISTANCE_CAL = "MAX_DIST_(${SCAL})";

    }

    public static String getFullName(String measurement) {
        return "BANDS // " + measurement;
    }

    @Override
    public Category getCategory() {
        return Categories.OBJECTS_PROCESS;
    }

    @Override
    public String getVersionNumber() {
        return "1.1.0";
    }

    @Override
    public String getDescription() {
        return "";
    }

    public static <T extends RealType<T> & NativeType<T>> Objs getAllBands(Image<T> inputImage, Image<T> maskImage,
            String outputObjectsName, String weightMode, boolean matchZToXY, double bandWidthPx, double minDistPx,
            double maxDistPx, String type) {
        // Get distance map
        Objs internalBands = getBands(inputImage, maskImage, outputObjectsName, true, weightMode, matchZToXY,
                bandWidthPx, minDistPx, maxDistPx, type);
        Objs externalBands = getBands(inputImage, maskImage, outputObjectsName, false, weightMode, matchZToXY,
                bandWidthPx, minDistPx, maxDistPx, type);

        // Getting max ID for internal bands
        int ID = internalBands.getLargestID() + 1;
        for (Obj externalBand : externalBands.values()) {
            externalBand.setID(ID++);
            internalBands.add(externalBand);
        }

        return internalBands;

    }

    public static <T extends RealType<T> & NativeType<T>> Objs getBands(Image<T> inputImage, Image<T> maskImage,
            String outputObjectsName, boolean internalBands, String weightMode, boolean matchZToXY, double bandWidthPx,
            double minDistPx, double maxDistPx, String type) {
        Image<T> distPx = DistanceMap.process(inputImage, "Distance", true, weightMode, matchZToXY, false);
        ImageMath.process(distPx, ImageMath.CalculationModes.DIVIDE, bandWidthPx);

        // Applying masking (can occur due to ZtoXY interpolation)
        String calculation = internalBands ? ImageCalculator.CalculationMethods.AND
                : ImageCalculator.CalculationMethods.NOT;
        ImageCalculator.process(distPx, maskImage, calculation,
                ImageCalculator.OverwriteModes.OVERWRITE_IMAGE1, null, true, false);

        ImgPlus<T> distPxImg = distPx.getImgPlus();
        LoopBuilder.setImages(distPxImg).forEachPixel((s) -> s.setReal(Math.ceil(s.getRealDouble())));
        distPx.setImgPlus(distPxImg);
        ImageTypeConverter.process(distPx, 16, ImageTypeConverter.ScalingModes.CLIP);

        // Applying distance limits
        applyDistanceLimits(distPx, minDistPx, maxDistPx, bandWidthPx);

        // Creating external bands objects
        Objs bands = distPx.convertImageToObjects(type, outputObjectsName, false);

        addMeasurements(bands, distPx, bandWidthPx, false);

        return bands;

    }

    public static <T extends RealType<T> & NativeType<T>> void applyDistanceLimits(Image<T> inputImage,
            double minDistPx, double maxDistPx, double bandWidthPx) {
        // Skipping this step if no limits were set
        if (minDistPx == 0 && maxDistPx == Double.MAX_VALUE)
            return;

        // Converting limits to scaled values
        double minDist = minDistPx / bandWidthPx;
        double maxDist = maxDistPx / bandWidthPx;

        ImgPlus<T> img = inputImage.getImgPlus();
        LoopBuilder.setImages(img).forEachPixel((s) -> s.setReal(
                s.getRealDouble() >= minDist && s.getRealDouble() <= maxDist ? s.getRealDouble() : Double.NaN));
        inputImage.setImgPlus(img);

    }

    static <T extends RealType<T> & NativeType<T>> void addMeasurements(Objs bands, Image<T> distPx, double bandWidthPx,
            boolean internalObjects) {
        double dppXY = bands.getDppXY();
        double sign = internalObjects ? -1 : 1;
        ImgPlus<T> distPxImg = distPx.getImgPlus();

        // Applying measurements
        for (Obj obj : bands.values()) {
            // Measure distance value of first pixel
            Point pt = obj.getImgPlusCoordinateIterator(distPxImg, 0).next();
            double val = distPxImg.getAt(pt).getRealDouble();

            // Converting value to distance
            double minDist = (val - 1) * bandWidthPx * sign;
            double maxDist = val * bandWidthPx * sign;
            double centDist = (minDist + maxDist) / 2;

            obj.addMeasurement(new Measurement(getFullName(Measurements.CENTRAL_DISTANCE_PX), centDist));
            obj.addMeasurement(new Measurement(getFullName(Measurements.CENTRAL_DISTANCE_CAL), centDist * dppXY));
            obj.addMeasurement(new Measurement(getFullName(Measurements.MIN_DISTANCE_PX), minDist));
            obj.addMeasurement(new Measurement(getFullName(Measurements.MIN_DISTANCE_CAL), minDist * dppXY));
            obj.addMeasurement(new Measurement(getFullName(Measurements.MAX_DISTANCE_PX), maxDist));
            obj.addMeasurement(new Measurement(getFullName(Measurements.MAX_DISTANCE_CAL), maxDist * dppXY));

        }
    }

    @Override
    public Status process(Workspace workspace) {
        // Getting parameters
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS, workspace);
        String outputObjectsName = parameters.getValue(OUTPUT_OBJECTS, workspace);
        String type = parameters.getValue(VOLUME_TYPE, workspace);

        String relativeMode = parameters.getValue(RELATIVE_MODE, workspace);
        String parentObjectsName = parameters.getValue(PARENT_OBJECTS, workspace);
        String bandMode = parameters.getValue(BAND_MODE, workspace);
        boolean matchZToXY = parameters.getValue(MATCH_Z_TO_X, workspace);
        String weightMode = parameters.getValue(WEIGHT_MODE, workspace);
        double bandWidth = parameters.getValue(BAND_WIDTH, workspace);
        String spatialUnits = parameters.getValue(SPATIAL_UNITS_MODE, workspace);
        boolean applyMinDist = parameters.getValue(APPLY_MINIMUM_BAND_DISTANCE, workspace);
        double minDist = parameters.getValue(MINIMUM_BAND_DISTANCE, workspace);
        boolean applyMaxDist = parameters.getValue(APPLY_MAXIMUM_BAND_DISTANCE, workspace);
        double maxDist = parameters.getValue(MAXIMUM_BAND_DISTANCE, workspace);

        // Getting input objects
        Objs inputObjects = workspace.getObjects(inputObjectsName);

        // Applying spatial calibration
        if (spatialUnits.equals(SpatialUnitsModes.CALIBRATED)) {
            double dppXY = inputObjects.getDppXY();
            bandWidth = bandWidth / dppXY;
            minDist = minDist / dppXY;
            maxDist = maxDist / dppXY;
        }

        // Removing distance limits if not being used
        if (!applyMinDist)
            minDist = 0;
        if (!applyMaxDist)
            maxDist = Double.MAX_VALUE;

        // Creating output bands objects
        Objs bandObjects = new Objs(outputObjectsName, inputObjects);

        // Iterating over each object, creating distance bands
        for (Obj inputObject : inputObjects.values()) {
            // Creating binary image
            Image inputImage;
            switch (relativeMode) {
                default:
                case RelativeModes.OBJECT_CENTROID:
                    inputImage = inputObject.getCentroidAsImage("Binary", false);
                    InvertIntensity.process(inputImage);
                    break;
                case RelativeModes.OBJECT_SURFACE:
                    inputImage = inputObject.getAsImage("Binary", false);
                    break;
                case RelativeModes.PARENT_CENTROID:
                    Obj parentObject = inputObject.getParent(parentObjectsName);
                    if (parentObject == null)
                        continue;
                    inputImage = parentObject.getCentroidAsImage("Binary", false);
                    InvertIntensity.process(inputImage);
                    break;
            }

            Image<T> maskImage = inputObject.getAsImage("Mask", false);

            Objs tempBandObjects;
            switch (bandMode) {
                case BandModes.INSIDE_AND_OUTSIDE:
                default:
                    tempBandObjects = getAllBands(inputImage, maskImage, outputObjectsName, weightMode, matchZToXY,
                            bandWidth, minDist, maxDist, type);
                    break;
                case BandModes.INSIDE_OBJECTS:
                    tempBandObjects = getBands(inputImage, maskImage, outputObjectsName, true, weightMode, matchZToXY,
                            bandWidth, minDist, maxDist, type);
                    break;
                case BandModes.OUTSIDE_OBJECTS:
                    tempBandObjects = getBands(inputImage, maskImage, outputObjectsName, false, weightMode, matchZToXY,
                            bandWidth, minDist, maxDist, type);
                    break;
            }

            // Transferring new band objects to main collection
            for (Obj tempBandObject : tempBandObjects.values()) {
                tempBandObject.setID(bandObjects.getAndIncrementID());
                for (Measurement measurement : tempBandObject.getMeasurements().values())
                    tempBandObject.addMeasurement(new Measurement(measurement.getName(), measurement.getValue()));

                tempBandObject.addParent(inputObject);
                inputObject.addChild(tempBandObject);

                bandObjects.add(tempBandObject);
            }
        }

        // Adding objects to workspace
        workspace.addObjects(bandObjects);

        // Showing objects
        if (showOutput)
            bandObjects.convertToImageIDColours().show();

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        parameters.add(new SeparatorP(INPUT_SEPARATOR, this));
        parameters.add(new InputObjectsP(INPUT_OBJECTS, this));
        parameters.add(new OutputObjectsP(OUTPUT_OBJECTS, this));
        parameters.add(new ChoiceP(VOLUME_TYPE, this, VolumeTypes.POINTLIST, VolumeTypes.ALL));

        parameters.add(new SeparatorP(BAND_SEPARATOR, this));
        parameters.add(new ChoiceP(RELATIVE_MODE, this, RelativeModes.OBJECT_CENTROID, RelativeModes.ALL));
        parameters.add(new ParentObjectsP(PARENT_OBJECTS, this));
        parameters.add(new ChoiceP(BAND_MODE, this, BandModes.INSIDE_AND_OUTSIDE, BandModes.ALL));
        parameters.add(new BooleanP(MATCH_Z_TO_X, this, true));
        parameters.add(new ChoiceP(WEIGHT_MODE, this, WeightModes.WEIGHTS_3_4_5_7, WeightModes.ALL));
        parameters.add(new DoubleP(BAND_WIDTH, this, 1));
        parameters.add(new ChoiceP(SPATIAL_UNITS_MODE, this, SpatialUnitsModes.PIXELS, SpatialUnitsModes.ALL));
        parameters.add(new BooleanP(APPLY_MINIMUM_BAND_DISTANCE, this, false));
        parameters.add(new DoubleP(MINIMUM_BAND_DISTANCE, this, 0));
        parameters.add(new BooleanP(APPLY_MAXIMUM_BAND_DISTANCE, this, false));
        parameters.add(new DoubleP(MAXIMUM_BAND_DISTANCE, this, 1));

        addParameterDescriptions();

    }

    @Override
    public Parameters updateAndGetParameters() {
        Parameters returnedParameters = new Parameters();

        returnedParameters.add(parameters.get(INPUT_SEPARATOR));
        returnedParameters.add(parameters.get(INPUT_OBJECTS));
        returnedParameters.add(parameters.get(OUTPUT_OBJECTS));

        returnedParameters.add(parameters.get(VOLUME_TYPE));

        returnedParameters.add(parameters.get(BAND_SEPARATOR));
        returnedParameters.add(parameters.get(RELATIVE_MODE));
        switch ((String) parameters.getValue(RELATIVE_MODE, null)) {
            case RelativeModes.PARENT_CENTROID:
                returnedParameters.add(parameters.get(PARENT_OBJECTS));
                ParentObjectsP param = parameters.getParameter(PARENT_OBJECTS);
                param.setChildObjectsName(parameters.getValue(INPUT_OBJECTS, null));
                break;
        }
        returnedParameters.add(parameters.get(BAND_MODE));
        returnedParameters.add(parameters.get(MATCH_Z_TO_X));
        returnedParameters.add(parameters.get(WEIGHT_MODE));
        returnedParameters.add(parameters.get(BAND_WIDTH));
        returnedParameters.add(parameters.get(SPATIAL_UNITS_MODE));
        returnedParameters.add(parameters.get(APPLY_MINIMUM_BAND_DISTANCE));
        if ((boolean) parameters.getValue(APPLY_MINIMUM_BAND_DISTANCE, null))
            returnedParameters.add(parameters.get(MINIMUM_BAND_DISTANCE));
        returnedParameters.add(parameters.get(APPLY_MAXIMUM_BAND_DISTANCE));
        if ((boolean) parameters.getValue(APPLY_MAXIMUM_BAND_DISTANCE, null))
            returnedParameters.add(parameters.get(MAXIMUM_BAND_DISTANCE));

        return returnedParameters;

    }

    @Override
    public ImageMeasurementRefs updateAndGetImageMeasurementRefs() {
        return null;
    }

    @Override
    public ObjMeasurementRefs updateAndGetObjectMeasurementRefs() {
        Workspace workspace = null;
        ObjMeasurementRefs returnedRefs = new ObjMeasurementRefs();
        String outputObjects = parameters.getValue(OUTPUT_OBJECTS, workspace);

        String name = getFullName(Measurements.CENTRAL_DISTANCE_PX);
        ObjMeasurementRef reference = objectMeasurementRefs.getOrPut(name);
        reference.setObjectsName(outputObjects);
        returnedRefs.add(reference);

        name = getFullName(Measurements.CENTRAL_DISTANCE_CAL);
        reference = objectMeasurementRefs.getOrPut(name);
        reference.setObjectsName(outputObjects);
        returnedRefs.add(reference);

        name = getFullName(Measurements.MIN_DISTANCE_PX);
        reference = objectMeasurementRefs.getOrPut(name);
        reference.setObjectsName(outputObjects);
        returnedRefs.add(reference);

        name = getFullName(Measurements.MIN_DISTANCE_CAL);
        reference = objectMeasurementRefs.getOrPut(name);
        reference.setObjectsName(outputObjects);
        returnedRefs.add(reference);

        name = getFullName(Measurements.MAX_DISTANCE_PX);
        reference = objectMeasurementRefs.getOrPut(name);
        reference.setObjectsName(outputObjects);
        returnedRefs.add(reference);

        name = getFullName(Measurements.MAX_DISTANCE_CAL);
        reference = objectMeasurementRefs.getOrPut(name);
        reference.setObjectsName(outputObjects);
        returnedRefs.add(reference);

        return returnedRefs;

    }

    @Override
    public MetadataRefs updateAndGetMetadataReferences() {
        return null;
    }

    @Override
    public ParentChildRefs updateAndGetParentChildRefs() {
        ParentChildRefs returnedRefs = new ParentChildRefs();

        String parentObjectsName = parameters.getValue(INPUT_OBJECTS, null);
        String childObjectsName = parameters.getValue(OUTPUT_OBJECTS, null);
        returnedRefs.add(parentChildRefs.getOrPut(parentObjectsName, childObjectsName));

        return returnedRefs;

    }

    @Override
    public PartnerRefs updateAndGetPartnerRefs() {
        return null;
    }

    @Override
    public boolean verify() {
        return true;
    }

    void addParameterDescriptions() {

    }
}