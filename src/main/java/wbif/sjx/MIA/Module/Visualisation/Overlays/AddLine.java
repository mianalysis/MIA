// package wbif.sjx.MIA.Module.Visualisation.Overlays;

// import ij.ImagePlus;
// import ij.Prefs;
// import ij.gui.OvalRoi;
// import ij.gui.PointRoi;
// import ij.plugin.Duplicator;
// import ij.plugin.HyperStackConverter;
// import wbif.sjx.MIA.Module.ModuleCollection;
// import wbif.sjx.MIA.Module.PackageNames;
// import wbif.sjx.MIA.Object.*;
// import wbif.sjx.MIA.Object.Image;
// import wbif.sjx.MIA.Object.Parameters.*;
// import wbif.sjx.MIA.Object.References.ImageMeasurementRefCollection;
// import wbif.sjx.MIA.Object.References.ObjMeasurementRefCollection;
// import wbif.sjx.MIA.Object.References.MetadataRefCollection;
// import wbif.sjx.MIA.Object.References.RelationshipRefCollection;
// import wbif.sjx.MIA.Process.ColourFactory;

// import java.awt.*;
// import java.util.HashMap;
// import java.util.concurrent.LinkedBlockingQueue;
// import java.util.concurrent.ThreadPoolExecutor;
// import java.util.concurrent.TimeUnit;
// import java.util.concurrent.atomic.AtomicInteger;

// public class AddLine extends Overlay {
//     public static final String INPUT_SEPARATOR = "Image and object input";
//     public static final String INPUT_IMAGE = "Input image";
//     public static final String INPUT_OBJECTS = "Input objects";

//     public static final String OUTPUT_SEPARATOR = "Image output";
//     public static final String APPLY_TO_INPUT = "Apply to input image";
//     public static final String ADD_OUTPUT_TO_WORKSPACE = "Add output image to workspace";
//     public static final String OUTPUT_IMAGE = "Output image";

//     public static final String POSITION_SEPARATOR = "Overlay position";
//     public static final String REFERENCE_MODE_1 = "Reference mode 1";
//     public static final String X_POSITION_MEASUREMENT_1 = "X-position measurement 1 (px)";
//     public static final String Y_POSITION_MEASUREMENT_1 = "Y-position measurement 1 (px)";
//     public static final String X_FIXED_1 = "Fixed X-position 1 (px)";
//     public static final String Y_FIXED_1 = "Fixed Y-position 1 (px)";
//     public static final String REFERENCE_MODE_2 = "Reference mode 2";
//     public static final String X_POSITION_MEASUREMENT_2 = "X-position measurement 2 (px)";
//     public static final String Y_POSITION_MEASUREMENT_2 = "Y-position measurement 2 (px)";
//     public static final String X_FIXED_2 = "Fixed X-position 2 (px)";
//     public static final String Y_FIXED_2 = "Fixed Y-position 2 (px)";

//     public static final String RENDERING_SEPARATOR = "Overlay rendering";
//     public static final String LINE_WIDTH = "Line width";
//     public static final String POINT_SIZE = "Point size";
//     public static final String POINT_TYPE = "Point type";
//     public static final String RENDER_IN_ALL_FRAMES = "Render in all frames";

//     public static final String EXECUTION_SEPARATOR = "Execution controls";
//     public static final String ENABLE_MULTITHREADING = "Enable multithreading";

//     public interface ReferenceModes {
//         String CENTROID = "Object centroid";
//         String FIXED_POSITION = "Fixed position";
//         String MEASUREMENT = "From measurement";
        
//         String[] ALL = new String[]{CENTROID,FIXED_POSITION,MEASUREMENT};
        
//     }

//     public AddLine(ModuleCollection modules) {
//         super("Add line", modules);
//     }


//     public static void addOverlay(Obj object, ImagePlus ipl, Color colour, String size, String type, double lineWidth, String[] posMeasurements, boolean renderInAllFrames) {
//         if (ipl.getOverlay() == null) ipl.setOverlay(new ij.gui.Overlay());

//         double xMean = object.getMeasurement(posMeasurements[0]).getValue();
//         double yMean = object.getMeasurement(posMeasurements[1]).getValue();
//         double zMean = object.getMeasurement(posMeasurements[2]).getValue();

//         // Getting coordinates and settings for plotting
//         int z = (int) Math.round(zMean+1);
//         int t = object.getT()+1;
//         int sizeVal = AddObjectCentroid.getSize(size);
//         int typeVal = AddObjectCentroid.getType(type);

//         if (renderInAllFrames) t = 0;
//         if (posMeasurements[3] == null) {
//             PointRoi pointRoi = new PointRoi(xMean+0.5,yMean+0.5);
//             pointRoi.setPointType(typeVal);
//             pointRoi.setSize(sizeVal);
//             if (ipl.isHyperStack()) {
//                 pointRoi.setPosition(1, z, t);
//             } else {
//                 int pos = Math.max(Math.max(1,z),t);
//                 pointRoi.setPosition(pos);
//             }
//             pointRoi.setStrokeColor(colour);
//             ipl.getOverlay().addElement(pointRoi);

//         } else {
//             double r = object.getMeasurement(posMeasurements[3]).getValue();
//             OvalRoi ovalRoi = new OvalRoi(xMean + 0.5 - r, yMean + 0.5 - r, 2 * r, 2 * r);
//             if (ipl.isHyperStack()) {
//                 ovalRoi.setPosition(1, z, t);
//             } else {
//                 int pos = Math.max(Math.max(1, z), t);
//                 ovalRoi.setPosition(pos);
//             }
//             ovalRoi.setStrokeColor(colour);
//             ovalRoi.setStrokeWidth(lineWidth);
//             ipl.getOverlay().addElement(ovalRoi);
//         }
//     }


//     @Override
//     public String getPackageName() {
//         return PackageNames.VISUALISATION_OVERLAYS;
//     }

//     @Override
//     public String getDescription() {
//         return "";
//     }

//     @Override
//     protected boolean process(Workspace workspace) {
//         // Getting parameters
//         boolean applyToInput = parameters.getValue(APPLY_TO_INPUT);
//         boolean addOutputToWorkspace = parameters.getValue(ADD_OUTPUT_TO_WORKSPACE);
//         String outputImageName = parameters.getValue(OUTPUT_IMAGE);

//         // Getting input objects
//         String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
//         ObjCollection inputObjects = workspace.getObjects().get(inputObjectsName);

//         // Getting input image
//         String inputImageName = parameters.getValue(INPUT_IMAGE);
//         Image inputImage = workspace.getImages().get(inputImageName);
//         ImagePlus ipl = inputImage.getImagePlus();

//         String xPosMeas = parameters.getValue(X_POSITION_MEASUREMENT);
//         String yPosMeas = parameters.getValue(Y_POSITION_MEASUREMENT);
//         String zPosMeas = parameters.getValue(Z_POSITION_MEASUREMENT);
//         boolean useRadius = parameters.getValue(USE_RADIUS);
//         String measurementForRadius = parameters.getValue(MEASUREMENT_FOR_RADIUS);

//         double opacity = parameters.getValue(OPACITY);        
//         String pointSize = parameters.getValue(POINT_SIZE);
//         String pointType = parameters.getValue(POINT_TYPE);
//         double lineWidth = parameters.getValue(LINE_WIDTH);
//         boolean renderInAllFrames = parameters.getValue(RENDER_IN_ALL_FRAMES);
//         boolean multithread = parameters.getValue(ENABLE_MULTITHREADING);

//         // Only add output to workspace if not applying to input
//         if (applyToInput) addOutputToWorkspace = false;

//         // Duplicating the image, so the original isn't altered
//         if (!applyToInput) ipl = new Duplicator().run(ipl);

//         // Setting position measurements
//         if (!useRadius) measurementForRadius = null;
//         String[] posMeasurements = new String[]{xPosMeas, yPosMeas, zPosMeas, measurementForRadius};

//         // Generating colours for each object
//         HashMap<Integer,Float> hues = getHues(inputObjects);

//         // If necessary, turning the image into a HyperStack (if 2 dimensions=1 it will be a standard ImagePlus)
//         if (!ipl.isComposite() & (ipl.getNSlices() > 1 | ipl.getNFrames() > 1 | ipl.getNChannels() > 1)) {
//             ipl = HyperStackConverter.toHyperStack(ipl, ipl.getNChannels(), ipl.getNSlices(), ipl.getNFrames());
//         }

//         // Adding the overlay element
//             int nThreads = multithread ? Prefs.getThreads() : 1;
//             ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads,nThreads,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());

//             // Running through each object, adding it to the overlay along with an ID label
//             AtomicInteger count = new AtomicInteger();
//             for (Obj object:inputObjects.values()) {
//                 ImagePlus finalIpl = ipl;

//                 Runnable task = () -> {
//                     float hue = hues.get(object.getID());
//                     Color colour = ColourFactory.getColour(hue,opacity);
                
//                     addOverlay(object, finalIpl, colour, pointSize, pointType, lineWidth, posMeasurements, renderInAllFrames);
        
//                     writeMessage("Rendered " + (count.incrementAndGet()) + " objects of " + inputObjects.size());
//                 };
//                 pool.submit(task);
//             }

//             pool.shutdown();
//         try {
//             pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS); // i.e. never terminate early
//         } catch (InterruptedException e) {
//             return false;
//         }

//         Image outputImage = new Image(outputImageName,ipl);

//         // If necessary, adding output image to workspace.  This also allows us to show it.
//         if (addOutputToWorkspace) workspace.addImage(outputImage);
//         if (showOutput) outputImage.showImage();

//         return true;

//     }

//     @Override
//     protected void initialiseParameters() {
//         super.initialiseParameters();

//         parameters.add(new ParamSeparatorP(INPUT_SEPARATOR,this));
//         parameters.add(new InputImageP(INPUT_IMAGE, this));
//         parameters.add(new InputObjectsP(INPUT_OBJECTS, this));

//         parameters.add(new ParamSeparatorP(OUTPUT_SEPARATOR,this));
//         parameters.add(new BooleanP(APPLY_TO_INPUT, this,false));
//         parameters.add(new BooleanP(ADD_OUTPUT_TO_WORKSPACE, this,false));
//         parameters.add(new OutputImageP(OUTPUT_IMAGE, this));

//         parameters.add(new ParamSeparatorP(POSITION_SEPARATOR,this));
//         parameters.add(new ObjectMeasurementP(X_POSITION_MEASUREMENT_1, this));
//         parameters.add(new ObjectMeasurementP(Y_POSITION_MEASUREMENT_1, this));
//         parameters.add(new ObjectMeasurementP(Z_POSITION_MEASUREMENT_1, this));
//         parameters.add(new BooleanP(USE_RADIUS, this,true));
//         parameters.add(new ObjectMeasurementP(MEASUREMENT_FOR_RADIUS, this));

//         parameters.add(new ParamSeparatorP(RENDERING_SEPARATOR,this));
//         parameters.add(new DoubleP(LINE_WIDTH,this,1));
//         parameters.add(new ChoiceP(POINT_SIZE,this,PointSizes.SMALL,PointSizes.ALL));
//         parameters.add(new ChoiceP(POINT_TYPE,this,PointTypes.CIRCLE,PointTypes.ALL));
//         parameters.add(new BooleanP(RENDER_IN_ALL_FRAMES,this,false));

//         parameters.add(new ParamSeparatorP(EXECUTION_SEPARATOR,this));
//         parameters.add(new BooleanP(ENABLE_MULTITHREADING, this, true));

//     }

//     @Override
//     public ParameterCollection updateAndGetParameters() {
//         String inputObjectsName = parameters.getValue(INPUT_OBJECTS);

//         ParameterCollection returnedParameters = new ParameterCollection();

//         returnedParameters.add(parameters.getParameter(INPUT_SEPARATOR));
//         returnedParameters.add(parameters.getParameter(INPUT_IMAGE));
//         returnedParameters.add(parameters.getParameter(INPUT_OBJECTS));

//         returnedParameters.add(parameters.getParameter(OUTPUT_SEPARATOR));
//         returnedParameters.add(parameters.getParameter(APPLY_TO_INPUT));
//         if (!(boolean) parameters.getValue(APPLY_TO_INPUT)) {
//             returnedParameters.add(parameters.getParameter(ADD_OUTPUT_TO_WORKSPACE));

//             if ((boolean) parameters.getValue(ADD_OUTPUT_TO_WORKSPACE)) {
//                 returnedParameters.add(parameters.getParameter(OUTPUT_IMAGE));

//             }
//         }

//         returnedParameters.add(parameters.getParameter(POSITION_SEPARATOR));
//         returnedParameters.add(parameters.getParameter(X_POSITION_MEASUREMENT));
//         returnedParameters.add(parameters.getParameter(Y_POSITION_MEASUREMENT));
//         returnedParameters.add(parameters.getParameter(Z_POSITION_MEASUREMENT));

//         ((ObjectMeasurementP) parameters.getParameter(X_POSITION_MEASUREMENT)).setObjectName(inputObjectsName);
//         ((ObjectMeasurementP) parameters.getParameter(Y_POSITION_MEASUREMENT)).setObjectName(inputObjectsName);
//         ((ObjectMeasurementP) parameters.getParameter(Z_POSITION_MEASUREMENT)).setObjectName(inputObjectsName);

//         returnedParameters.add(parameters.getParameter(USE_RADIUS));
//         if ((boolean) parameters.getValue(USE_RADIUS)) {
//             returnedParameters.add(parameters.getParameter(MEASUREMENT_FOR_RADIUS));
//             ((ObjectMeasurementP) parameters.getParameter(MEASUREMENT_FOR_RADIUS)).setObjectName(inputObjectsName);
//             returnedParameters.add(parameters.getParameter(RENDERING_SEPARATOR));
//             returnedParameters.add(parameters.getParameter(LINE_WIDTH));
//         } else {
//             returnedParameters.add(parameters.getParameter(RENDERING_SEPARATOR));
//             returnedParameters.add(parameters.getParameter(POINT_SIZE));
//             returnedParameters.add(parameters.getParameter(POINT_TYPE));
//         }

//         returnedParameters.addAll(super.updateAndGetParameters(inputObjectsName));
//         returnedParameters.add(parameters.getParameter(RENDER_IN_ALL_FRAMES));

//         returnedParameters.add(parameters.getParameter(EXECUTION_SEPARATOR));
//         returnedParameters.add(parameters.getParameter(ENABLE_MULTITHREADING));

//         return returnedParameters;

//     }

//     @Override
//     public ImageMeasurementRefCollection updateAndGetImageMeasurementRefs() {
//         return null;
//     }

//     @Override
//     public ObjMeasurementRefCollection updateAndGetObjectMeasurementRefs() {
//         return null;
//     }

//     @Override
//     public MetadataRefCollection updateAndGetMetadataReferences() {
//         return null;
//     }

//     @Override
//     public RelationshipRefCollection updateAndGetRelationships() {
//         return null;
//     }

//     @Override
//     public boolean verify() {
//         return true;
//     }
// }
