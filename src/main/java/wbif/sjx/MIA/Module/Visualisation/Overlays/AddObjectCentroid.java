package wbif.sjx.MIA.Module.Visualisation.Overlays;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.PointRoi;
import ij.plugin.Duplicator;
import ij.plugin.HyperStackConverter;
import wbif.sjx.MIA.Module.ModuleCollection;
import wbif.sjx.MIA.Module.PackageNames;
import wbif.sjx.MIA.Object.*;
import wbif.sjx.MIA.Object.Image;
import wbif.sjx.MIA.Object.Parameters.*;
import wbif.sjx.MIA.Object.References.ImageMeasurementRefCollection;
import wbif.sjx.MIA.Object.References.ObjMeasurementRefCollection;
import wbif.sjx.MIA.Object.References.MetadataRefCollection;
import wbif.sjx.MIA.Object.References.RelationshipRefCollection;
import wbif.sjx.MIA.Process.ColourFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AddObjectCentroid extends Overlay {
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
        public String TINY = "Tiny";
        public String SMALL = "Small";
        public String MEDIUM = "Medium";
        public String LARGE = "Large";
        public String EXTRA_LARGE = "Extra large";

        public String[] ALL = new String[]{TINY,SMALL,MEDIUM,LARGE,EXTRA_LARGE};

    }

    public interface PointTypes {
        public String CIRCLE = "Circle";
        public String CROSS = "Cross";
        public String DOT = "Dot";
        public String HYBRID = "Hybrid";

        public String[] ALL = new String[]{CIRCLE,CROSS,DOT,HYBRID};

    }


    public AddObjectCentroid(ModuleCollection modules) {
        super("Add object centroid",modules);
    }

    public static void addOverlay(ImagePlus ipl, ObjCollection inputObjects, HashMap<Integer,Float> hues, String size, String type, boolean renderInAllFrames, boolean multithread) {
        // Adding the overlay element
        try {
            // If necessary, turning the image into a HyperStack (if 2 dimensions=1 it will be a standard ImagePlus)
            if (!ipl.isComposite() & (ipl.getNSlices() > 1 | ipl.getNFrames() > 1 | ipl.getNChannels() > 1)) {
                ipl = HyperStackConverter.toHyperStack(ipl, ipl.getNChannels(), ipl.getNSlices(), ipl.getNFrames());
            }

            int nThreads = multithread ? Prefs.getThreads() : 1;
            ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads,nThreads,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());

            // Running through each object, adding it to the overlay along with an ID label
            AtomicInteger count = new AtomicInteger();
            for (Obj object:inputObjects.values()) {
                ImagePlus finalIpl = ipl;

                Runnable task = () -> {
                    float hue = hues.get(object.getID());
                    Color colour = ColourFactory.getColour(hue);

                    addOverlay(object, finalIpl, colour, size, type, renderInAllFrames);

                };
                pool.submit(task);
            }

            pool.shutdown();
            pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS); // i.e. never terminate early

        } catch (InterruptedException e) {
            return;
        }
    }

    public static void addOverlay(Obj object, ImagePlus ipl, Color colour, String size, String type, boolean renderInAllFrames) {
        if (ipl.getOverlay() == null) ipl.setOverlay(new ij.gui.Overlay());

        double xMean = object.getXMean(true);
        double yMean = object.getYMean(true);
        double zMean = object.getZMean(true,false);

        int sizeVal = getSize(size);
        int typeVal = getType(type);

        // Getting coordinates to plot
        int z = (int) Math.round(zMean+1);
        int t = object.getT()+1;

        if (renderInAllFrames) t = 0;

        // Adding circles where the object centroids are
        PointRoi pointRoi = new PointRoi(xMean+0.5,yMean+0.5);
        pointRoi.setPointType(typeVal);
        pointRoi.setSize(sizeVal);

        if (ipl.isHyperStack()) {
            pointRoi.setPosition(1, z, t);
        } else {
            int pos = Math.max(Math.max(1,z),t);
            pointRoi.setPosition(pos);
        }
        pointRoi.setStrokeColor(colour);
        ipl.getOverlay().addElement(pointRoi);

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
    public String getPackageName() {
        return PackageNames.VISUALISATION_OVERLAYS;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    protected boolean process(Workspace workspace) {
        // Getting parameters
        boolean applyToInput = parameters.getValue(APPLY_TO_INPUT);
        boolean addOutputToWorkspace = parameters.getValue(ADD_OUTPUT_TO_WORKSPACE);
        String outputImageName = parameters.getValue(OUTPUT_IMAGE);

        // Getting input objects
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        ObjCollection inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting input image
        String inputImageName = parameters.getValue(INPUT_IMAGE);
        Image inputImage = workspace.getImages().get(inputImageName);
        ImagePlus ipl = inputImage.getImagePlus();

        String pointSize = parameters.getValue(POINT_SIZE);
        String pointType = parameters.getValue(POINT_TYPE);
        boolean renderInAllFrames = parameters.getValue(RENDER_IN_ALL_FRAMES);
        boolean multithread = parameters.getValue(ENABLE_MULTITHREADING);

        // Only add output to workspace if not applying to input
        if (applyToInput) addOutputToWorkspace = false;

        // Duplicating the image, so the original isn't altered
        if (!applyToInput) ipl = new Duplicator().run(ipl);

        // Generating colours for each object
        HashMap<Integer,Float> hues= getHues(inputObjects);

        addOverlay(ipl,inputObjects,hues,pointSize,pointType,renderInAllFrames,multithread);

        Image outputImage = new Image(outputImageName,ipl);

        // If necessary, adding output image to workspace.  This also allows us to show it.
        if (addOutputToWorkspace) workspace.addImage(outputImage);
        if (showOutput) outputImage.showImage();

        return true;

    }

    @Override
    protected void initialiseParameters() {
        super.initialiseParameters();

        parameters.add(new ParamSeparatorP(INPUT_SEPARATOR,this));
        parameters.add(new InputImageP(INPUT_IMAGE, this));
        parameters.add(new InputObjectsP(INPUT_OBJECTS, this));

        parameters.add(new ParamSeparatorP(OUTPUT_SEPARATOR,this));
        parameters.add(new BooleanP(APPLY_TO_INPUT, this,false));
        parameters.add(new BooleanP(ADD_OUTPUT_TO_WORKSPACE, this,false));
        parameters.add(new OutputImageP(OUTPUT_IMAGE, this));

        parameters.add(new ParamSeparatorP(RENDERING_SEPARATOR,this));
        parameters.add(new ChoiceP(POINT_SIZE,this,PointSizes.SMALL,PointSizes.ALL));
        parameters.add(new ChoiceP(POINT_TYPE,this,PointTypes.CIRCLE,PointTypes.ALL));
        parameters.add(new BooleanP(RENDER_IN_ALL_FRAMES,this,false));

        parameters.add(new ParamSeparatorP(EXECUTION_SEPARATOR,this));
        parameters.add(new BooleanP(ENABLE_MULTITHREADING, this, true));

    }

    @Override
    public ParameterCollection updateAndGetParameters() {
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        ParameterCollection returnedParameters = new ParameterCollection();

        returnedParameters.add(parameters.getParameter(INPUT_SEPARATOR));
        returnedParameters.add(parameters.getParameter(INPUT_IMAGE));
        returnedParameters.add(parameters.getParameter(INPUT_OBJECTS));

        returnedParameters.add(parameters.getParameter(OUTPUT_SEPARATOR));
        returnedParameters.add(parameters.getParameter(APPLY_TO_INPUT));
        if (!(boolean) parameters.getValue(APPLY_TO_INPUT)) {
            returnedParameters.add(parameters.getParameter(ADD_OUTPUT_TO_WORKSPACE));

            if (parameters.getValue(ADD_OUTPUT_TO_WORKSPACE)) {
                returnedParameters.add(parameters.getParameter(OUTPUT_IMAGE));

            }
        }

        returnedParameters.add(parameters.getParameter(RENDERING_SEPARATOR));
        returnedParameters.addAll(super.updateAndGetParameters(inputObjectsName));
        returnedParameters.add(parameters.getParameter(POINT_SIZE));
        returnedParameters.add(parameters.getParameter(POINT_TYPE));
        returnedParameters.add(parameters.getParameter(RENDER_IN_ALL_FRAMES));

        returnedParameters.add(parameters.getParameter(EXECUTION_SEPARATOR));
        returnedParameters.add(parameters.getParameter(ENABLE_MULTITHREADING));

        return returnedParameters;

    }

    @Override
    public ImageMeasurementRefCollection updateAndGetImageMeasurementRefs() {
        return null;
    }

    @Override
    public ObjMeasurementRefCollection updateAndGetObjectMeasurementRefs() {
        return null;
    }

    @Override
    public MetadataRefCollection updateAndGetMetadataReferences() {
        return null;
    }

    @Override
    public RelationshipRefCollection updateAndGetRelationships() {
        return null;
    }

    @Override
    public boolean verify() {
        return true;
    }
}
