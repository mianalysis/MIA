package io.github.mianalysis.mia.module.objects.detect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Toolbar;
import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.module.Categories;
import io.github.mianalysis.mia.module.Category;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.module.inputoutput.abstrakt.AbstractSaver;
import io.github.mianalysis.mia.module.objects.track.TrackObjects;
import io.github.mianalysis.mia.object.Objs;
import io.github.mianalysis.mia.object.VolumeTypesInterface;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.parameters.BooleanP;
import io.github.mianalysis.mia.object.parameters.ChoiceP;
import io.github.mianalysis.mia.object.parameters.FilePathP;
import io.github.mianalysis.mia.object.parameters.InputImageP;
import io.github.mianalysis.mia.object.parameters.Parameters;
import io.github.mianalysis.mia.object.parameters.SeparatorP;
import io.github.mianalysis.mia.object.parameters.objects.OutputObjectsP;
import io.github.mianalysis.mia.object.parameters.objects.OutputTrackObjectsP;
import io.github.mianalysis.mia.object.parameters.text.StringP;
import io.github.mianalysis.mia.object.parameters.text.TextAreaP;
import io.github.mianalysis.mia.object.refs.ObjMetadataRef;
import io.github.mianalysis.mia.object.refs.collections.ImageMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.MetadataRefs;
import io.github.mianalysis.mia.object.refs.collections.ObjMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.ObjMetadataRefs;
import io.github.mianalysis.mia.object.refs.collections.ParentChildRefs;
import io.github.mianalysis.mia.object.refs.collections.PartnerRefs;
import io.github.mianalysis.mia.object.system.Status;
import io.github.mianalysis.mia.process.exceptions.IntegerOverflowException;
import io.github.mianalysis.mia.process.selectors.ClassSelector;
import io.github.mianalysis.mia.process.selectors.ObjectSelector;
import util.opencsv.CSVReader;

/**
 * Created by sc13967 on 27/02/2018.
 */

/**
 * Manually create objects using the ImageJ selection tools. Selected regions
 * can be interpolated in Z and T to speed up the object creation process.<br>
 * <br>
 * This module will display a control panel and an image onto which selections
 * are made. <br>
 * <br>
 * Following selection of a region to be included in the object, the user can
 * either add this region to a new object ("Add new" button), or add it to an
 * existing object ("Add to existing" button). The target object for adding to
 * an existing object is specified using the "Existing object number" control (a
 * list of existing object IDs is shown directly below this control).<br>
 * <br>
 * References to each selection are displayed below the controls.
 * Previously-added regions can be re-selected by clicking the relevant
 * reference. This allows selections to be deleted or used as a basis for
 * further selections.<br>
 * <br>
 * Once all selections have been made, objects are added to the workspace with
 * the "Finish" button.<br>
 * <br>
 * Objects need to be added slice-by-slice and can be linked in 3D using the
 * "Add to existing" control.
 */
@Plugin(type = Module.class, priority = Priority.LOW, visible = true)
public class ManuallyIdentifyObjects extends AbstractSaver {
    /**
    * 
    */
    public static final String INPUT_SEPARATOR = "Image input";

    /**
     * Image onto which selections will be drawn. This will be displayed
     * automatically when the module runs.
     */
    public static final String INPUT_IMAGE = "Input image";

    /**
    * 
    */
    public static final String OUTPUT_SEPARATOR = "Object output";

    /**
     * Objects created by this module.
     */
    public static final String OUTPUT_OBJECTS = "Output objects";

    /**
     * The method used to store pixel coordinates. This only affects performance and
     * memory usage, there is no difference in results obtained using difference
     * storage methods.<br>
     * <ul>
     * <li>"Pointlist" (default) stores object coordinates as a list of XYZ
     * coordinates. This is most efficient for small objects, very thin objects or
     * objects with lots of holes.</li>
     * <li>"Octree" stores objects in an octree format. Here, the coordinate space
     * is broken down into cubes of different sizes, each of which is marked as
     * foreground (i.e. an object) or background. Octrees are most efficient when
     * there are lots of large cubic regions of the same label, as the space can be
     * represented by larger (and thus fewer) cubes. This is best used when there
     * are large, completely solid objects. If z-axis sampling is much larger than
     * xy-axis sampling, it's typically best to opt for the quadtree method.</li>
     * <li>"Quadtree" stores objects in a quadtree format. Here, each Z-plane of the
     * object is broken down into squares of different sizes, each of which is
     * marked as foreground (i.e. an object) or background. Quadtrees are most
     * efficient when there are lots of large square regions of the same label, as
     * the space can be represented by larger (and thus fewer) squares. This is best
     * used when there are large, completely solid objects.</li>
     * </ul>
     */
    public static final String VOLUME_TYPE = "Volume type";

    /**
     * When selected, the same object can be identified across multiple timepoints.
     * The same ID should be used for all objects in this "track" - this will become
     * the ID of the track object itself, while each timepoint instance will be
     * assigned its own unique ID. This feature also enables the use of temporal
     * intepolation of objects.
     */
    public static final String OUTPUT_TRACKS = "Output tracks";

    /**
     * Name of track objects to be added to the workspace. These will be parents of
     * the individual timepoint instances and provide a way of grouping all the
     * individual timepoint instances of a particular object. Track objects
     * themselves do not contain any coordinate information.
     */
    public static final String OUTPUT_TRACK_OBJECTS = "Output track objects";

    /**
     * Interpolate objects in Z. Objects assigned the same ID will be interpolated
     * to appear in all slices between the top-most and bottom-most specific slices.
     * Specified regions must contain a degree of overlap (higher overlap will give
     * better results).
     */
    public static final String SPATIAL_INTERPOLATION = "Spatial interpolation";

    /**
     * Interpolate objects across multiple frames. Objects assigned the same ID will
     * be interpolated to appear in all frames between the first and last specified
     * timepoints. Specified regions must contain a degree of overlap (higher
     * overlap will give better results).
     */
    public static final String TEMPORAL_INTERPOLATION = "Temporal interpolation";

    public static final String CLASS_SEPARATOR = "Class controls";

    public static final String ASSIGN_CLASSES = "Assign classes";

    public static final String CLASSES_SOURCE = "Classes source";

    public static final String CLASS_FILE = "Class file";

    public static final String ALLOW_ADDITIONS = "Allow additions";

    public static final String CLASS_LIST = "Class list (comma-separated)";

    /**
    * 
    */
    public static final String SELECTION_SEPARATOR = "Object selection controls";

    /**
     * Text that will be displayed to the user on the object selection control
     * panel. This can inform them of the steps they need to take to select the
     * objects.
     */
    public static final String INSTRUCTION_TEXT = "Instruction text";

    /**
     * Default region drawing tool to enable. This tool can be changed by the user
     * when selecting regions. Choices are: Freehand line, Freehand region, Line,
     * Oval, Points, Polygon, Rectangle, Segmented line, Wand (tracing) tool.
     */
    public static final String SELECTOR_TYPE = "Default selector type";
    public static final String POINT_MODE = "Point mode (point-type ROIs only)";

    /**
     * Message to display in title of image.
     */
    public static final String MESSAGE_ON_IMAGE = "Message on image";

    public ManuallyIdentifyObjects(Modules modules) {
        super("Manually identify objects", modules);
    }

    public interface SelectorTypes {
        String FREEHAND_LINE = "Freehand line";
        String FREEHAND_REGION = "Freehand region";
        String LINE = "Line";
        String OVAL = "Oval";
        String POINTS = "Points";
        String POLYGON = "Polygon";
        String RECTANGLE = "Rectangle";
        String SEGMENTED_LINE = "Segmented line";
        String WAND = "Wand (tracing) tool";

        String[] ALL = new String[] { FREEHAND_LINE, FREEHAND_REGION, LINE, OVAL, POINTS, POLYGON, RECTANGLE,
                SEGMENTED_LINE, WAND };

    }

    public interface SpatialInterpolationModes {
        String NONE = "None";
        String SPATIAL = "Spatial";

        String[] ALL = new String[] { NONE, SPATIAL };

    }

    public interface SpatioTemporalInterpolationModes extends SpatialInterpolationModes {
        String NONE = "None";
        String TEMPORAL = "Temporal";
        String SPATIAL_AND_TEMPORAL = "Spatial and temporal";

        String[] ALL = new String[] { NONE, SPATIAL, TEMPORAL, SPATIAL_AND_TEMPORAL };

    }

    public interface VolumeTypes extends VolumeTypesInterface {
    }

    public interface ClassesSources {
        String EXISTING_CLASS_FILE = "Existing class file";
        String FIXED_LIST = "Fixed list";
        String NEW_CLASS_FILE = "New class file";

        String[] ALL = new String[] { EXISTING_CLASS_FILE, FIXED_LIST, NEW_CLASS_FILE };

    }

    public interface PointModes extends ObjectSelector.PointModes {
    }

    public interface ObjMetadataItems extends ObjectSelector.ObjMetadataItems {
    }

    void setSelector(String selectorType) {
        switch (selectorType) {
            case SelectorTypes.FREEHAND_LINE:
                IJ.setTool(Toolbar.FREELINE);
                return;
            default:
            case SelectorTypes.FREEHAND_REGION:
                IJ.setTool(Toolbar.FREEROI);
                return;
            case SelectorTypes.LINE:
                IJ.setTool(Toolbar.LINE);
                return;
            case SelectorTypes.OVAL:
                IJ.setTool(Toolbar.OVAL);
                return;
            case SelectorTypes.POINTS:
                IJ.setTool(Toolbar.POINT);
                return;
            case SelectorTypes.RECTANGLE:
                IJ.setTool(Toolbar.RECTANGLE);
                return;
            case SelectorTypes.SEGMENTED_LINE:
                IJ.setTool(Toolbar.POLYLINE);
                return;
            case SelectorTypes.POLYGON:
                IJ.setTool(Toolbar.POLYGON);
                return;
            case SelectorTypes.WAND:
                IJ.setTool(Toolbar.WAND);
                return;
        }
    }

    @Override
    public Category getCategory() {
        return Categories.OBJECTS_DETECT;
    }

    @Override
    public String getVersionNumber() {
        return "1.0.2";
    }

    @Override
    public String getDescription() {
        return "Manually create objects using the ImageJ selection tools.  Selected regions can be interpolated in Z and T to speed up the object creation process."
                + "<br><br>This module will display a control panel and an image onto which selections are made.  "
                + "<br><br>Following selection of a region to be included in the object, the user can either add this region to a new object (\"Add new\" button), or add it to an existing object (\"Add to existing\" button).  "
                + "The target object for adding to an existing object is specified using the \"Existing object number\" control (a list of existing object IDs is shown directly below this control)."
                + "<br><br>References to each selection are displayed below the controls.  Previously-added regions can be re-selected by clicking the relevant reference.  This allows selections to be deleted or used as a basis for further selections."
                + "<br><br>Once all selections have been made, objects are added to the workspace with the \"Finish\""
                + " button.<br><br>Objects need to be added slice-by-slice and can be linked in 3D using the \""
                + "Add to existing\" control.";
    }

    static TreeSet<String> getClasses(String classesSource, String classFile, String classList) {
        TreeSet<String> classes = new TreeSet<>();

        switch (classesSource) {
            case ClassesSources.EXISTING_CLASS_FILE:
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new FileReader(new File(classFile)));
                } catch (FileNotFoundException e) {
                    MIA.log.writeWarning("File not found: \"" + classFile + "\"");
                    return null;
                }

                CSVReader csvReader = new CSVReader(reader);
                try {
                    String[] row = csvReader.readNext();
                    while (row != null) {
                        for (String item : row)
                            classes.add(item);

                        row = csvReader.readNext();

                    }
                } catch (IOException e) {
                    MIA.log.writeError(e);
                    return null;
                }
                break;
            case ClassesSources.FIXED_LIST:
                String[] classesList = classList.split(",");
                for (String item:classesList)
                    classes.add(item);

                break;
        }

        return classes;

    }

    @Override
    public Status process(Workspace workspace) {// Local access to this is required for the action listeners
        // Getting parameters
        String inputImageName = parameters.getValue(INPUT_IMAGE, workspace);
        String outputObjectsName = parameters.getValue(OUTPUT_OBJECTS, workspace);
        String type = parameters.getValue(VOLUME_TYPE, workspace);
        boolean outputTracks = parameters.getValue(OUTPUT_TRACKS, workspace);
        String outputTrackObjectsName = parameters.getValue(OUTPUT_TRACK_OBJECTS, workspace);
        boolean spatialInterpolation = parameters.getValue(SPATIAL_INTERPOLATION, workspace);
        boolean temporalInterpolation = parameters.getValue(TEMPORAL_INTERPOLATION, workspace);
        String instructionText = parameters.getValue(INSTRUCTION_TEXT, workspace);
        String selectorType = parameters.getValue(SELECTOR_TYPE, workspace);
        String messageOnImage = parameters.getValue(MESSAGE_ON_IMAGE, workspace);
        String volumeTypeString = parameters.getValue(VOLUME_TYPE, workspace);
        String pointMode = parameters.getValue(POINT_MODE, workspace);
        boolean assignClasses = parameters.getValue(ASSIGN_CLASSES, workspace);
        String classesSource = parameters.getValue(CLASSES_SOURCE, workspace);
        String classFile = parameters.getValue(CLASS_FILE, workspace);
        boolean allowAdditions = parameters.getValue(ALLOW_ADDITIONS, workspace);
        String classList = parameters.getValue(CLASS_LIST, workspace);

        // Getting input image
        Image inputImage = workspace.getImage(inputImageName);
        ImagePlus inputImagePlus = inputImage.getImagePlus();

        setSelector(selectorType);

        if (!outputTracks)
            outputTrackObjectsName = null;

        ClassSelector classSelector = null;
        if (assignClasses) {
            TreeSet<String> classes = getClasses(classesSource, classFile, classList);
            classSelector = new ClassSelector(classes);
        }

        ObjectSelector objectSelector = new ObjectSelector(inputImagePlus, outputObjectsName, messageOnImage,
                instructionText, volumeTypeString, pointMode, outputTrackObjectsName, classSelector);

        // All the while the control is open, do nothing
        while (objectSelector.isActive())
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

        // If more pixels than Integer.MAX_VALUE were assigned, return false
        // (IntegerOverflowException).
        if (objectSelector.hadOverflow())
            return Status.FAIL;

        // Getting objects
        Objs outputObjects = objectSelector.getObjects();
        Objs outputTrackObjects = objectSelector.getTrackObjects();

        // If necessary, apply interpolation
        try {
            if (spatialInterpolation)
                ObjectSelector.applySpatialInterpolation(outputObjects, type);
            if (outputTracks && temporalInterpolation)
                ObjectSelector.applyTemporalInterpolation(outputObjects, outputTrackObjects, type);
        } catch (IntegerOverflowException e) {
            return Status.FAIL;
        }

        workspace.addObjects(outputObjects);

        // Showing the selected objects
        if (showOutput)
            if (outputTracks)
                TrackObjects.showObjects(outputObjects, outputTrackObjectsName);
            else
                outputObjects.convertToImageIDColours().show();

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        super.initialiseParameters();

        parameters.add(new SeparatorP(INPUT_SEPARATOR, this));
        parameters.add(new InputImageP(INPUT_IMAGE, this));

        parameters.add(new SeparatorP(OUTPUT_SEPARATOR, this));
        parameters.add(new OutputObjectsP(OUTPUT_OBJECTS, this));
        parameters.add(new ChoiceP(VOLUME_TYPE, this, VolumeTypes.POINTLIST, VolumeTypes.ALL));
        parameters.add(new BooleanP(SPATIAL_INTERPOLATION, this, false));
        parameters.add(new BooleanP(OUTPUT_TRACKS, this, false));
        parameters.add(new OutputTrackObjectsP(OUTPUT_TRACK_OBJECTS, this));
        parameters.add(new BooleanP(TEMPORAL_INTERPOLATION, this, false));

        parameters.add(new SeparatorP(CLASS_SEPARATOR, this));
        parameters.add(new BooleanP(ASSIGN_CLASSES, this, false));
        parameters.add(new ChoiceP(CLASSES_SOURCE, this, ClassesSources.FIXED_LIST, ClassesSources.ALL));
        parameters.add(new FilePathP(CLASS_FILE, this));
        parameters.add(new BooleanP(ALLOW_ADDITIONS, this, false));
        parameters.add(new StringP(CLASS_LIST, this));

        parameters.add(new SeparatorP(SELECTION_SEPARATOR, this));
        parameters.add(new TextAreaP(INSTRUCTION_TEXT, this,
                "Draw round an object, then select one of the following"
                        + "\n(or click \"Finish adding objects\" at any time)."
                        + "\nDifferent timepoints must be added as new objects.",
                true, 100));
        parameters.add(new ChoiceP(SELECTOR_TYPE, this, SelectorTypes.FREEHAND_REGION, SelectorTypes.ALL));
        parameters.add(new ChoiceP(POINT_MODE, this, PointModes.INDIVIDUAL_OBJECTS, PointModes.ALL));
        parameters.add(new StringP(MESSAGE_ON_IMAGE, this, "Draw objects on this image"));

        addParameterDescriptions();

    }

    @Override
    public Parameters updateAndGetParameters() {
        Workspace workspace = null;
        Parameters returnedParameters = new Parameters();

        returnedParameters.add(parameters.get(INPUT_SEPARATOR));
        returnedParameters.add(parameters.get(INPUT_IMAGE));

        returnedParameters.add(parameters.get(OUTPUT_SEPARATOR));
        returnedParameters.add(parameters.get(OUTPUT_OBJECTS));
        returnedParameters.add(parameters.get(VOLUME_TYPE));
        returnedParameters.add(parameters.get(POINT_MODE)); // Must always be visible, as user can change ROI type
        returnedParameters.add(parameters.get(SPATIAL_INTERPOLATION));
        returnedParameters.add(parameters.get(OUTPUT_TRACKS));

        if ((boolean) parameters.getValue(OUTPUT_TRACKS, workspace)) {
            returnedParameters.add(parameters.get(OUTPUT_TRACK_OBJECTS));
            returnedParameters.add(parameters.get(TEMPORAL_INTERPOLATION));
        }

        returnedParameters.add(parameters.get(CLASS_SEPARATOR));
        returnedParameters.add(parameters.get(ASSIGN_CLASSES));
        if ((boolean) parameters.getValue(ASSIGN_CLASSES, workspace)) {
            returnedParameters.add(parameters.get(CLASSES_SOURCE));
            switch ((String) parameters.getValue(CLASSES_SOURCE, workspace)) {
                case ClassesSources.EXISTING_CLASS_FILE:
                    returnedParameters.add(parameters.get(CLASS_FILE));
                    returnedParameters.add(parameters.get(ALLOW_ADDITIONS));
                    break;
                case ClassesSources.FIXED_LIST:
                    returnedParameters.add(parameters.get(CLASS_LIST));
                    break;
                case ClassesSources.NEW_CLASS_FILE:
                    Parameters saverParameters = super.updateAndGetParameters();
                    saverParameters.remove(FILE_SAVING_SEPARATOR);
                    returnedParameters.addAll(saverParameters);
                    break;
            }
        }

        returnedParameters.add(parameters.get(SELECTION_SEPARATOR));
        returnedParameters.add(parameters.get(INSTRUCTION_TEXT));
        returnedParameters.add(parameters.get(SELECTOR_TYPE));
        returnedParameters.add(parameters.get(MESSAGE_ON_IMAGE));

        return returnedParameters;

    }

    @Override
    public ImageMeasurementRefs updateAndGetImageMeasurementRefs() {
        return null;
    }

    @Override
    public ObjMeasurementRefs updateAndGetObjectMeasurementRefs() {
        return null;
    }

    @Override
    public ObjMetadataRefs updateAndGetObjectMetadataRefs() {
        if (!(boolean) parameters.getValue(ASSIGN_CLASSES, null))
            return null;

        ObjMetadataRefs returnedRefs = new ObjMetadataRefs();

        String objectsName = parameters.getValue(OUTPUT_OBJECTS, null);

        ObjMetadataRef ref = objectMetadataRefs.getOrPut(ObjMetadataItems.CLASS);
        ref.setObjectsName(objectsName);
        returnedRefs.add(ref);

        return returnedRefs;

    }

    @Override
    public MetadataRefs updateAndGetMetadataReferences() {
        return null;
    }

    @Override
    public ParentChildRefs updateAndGetParentChildRefs() {
        Workspace workspace = null;
        ParentChildRefs returnedRelationships = new ParentChildRefs();

        if ((boolean) parameters.getValue(OUTPUT_TRACKS, workspace))
            returnedRelationships.add(parentChildRefs.getOrPut(parameters.getValue(OUTPUT_TRACK_OBJECTS, workspace),
                    parameters.getValue(OUTPUT_OBJECTS, workspace)));

        return returnedRelationships;

    }

    @Override
    public PartnerRefs updateAndGetPartnerRefs() {
        return null;
    }

    @Override
    public boolean verify() {
        return true;
    }

    protected void addParameterDescriptions() {
        super.addParameterDescriptions();

        parameters.get(INPUT_IMAGE).setDescription(
                "Image onto which selections will be drawn.  This will be displayed automatically when the module runs.");

        parameters.get(OUTPUT_OBJECTS).setDescription("Objects created by this module.");

        parameters.get(VOLUME_TYPE).setDescription(
                "The method used to store pixel coordinates.  This only affects performance and memory usage, there is no difference in results obtained using difference storage methods.<br><ul>"
                        + "<li>\"" + VolumeTypes.POINTLIST
                        + "\" (default) stores object coordinates as a list of XYZ coordinates.  This is most efficient for small objects, very thin objects or objects with lots of holes.</li>"
                        + "<li>\"" + VolumeTypes.OCTREE
                        + "\" stores objects in an octree format.  Here, the coordinate space is broken down into cubes of different sizes, each of which is marked as foreground (i.e. an object) or background.  Octrees are most efficient when there are lots of large cubic regions of the same label, as the space can be represented by larger (and thus fewer) cubes.  This is best used when there are large, completely solid objects.  If z-axis sampling is much larger than xy-axis sampling, it's typically best to opt for the quadtree method.</li>"
                        + "<li>\"" + VolumeTypes.QUADTREE
                        + "\" stores objects in a quadtree format.  Here, each Z-plane of the object is broken down into squares of different sizes, each of which is marked as foreground (i.e. an object) or background.  Quadtrees are most efficient when there are lots of large square regions of the same label, as the space can be represented by larger (and thus fewer) squares.  This is best used when there are large, completely solid objects.</li></ul>");

        parameters.get(OUTPUT_TRACKS).setDescription(
                "When selected, the same object can be identified across multiple timepoints.  The same ID should be used for all objects in this \"track\" - this will become the ID of the track object itself, while each timepoint instance will be assigned its own unique ID.  This feature also enables the use of temporal intepolation of objects.");

        parameters.get(OUTPUT_TRACK_OBJECTS).setDescription(
                "Name of track objects to be added to the workspace.  These will be parents of the individual timepoint instances and provide a way of grouping all the individual timepoint instances of a particular object.  Track objects themselves do not contain any coordinate information.");

        parameters.get(SPATIAL_INTERPOLATION).setDescription(
                "Interpolate objects in Z.  Objects assigned the same ID will be interpolated to appear in all slices between the top-most and bottom-most specific slices.  Specified regions must contain a degree of overlap (higher overlap will give better results).");

        parameters.get(TEMPORAL_INTERPOLATION).setDescription(
                "Interpolate objects across multiple frames.  Objects assigned the same ID will be interpolated to appear in all frames between the first and last specified timepoints.  Specified regions must contain a degree of overlap (higher overlap will give better results).");

        parameters.get(INSTRUCTION_TEXT).setDescription(
                "Text that will be displayed to the user on the object selection control panel.  This can inform them of the steps they need to take to select the objects.");

        parameters.get(SELECTOR_TYPE).setDescription(
                "Default region drawing tool to enable.  This tool can be changed by the user when selecting regions.  Choices are: "
                        + String.join(", ", SelectorTypes.ALL) + ".");

        parameters.get(MESSAGE_ON_IMAGE).setDescription("Message to display in title of image.");

    }
}