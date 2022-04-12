package io.github.mianalysis.mia.module.visualise.overlays;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Overlay;
import ij.gui.PointRoi;
import ij.plugin.Duplicator;
import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.module.Categories;
import io.github.mianalysis.mia.module.Category;
import io.github.mianalysis.mia.module.IL2Support;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.object.Obj;
import io.github.mianalysis.mia.object.Objs;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.image.ImageFactory;
import io.github.mianalysis.mia.object.parameters.BooleanP;
import io.github.mianalysis.mia.object.parameters.ChoiceP;
import io.github.mianalysis.mia.object.parameters.InputImageP;
import io.github.mianalysis.mia.object.parameters.InputObjectsP;
import io.github.mianalysis.mia.object.parameters.OutputImageP;
import io.github.mianalysis.mia.object.parameters.Parameters;
import io.github.mianalysis.mia.object.parameters.SeparatorP;
import io.github.mianalysis.mia.object.refs.collections.ImageMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.MetadataRefs;
import io.github.mianalysis.mia.object.refs.collections.ObjMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.ParentChildRefs;
import io.github.mianalysis.mia.object.refs.collections.PartnerRefs;
import io.github.mianalysis.mia.object.system.Preferences;
import io.github.mianalysis.mia.object.system.Status;

@Plugin(type = Module.class, priority = Priority.LOW, visible = true)
public class AddObjectCentroid extends AbstractOverlay {
    public static final String INPUT_SEPARATOR = "Image and object input";
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";

    public static final String OUTPUT_SEPARATOR = "Image output";
    public static final String APPLY_TO_INPUT = "Apply to input image";
    public static final String ADD_OUTPUT_TO_WORKSPACE = "Add output image to workspace";
    public static final String OUTPUT_IMAGE = "Output image";

    public static final String RENDERING_SEPARATOR = "Overlay rendering";
    public static final String POINT_SIZE = "Point size";
    public static final String POINT_TYPE = "Point type";
    public static final String RENDER_IN_ALL_FRAMES = "Render in all frames";

    public static final String EXECUTION_SEPARATOR = "Execution controls";
    public static final String ENABLE_MULTITHREADING = "Enable multithreading";

    public interface PointSizes {
        String TINY = "Tiny";
        String SMALL = "Small";
        String MEDIUM = "Medium";
        String LARGE = "Large";
        String EXTRA_LARGE = "Extra large";

        String[] ALL = new String[] { TINY, SMALL, MEDIUM, LARGE, EXTRA_LARGE };

    }

    public interface PointTypes {
        String CIRCLE = "Circle";
        String CROSS = "Cross";
        String DOT = "Dot";
        String HYBRID = "Hybrid";

        String[] ALL = new String[] { CIRCLE, CROSS, DOT, HYBRID };

    }

    public AddObjectCentroid(Modules modules) {
        super("Add object centroid", modules);
        il2Support = IL2Support.FULL;
    }

    public static void addOverlay(Overlay overlay, Objs inputObjects, HashMap<Integer, Color> colours, String size,
            String type, boolean renderInAllFrames, boolean isHyperStack, boolean multithread) {
        // Adding the overlay element
        try {
            int nThreads = multithread ? Prefs.getThreads() : 1;
            ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());

            // Running through each object, adding it to the overlay along with an ID label
            for (Obj object : inputObjects.values()) {
                Runnable task = () -> {
                    Color colour = colours.get(object.getID());
                    addOverlay(object, overlay, colour, size, type, renderInAllFrames, isHyperStack);
                };
                pool.submit(task);
            }

            pool.shutdown();
            pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS); // i.e. never terminate early

        } catch (InterruptedException e) {
            // Do nothing as the user has selected this
        }
    }

    public static void addOverlay(Obj object, Overlay overlay, Color colour, String size, String type,
            boolean renderInAllFrames, boolean isHyperStack) {
        double xMean = object.getXMean(true);
        double yMean = object.getYMean(true);
        double zMean = object.getZMean(true, false);

        int sizeVal = getSize(size);
        int typeVal = getType(type);

        // Getting coordinates to plot
        int z = (int) Math.round(zMean + 1);
        int t = object.getT() + 1;

        if (renderInAllFrames)
            t = 0;

        // Adding circles where the object centroids are
        PointRoi pointRoi = new PointRoi(xMean + 0.5, yMean + 0.5);
        pointRoi.setPointType(typeVal);
        pointRoi.setSize(sizeVal);
        pointRoi.setStrokeColor(colour);
                    
        if (isHyperStack) {
            pointRoi.setPosition(1, z, t);
        } else {
            int pos = Math.max(Math.max(1, z), t);
            pointRoi.setPosition(pos);
        }
        
        overlay.addElement(pointRoi);

    }

    static int getSize(String size) {
        switch (size) {
            case PointSizes.TINY:
                return 0;
            case PointSizes.SMALL:
            default:
                return 1;
            case PointSizes.MEDIUM:
                return 2;
            case PointSizes.LARGE:
                return 3;
            case PointSizes.EXTRA_LARGE:
                return 4;
        }
    }

    static int getType(String type) {
        switch (type) {
            case PointTypes.HYBRID:
                return 0;
            case PointTypes.CROSS:
                return 1;
            case PointTypes.DOT:
                return 2;
            case PointTypes.CIRCLE:
            default:
                return 3;
        }
    }

    @Override
    public Category getCategory() {
        return Categories.VISUALISATION_OVERLAYS;
    }

    @Override
    public String getDescription() {
        return "Adds an overlay to the specified input image representing each object by a single marker placed at the centroid of that object.";
    }

    @Override
    protected Status process(Workspace workspace) {
        // Getting parameters
        boolean applyToInput = parameters.getValue(APPLY_TO_INPUT);
        boolean addOutputToWorkspace = parameters.getValue(ADD_OUTPUT_TO_WORKSPACE);
        String outputImageName = parameters.getValue(OUTPUT_IMAGE);

        // Getting input objects
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        Objs inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting input image
        String inputImageName = parameters.getValue(INPUT_IMAGE);
        Image image = workspace.getImages().get(inputImageName);

        String pointSize = parameters.getValue(POINT_SIZE);
        String pointType = parameters.getValue(POINT_TYPE);
        boolean renderInAllFrames = parameters.getValue(RENDER_IN_ALL_FRAMES);
        boolean multithread = parameters.getValue(ENABLE_MULTITHREADING);

        // Duplicating the image, so the original isn't altered
        if (!applyToInput)
            image = image.duplicate(outputImageName);

        // Generating colours for each object
        HashMap<Integer, Color> colours = getColours(inputObjects);

        // Getting the overlay and if one doesn't exist, creating one
        Overlay overlay = image.getOverlay();

        boolean isHyperStack = image.getImagePlus().isHyperStack();

        addOverlay(overlay, inputObjects, colours, pointSize, pointType, renderInAllFrames, isHyperStack, multithread);

        // If necessary, adding output image to workspace
        if (!applyToInput && addOutputToWorkspace)
            workspace.addImage(image);

        if (showOutput)
            image.showImage();

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        super.initialiseParameters();

        parameters.add(new SeparatorP(INPUT_SEPARATOR, this));
        parameters.add(new InputImageP(INPUT_IMAGE, this));
        parameters.add(new InputObjectsP(INPUT_OBJECTS, this));

        parameters.add(new SeparatorP(OUTPUT_SEPARATOR, this));
        parameters.add(new BooleanP(APPLY_TO_INPUT, this, false));
        parameters.add(new BooleanP(ADD_OUTPUT_TO_WORKSPACE, this, false));
        parameters.add(new OutputImageP(OUTPUT_IMAGE, this));

        parameters.add(new SeparatorP(RENDERING_SEPARATOR, this));
        parameters.add(new ChoiceP(POINT_SIZE, this, PointSizes.SMALL, PointSizes.ALL));
        parameters.add(new ChoiceP(POINT_TYPE, this, PointTypes.CIRCLE, PointTypes.ALL));
        parameters.add(new BooleanP(RENDER_IN_ALL_FRAMES, this, false));

        parameters.add(new SeparatorP(EXECUTION_SEPARATOR, this));
        parameters.add(new BooleanP(ENABLE_MULTITHREADING, this, true));

        addParameterDescriptions();

    }

    @Override
    public Parameters updateAndGetParameters() {
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        Parameters returnedParameters = new Parameters();

        returnedParameters.add(parameters.getParameter(INPUT_SEPARATOR));
        returnedParameters.add(parameters.getParameter(INPUT_IMAGE));
        returnedParameters.add(parameters.getParameter(INPUT_OBJECTS));

        returnedParameters.add(parameters.getParameter(OUTPUT_SEPARATOR));
        returnedParameters.add(parameters.getParameter(APPLY_TO_INPUT));
        if (!(boolean) parameters.getValue(APPLY_TO_INPUT)) {
            returnedParameters.add(parameters.getParameter(ADD_OUTPUT_TO_WORKSPACE));

            if ((boolean) parameters.getValue(ADD_OUTPUT_TO_WORKSPACE)) {
                returnedParameters.add(parameters.getParameter(OUTPUT_IMAGE));

            }
        }

        returnedParameters.addAll(super.updateAndGetParameters(inputObjectsName));

        returnedParameters.add(parameters.getParameter(RENDERING_SEPARATOR));
        returnedParameters.add(parameters.getParameter(POINT_SIZE));
        returnedParameters.add(parameters.getParameter(POINT_TYPE));
        returnedParameters.add(parameters.getParameter(RENDER_IN_ALL_FRAMES));

        returnedParameters.add(parameters.getParameter(EXECUTION_SEPARATOR));
        returnedParameters.add(parameters.getParameter(ENABLE_MULTITHREADING));

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

    @Override
    protected void addParameterDescriptions() {
        super.addParameterDescriptions();

        parameters.get(INPUT_IMAGE)
                .setDescription("Image onto which overlay will be rendered.  Input image will only be updated if \""
                        + APPLY_TO_INPUT
                        + "\" is enabled, otherwise the image containing the overlay will be stored as a new image with name specified by \""
                        + OUTPUT_IMAGE + "\".");

        parameters.get(INPUT_OBJECTS).setDescription("Objects to represent as overlays.");

        parameters.get(APPLY_TO_INPUT).setDescription(
                "Determines if the modifications made to the input image (added overlay elements) will be applied to that image or directed to a new image.  When selected, the input image will be updated.");

        parameters.get(ADD_OUTPUT_TO_WORKSPACE).setDescription(
                "If the modifications (overlay) aren't being applied directly to the input image, this control will determine if a separate image containing the overlay should be saved to the workspace.");

        parameters.get(OUTPUT_IMAGE).setDescription(
                "The name of the new image to be saved to the workspace (if not applying the changes directly to the input image).");

        parameters.get(POINT_SIZE).setDescription(
                "Size of each overlay marker.  Choices are: " + String.join(", ", PointSizes.ALL) + ".");

        parameters.get(POINT_TYPE).setDescription("Type of overlay marker used to represent each object.  Choices are: "
                + String.join(", ", PointTypes.ALL) + ".");

        parameters.get(RENDER_IN_ALL_FRAMES).setDescription(
                "Display the overlay elements in all frames (time axis) of the input image stack, irrespective of whether the object was present in that frame.");

        parameters.get(ENABLE_MULTITHREADING).setDescription(
                "Process multiple overlay elements simultaneously.  This can provide a speed improvement when working on a computer with a multi-core CPU.");

    }
}
