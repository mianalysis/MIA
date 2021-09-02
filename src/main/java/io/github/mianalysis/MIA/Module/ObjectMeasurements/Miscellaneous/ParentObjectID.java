// TODO: Could addRef an optional parameter to select the channel of the input image to use for measurement

package io.github.mianalysis.MIA.Module.ObjectMeasurements.Miscellaneous;

import io.github.mianalysis.MIA.Module.Module;
import io.github.mianalysis.MIA.Module.Modules;
import io.github.mianalysis.MIA.Module.Category;
import io.github.mianalysis.MIA.Module.Categories;
import io.github.mianalysis.MIA.Object.Obj;
import io.github.mianalysis.MIA.Object.Objs;
import io.github.mianalysis.MIA.Object.Status;
import io.github.mianalysis.MIA.Object.Workspace;
import io.github.mianalysis.MIA.Object.Measurements.ParentIDMeasurement;
import io.github.mianalysis.MIA.Object.Parameters.InputObjectsP;
import io.github.mianalysis.MIA.Object.Parameters.SeparatorP;
import io.github.mianalysis.MIA.Object.Refs.ObjMeasurementRef;
import io.github.mianalysis.MIA.Object.Refs.Collections.ImageMeasurementRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.MetadataRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.ObjMeasurementRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.ParentChildRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.PartnerRefs;
import io.github.mianalysis.MIA.Object.Parameters.Parameters;
import io.github.mianalysis.MIA.Object.Parameters.ParentObjectsP;

/**
 * Created by sc13967 on 05/05/2017.
 */
public class ParentObjectID extends Module {
    public static final String INPUT_SEPARATOR = "Object and image input";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String PARENT_OBJECT = "Parent object";

    public ParentObjectID(Modules modules) {
        super("Parent object ID", modules);
    }

    public static String getFullName(String parentObjectsName) {
        return "PARENT_ID // "+ parentObjectsName;
    }


    @Override
    public Category getCategory() {
        return Categories.OBJECT_MEASUREMENTS_MISCELLANEOUS;
    }

    @Override
    public String getDescription() {
        return "Stores the ID number of an associated parent from a specific class.  Associated IDs are stored as measurements and are assigned to all objects in the input collection.  Unlike normal measurements, this value is evaluated at the time of use, so should always be up to date.";
    }

    @Override
    public Status process(Workspace workspace) {
        // Getting input objects
        String objectName = parameters.getValue(INPUT_OBJECTS);
        String parentObjectsName = parameters.getValue(PARENT_OBJECT);

        Objs objects = workspace.getObjects().get(objectName);
        String measurementName = getFullName(parentObjectsName);

        if (objects == null)
            return Status.PASS;
            
        for (Obj obj : objects.values())
            obj.addMeasurement(new ParentIDMeasurement(measurementName, obj, parentObjectsName));
        
        if (showOutput) objects.showMeasurements(this,modules);

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        parameters.add(new SeparatorP(INPUT_SEPARATOR, this));
        parameters.add(new InputObjectsP(INPUT_OBJECTS, this));
        parameters.add(new ParentObjectsP(PARENT_OBJECT, this));

        addParameterDescriptions();

    }

    @Override
    public Parameters updateAndGetParameters() {
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        ((ParentObjectsP) parameters.get(PARENT_OBJECT)).setChildObjectsName(inputObjectsName);

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
        String parentObjectsName = parameters.getValue(PARENT_OBJECT);
        
        String measurementName = getFullName(parentObjectsName);

        // We don't want statistics for this measurement
        ObjMeasurementRef ref = objectMeasurementRefs.getOrPut(measurementName);
        ref.setObjectsName(inputObjectsName);
        ref.setDescription("ID number of associated \""+parentObjectsName+"\" parent object.");
        ref.setExportMax(false);
        ref.setExportMean(false);
        ref.setExportMin(false);
        ref.setExportStd(false);
        ref.setExportSum(false);

        returnedRefs.add(ref);

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
        return true;
    }

    void addParameterDescriptions() {
        parameters.get(INPUT_OBJECTS).setDescription(
                "For each object in this collection the ID number of an associated parent object (from the collection specified by \""
                        + PARENT_OBJECT
                        + "\") will be stored as a measurement.  This measurement will be associated with each input object.  The measurement is evaluated at the time of access (unlike \"normal\" measurements which have fixed values), so should always be correct.");

        parameters.get(PARENT_OBJECT).setDescription("Associated parent object collection.");

    }
}
