package io.github.mianalysis.mia.object.parameters.text;

import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.system.GlobalVariables;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.parameters.abstrakt.NumberType;
import io.github.mianalysis.mia.object.parameters.abstrakt.Parameter;

public class IntegerP extends NumberType {
    protected String value;

    public IntegerP(String name, Module module, int value) {
        super(name,module);
        this.value = String.valueOf(value);
    }

    public IntegerP(String name, Module module, String value) {
        super(name,module);
        this.value = value;
    }

    public IntegerP(String name, Module module, int value, String description) {
        super(name,module,description);
        this.value = String.valueOf(value);
    }

    public IntegerP(String name, Module module, String value, String description) {
        super(name,module,description);
        this.value = value;
    }

    public void setValue(int value) {
        this.value = String.valueOf(value);
    }

    public void setValue(String value) {
        // Checking this is valid
        if (GlobalVariables.containsValue(value) || containsReference(value)) {
            this.value = value;
        } else {
            try {
                Integer.parseInt(value);
                this.value = value;
            } catch (NumberFormatException e) {
                MIA.log.writeWarning("Module \"" + module.getName() + "\", parameter \"" + getName()
                        + " \". Must either:" + "\n    - Be an integer number"
                        + "\n    - Be a global variable handle (e.g. V{name}) "
                        + "\n    - Contain a calculation (e.g. C{3-6}."
                        + "\nNote: Global variables and calculations can be combined (e.g. C{V{name1} + V{name2} - 4})");
            }
        }
    }

    @Override
    public String getRawStringValue() {
        return value;
    }

    @Override
    public void setValueFromString(String value) {
        setValue(value);
    }

    @Override
    public <T> T getValue(Workspace workspace) {
        String converted = GlobalVariables.convertString(value, module.getModules());
        converted = insertWorkspaceValues(converted, workspace);
        converted = applyCalculation(converted);

        return (T) (Integer) Integer.parseInt(converted);

    }

    @Override
    public <T> void setValue(T value) {
        this.value = String.valueOf(value);
    }

    @Override
    public <T extends Parameter> T duplicate(Module newModule) {
        IntegerP newParameter = new IntegerP(name,newModule,value,getDescription());

        newParameter.setNickname(getNickname());
        newParameter.setVisible(isVisible());
        newParameter.setExported(isExported());

        return (T) newParameter;

    }
}
