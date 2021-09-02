package io.github.mianalysis.MIA.Macro.ObjectMeasurements;

import ij.macro.MacroExtension;
import io.github.mianalysis.MIA.Macro.MacroOperation;
import io.github.mianalysis.MIA.Module.Modules;
import io.github.mianalysis.MIA.Object.*;

public class MIA_GetObjectTimepoint extends MacroOperation {
    public MIA_GetObjectTimepoint(MacroExtension theHandler) {
        super(theHandler);
    }

    @Override
    public int[] getArgumentTypes() {
        return new int[]{ARG_STRING,ARG_NUMBER};
    }

    @Override
    public String action(Object[] objects, Workspace workspace, Modules modules) {
        String objectName = (String) objects[0];
        int objectID = (int) Math.round((Double) objects[1]);

        // Getting the object set
        Objs objCollection = workspace.getObjectSet(objectName);
        if (objCollection == null) return "";

        // Getting the object
        if (!objCollection.keySet().contains(objectID)) return "";
        Obj obj = objCollection.get(objectID);

        // Getting the timepoint
        return String.valueOf(obj.getT());

    }

    @Override
    public String getArgumentsDescription() {
        return "String objectName, Integer objectID";
    }

    @Override
    public String getDescription() {
        return "Returns the timepoint this object is present in.  Note: Numbering starts at 0.";
    }
}
