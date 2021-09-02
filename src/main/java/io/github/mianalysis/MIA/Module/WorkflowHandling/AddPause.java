package io.github.mianalysis.MIA.Module.WorkflowHandling;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import ij.ImagePlus;
import ij.plugin.Duplicator;
import io.github.mianalysis.MIA.Module.Module;
import io.github.mianalysis.MIA.Module.Modules;
import io.github.mianalysis.MIA.Module.Category;
import io.github.mianalysis.MIA.Module.Categories;
import io.github.mianalysis.MIA.Object.Image;
import io.github.mianalysis.MIA.Object.Status;
import io.github.mianalysis.MIA.Object.Workspace;
import io.github.mianalysis.MIA.Object.Parameters.BooleanP;
import io.github.mianalysis.MIA.Object.Parameters.InputImageP;
import io.github.mianalysis.MIA.Object.Parameters.Parameters;
import io.github.mianalysis.MIA.Object.Parameters.SeparatorP;
import io.github.mianalysis.MIA.Object.Refs.Collections.ImageMeasurementRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.MetadataRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.ObjMeasurementRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.ParentChildRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.PartnerRefs;

/**
 * Created by sc13967 on 09/02/2018.
 */
public class AddPause extends Module {
    public static final String PAUSE_SEPARATOR = "Pause controls";
    public static final String SHOW_IMAGE = "Show image";
    public static final String INPUT_IMAGE = "Input image";

    private static final String RESUME = "Resume";
    private static final String TERMINATE = "Terminate";

    public AddPause(Modules modules) {
        super("Add pause",modules);
    }



    @Override
    public Category getCategory() {
        return Categories.WORKFLOW_HANDLING;
    }

    @Override
    public String getDescription() {
        return "Pauses workflow execution and displays an option dialog to continue or quit.  Optionally, an image from the workspace can also be displayed.  An example usage would be during parameter optimisation, where subsequent elements of the analysis only want executing if the first steps are deemed successful (and this can't be automatically determined).";
    }

    @Override
    protected Status process(Workspace workspace) {
        // Getting parameters
        boolean showImage = parameters.getValue(SHOW_IMAGE);
        String inputImageName = parameters.getValue(INPUT_IMAGE);

        if (showImage) {
            Image inputImage = workspace.getImage(inputImageName);
            ImagePlus showIpl = new Duplicator().run(inputImage.getImagePlus());
            showIpl.setTitle(inputImageName);
            showIpl.show();
        }

        String[] options = {RESUME,TERMINATE};
        JOptionPane optionPane = new JOptionPane("Execution paused.  What would you like to do?",JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION,null,options);
        JDialog dialog = optionPane.createDialog(null, "Execution paused");
        dialog.setModal(false);
        dialog.setVisible(true);

        writeStatus("Execution paused");

        while (optionPane.getValue() == JOptionPane.UNINITIALIZED_VALUE) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        switch ((String) optionPane.getValue()) {
            case RESUME:
                return Status.PASS;

            case TERMINATE:
                return Status.TERMINATE;

        }

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        parameters.add(new SeparatorP(PAUSE_SEPARATOR, this));
        parameters.add(new BooleanP(SHOW_IMAGE,this,true));
        parameters.add(new InputImageP(INPUT_IMAGE,this));

        addParameterDescriptions();

    }

    @Override
    public Parameters updateAndGetParameters() {
        Parameters returnedParameters = new Parameters();

        returnedParameters.add(parameters.getParameter(SHOW_IMAGE));
        if ((boolean) parameters.getValue(SHOW_IMAGE)) {
            returnedParameters.add(parameters.getParameter(INPUT_IMAGE));
        }

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

    protected void addParameterDescriptions() {
        parameters.get(SHOW_IMAGE).setDescription("When selected, an image from the workspace can be automatically displayed when this module executes.");

        parameters.get(INPUT_IMAGE).setDescription("If \""+SHOW_IMAGE+"\" is selected, this image will be displayed when the module executes.");

    }
}
