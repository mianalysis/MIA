package io.github.mianalysis.mia.module.objects.detect;

import java.io.File;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import ch.epfl.biop.wrappers.cellpose.Cellpose;
import ch.epfl.biop.wrappers.cellpose.ij2commands.CellposeWrapper;
import ij.ImagePlus;
import ij.Prefs;
import io.github.mianalysis.mia.module.Categories;
import io.github.mianalysis.mia.module.Category;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.object.Objs;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.coordinates.volume.VolumeType;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.image.ImageFactory;
import io.github.mianalysis.mia.object.parameters.BooleanP;
import io.github.mianalysis.mia.object.parameters.ChoiceP;
import io.github.mianalysis.mia.object.parameters.FolderPathP;
import io.github.mianalysis.mia.object.parameters.InputImageP;
import io.github.mianalysis.mia.object.parameters.Parameters;
import io.github.mianalysis.mia.object.parameters.SeparatorP;
import io.github.mianalysis.mia.object.parameters.objects.OutputObjectsP;
import io.github.mianalysis.mia.object.parameters.text.DoubleP;
import io.github.mianalysis.mia.object.parameters.text.IntegerP;
import io.github.mianalysis.mia.object.refs.collections.ImageMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.MetadataRefs;
import io.github.mianalysis.mia.object.refs.collections.ObjMeasurementRefs;
import io.github.mianalysis.mia.object.refs.collections.ParentChildRefs;
import io.github.mianalysis.mia.object.refs.collections.PartnerRefs;
import io.github.mianalysis.mia.object.system.Status;

@Plugin(type = Module.class, priority = Priority.LOW, visible = true)
public class CellposeDetection extends Module {

    public static final String INPUT_SEPARATOR = "Image input/object output";

    public static final String INPUT_IMAGE = "Input image";

    public static final String OUTPUT_OBJECTS = "Output objects";

    public static final String CELLPOSE_SEPARATOR = "Cellpose settings";

    public static final String CELLPOSE_PATH = "Cellpose environment path";

    public static final String ENVIRONMENT_TYPE = "Environment type";

    public static final String CELLPOSE_VERSION = "Cellpose version";

    public static final String USE_MXNET = "Use MXNet";

    public static final String USE_RESAMPLE = "Use resample";

    public static final String USE_GPU = "Use GPU";

    public static final String USE_FASTMODE = "Use fast mode";

    public static final String SEGMENTATION_SEPARATOR = "Segmentation settings";

    public static final String DIAMETER = "Diameter";

    public static final String CELL_PROBABILITY_THRESHOLD = "Cell probability threshold";

    public static final String FLOW_THRESHOLD = "Flow threshold";

    public static final String ANISOTROPY = "Anisotropy";

    public static final String DIAMETER_THRESHOLD = "Diameter threshold";


    public interface EnvironmentTypes {
        String CONDA = "Conda";
        String VENV = "Venv";

        String[] ALL = new String[] { CONDA, VENV };

    }

    public interface CellposeVersions {
        String V0P6 = "0.6";
        String V0P7 = "0.7";
        String V1P0 = "1.0";
        String V2P0 = "2.0";

        String[] ALL = new String[] { V0P6, V0P7, V1P0, V2P0 };

    }

    public CellposeDetection(Modules modules) {
        super("Cellpose detection", modules);
    }

    @Override
    public Category getCategory() {
        return Categories.OBJECTS_DETECT;
    }

    @Override
    public String getVersionNumber() {
        return "1.0.0";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public Status process(Workspace workspace) {
        // Getting input image
        String inputImageName = parameters.getValue(INPUT_IMAGE, workspace);
        String outputObjectsName = parameters.getValue(OUTPUT_OBJECTS, workspace);

        String cellposePath = parameters.getValue(CELLPOSE_PATH, workspace);
        String environmentType = parameters.getValue(ENVIRONMENT_TYPE, workspace).toString().toLowerCase();
        String version = parameters.getValue(CELLPOSE_VERSION, workspace);
        boolean useMXNet = parameters.getValue(USE_MXNET, workspace);
        boolean useFastMode = parameters.getValue(USE_FASTMODE, workspace);
        boolean useGPU = parameters.getValue(USE_GPU, workspace);
        boolean useResample = parameters.getValue(USE_RESAMPLE, workspace);

        int diameter = parameters.getValue(DIAMETER, workspace);
        double cellProbThresh = parameters.getValue(CELL_PROBABILITY_THRESHOLD, workspace);
        double flowThreshold = parameters.getValue(FLOW_THRESHOLD, workspace);
        double anisotropy = parameters.getValue(ANISOTROPY, workspace);
        double diameterThreshold = parameters.getValue(DIAMETER_THRESHOLD, workspace);

        Image inputImage = workspace.getImages().get(inputImageName);
        ImagePlus ipl = inputImage.getImagePlus();

        Cellpose.setEnvDirPath(new File(cellposePath));
        Cellpose.setEnvType(environmentType);
        Cellpose.setVersion(version);
        switch ((String) parameters.getValue(CELLPOSE_VERSION, workspace)) {
            case CellposeVersions.V0P6:
            case CellposeVersions.V0P7:
                Cellpose.setUseMxnet(useMXNet);
                Cellpose.setUseFastMode(useFastMode);
                break;
            case CellposeVersions.V1P0:
                Cellpose.setUseMxnet(useMXNet);
                break;
        }
        Cellpose.setUseGpu(useGPU);
        Cellpose.setUseResample(useResample);

        CellposeWrapper cellpose = new CellposeWrapper();
        cellpose.setImagePlus(ipl);
        cellpose.setDiameter(diameter);
        cellpose.setCellProbabilityThreshold(cellProbThresh);
        cellpose.setFlowThreshold(flowThreshold);
        cellpose.setAnisotropy(anisotropy);
        cellpose.setDiameterThreshold(diameterThreshold);

        // cellpose.model_path = new File("cellpose");
        // cellpose.model = "cyto";
        // cellpose.nuclei_channel = -1;
        // cellpose.cyto_channel = 1;
        // cellpose.dimensionMode = "2D";
        // cellpose.stitch_threshold = 0;
        // cellpose.omni = false;
        // cellpose.cluster = false;
        // cellpose.additional_flags = "";

        cellpose.run();

        Image cellsImage = ImageFactory.createImage("Objects", cellpose.getLabels());
        Objs outputObjects = cellsImage.convertImageToObjects(VolumeType.QUADTREE, outputObjectsName);

        workspace.addObjects(outputObjects);

        if (showOutput)
            outputObjects.convertToImageRandomColours().show();

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        // Getting defaults
        String keyPrefix = Cellpose.class.getName() + ".";

        String defaultEnv = EnvironmentTypes.CONDA;
        switch (Prefs.get(keyPrefix + "envType", "conda")) {
            case "conda":
                defaultEnv = EnvironmentTypes.CONDA;
                break;
            case "venv":
                defaultEnv = EnvironmentTypes.VENV;
                break;
        }

        String defaultVersion = CellposeVersions.V2P0;
        switch (Prefs.get(keyPrefix + "version", "2.0")) {
            case "0.6":
                defaultVersion = CellposeVersions.V0P6;
                break;
            case "0.7":
                defaultVersion = CellposeVersions.V0P7;
                break;
            case "1.0":
                defaultVersion = CellposeVersions.V1P0;
                break;
            case "2.0":
                defaultVersion = CellposeVersions.V2P0;
                break;
        }

        parameters.add(new SeparatorP(INPUT_SEPARATOR, this));
        parameters.add(new InputImageP(INPUT_IMAGE, this));
        parameters.add(new OutputObjectsP(OUTPUT_OBJECTS, this));

        parameters.add(new SeparatorP(CELLPOSE_SEPARATOR, this));
        parameters.add(new FolderPathP(CELLPOSE_PATH, this, Prefs.get(keyPrefix + "envDirPath", "")));
        parameters.add(new ChoiceP(ENVIRONMENT_TYPE, this, defaultEnv, EnvironmentTypes.ALL));
        parameters.add(new ChoiceP(CELLPOSE_VERSION, this, defaultVersion, CellposeVersions.ALL));
        parameters.add(new BooleanP(USE_MXNET, this, Prefs.get(keyPrefix + "useMxnet", false)));
        parameters.add(new BooleanP(USE_RESAMPLE, this, Prefs.get(keyPrefix + "useResample", false)));
        parameters.add(new BooleanP(USE_GPU, this, Prefs.get(keyPrefix + "useGpu", false)));
        parameters.add(new BooleanP(USE_FASTMODE, this, Prefs.get(keyPrefix + "useFastMode", false)));

        parameters.add(new SeparatorP(SEGMENTATION_SEPARATOR, this));
        parameters.add(new IntegerP(DIAMETER, this, 30));
        parameters.add(new DoubleP(CELL_PROBABILITY_THRESHOLD, this, 0.0));
        parameters.add(new DoubleP(FLOW_THRESHOLD, this, 0.4));
        parameters.add(new DoubleP(ANISOTROPY, this, 1.0));
        parameters.add(new DoubleP(DIAMETER_THRESHOLD, this, 12));

    }

    @Override
    public Parameters updateAndGetParameters() {
        Workspace workspace = null;
        Parameters returnedParameters = new Parameters();

        returnedParameters.add(parameters.getParameter(INPUT_SEPARATOR));
        returnedParameters.add(parameters.getParameter(INPUT_IMAGE));
        returnedParameters.add(parameters.getParameter(OUTPUT_OBJECTS));

        returnedParameters.add(parameters.getParameter(CELLPOSE_SEPARATOR));
        returnedParameters.add(parameters.getParameter(CELLPOSE_PATH));
        returnedParameters.add(parameters.getParameter(ENVIRONMENT_TYPE));
        returnedParameters.add(parameters.getParameter(CELLPOSE_VERSION));
        switch ((String) parameters.getValue(CELLPOSE_VERSION, workspace)) {
            case CellposeVersions.V0P6:
            case CellposeVersions.V0P7:
                returnedParameters.add(parameters.getParameter(USE_MXNET));
                returnedParameters.add(parameters.getParameter(USE_RESAMPLE));
                break;
            case CellposeVersions.V1P0:
                returnedParameters.add(parameters.getParameter(USE_MXNET));
                break;
        }
        returnedParameters.add(parameters.getParameter(USE_GPU));
        returnedParameters.add(parameters.getParameter(USE_FASTMODE));

        returnedParameters.add(parameters.getParameter(SEGMENTATION_SEPARATOR));
        switch ((String) parameters.getValue(CELLPOSE_VERSION, workspace)) {
            case CellposeVersions.V0P6:
                returnedParameters.add(parameters.getParameter(DIAMETER));
                returnedParameters.add(parameters.getParameter(CELL_PROBABILITY_THRESHOLD));
                returnedParameters.add(parameters.getParameter(FLOW_THRESHOLD));
                break;
            case CellposeVersions.V0P7:
                returnedParameters.add(parameters.getParameter(DIAMETER));
                returnedParameters.add(parameters.getParameter(CELL_PROBABILITY_THRESHOLD));
                returnedParameters.add(parameters.getParameter(FLOW_THRESHOLD));
                returnedParameters.add(parameters.getParameter(ANISOTROPY));
                returnedParameters.add(parameters.getParameter(DIAMETER_THRESHOLD));
                break;
            case CellposeVersions.V1P0:
                returnedParameters.add(parameters.getParameter(DIAMETER));
                returnedParameters.add(parameters.getParameter(CELL_PROBABILITY_THRESHOLD));
                returnedParameters.add(parameters.getParameter(FLOW_THRESHOLD));
                returnedParameters.add(parameters.getParameter(DIAMETER_THRESHOLD));
                break;
            case CellposeVersions.V2P0:
                returnedParameters.add(parameters.getParameter(DIAMETER));
                returnedParameters.add(parameters.getParameter(CELL_PROBABILITY_THRESHOLD));
                returnedParameters.add(parameters.getParameter(FLOW_THRESHOLD));
                returnedParameters.add(parameters.getParameter(ANISOTROPY));
                break;
        }

        // Updating default parameters
        String keyPrefix = Cellpose.class.getName() + ".";

        Prefs.set(keyPrefix + "envDirPath", parameters.getValue(CELLPOSE_PATH, workspace));

        String defaultEnv = "conda";
        switch ((String) parameters.getValue(ENVIRONMENT_TYPE, workspace)) {
            case EnvironmentTypes.CONDA:
                defaultEnv = "conda";
                break;
            case EnvironmentTypes.VENV:
                defaultEnv = "venv";
                break;
        }
        Prefs.set(keyPrefix + "envType", defaultEnv);

        String defaultVersion = "2.0";
        switch ((String) parameters.getValue(CELLPOSE_VERSION, workspace)) {
            case CellposeVersions.V0P6:
                defaultVersion = "0.6";
                break;
            case CellposeVersions.V0P7:
                defaultVersion = "0.7";
                break;
            case CellposeVersions.V1P0:
                defaultVersion = "1.0";
                break;
            case CellposeVersions.V2P0:
                defaultVersion = "2.0";
                break;
        }
        Prefs.set(keyPrefix + "version", defaultVersion);

        Prefs.set(keyPrefix + "useMxnet", parameters.getValue(USE_MXNET, workspace).toString());
        Prefs.set(keyPrefix + "useResample", parameters.getValue(USE_RESAMPLE, workspace).toString());
        Prefs.set(keyPrefix + "useGpu", parameters.getValue(USE_GPU, workspace).toString());
        Prefs.set(keyPrefix + "useFastMode", parameters.getValue(USE_FASTMODE, workspace).toString());

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
        return true;
    }
}