package io.github.mianalysis.mia.object.parameters;

import io.github.mianalysis.mia.gui.parametercontrols.ModuleChoiceParameter;
import io.github.mianalysis.mia.gui.parametercontrols.ParameterControl;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.parameters.abstrakt.Parameter;

public class ModuleP extends Parameter {
    protected String selectedModuleID = "";
    protected boolean showNonRunnable = true;

    public ModuleP(String name, Module module, boolean showNonRunnable) {
        super(name, module);
        this.showNonRunnable = showNonRunnable;
    }

    public ModuleP(String name, Module module, boolean showNonRunnable, String selectedModuleID) {
        super(name, module);
        this.selectedModuleID = selectedModuleID;
        this.showNonRunnable = showNonRunnable;
    }

    public ModuleP(String name, Module module, boolean showNonRunnable, String selectedModuleID, String description) {
        super(name, module, description);
        this.selectedModuleID = selectedModuleID;
        this.showNonRunnable = showNonRunnable;
    }

    public Module getSelectedModule() {
        return module.getModules().getModuleByID(selectedModuleID);
    }

    public void setSelectedModule(Module selectedModule) {
        this.selectedModuleID = selectedModule.getModuleID();
    }

    public Module[] getModules() {
        Modules modules = module.getModules();
        int nAvailable = 0;
        for (Module module : modules) {
            if (module.isEnabled() && (module.isRunnable() || showNonRunnable))
                nAvailable++;
        }

        Module[] moduleArray = new Module[nAvailable];
        int count = 0;
        for (Module module : modules) {
            if (module.isEnabled() && (module.isRunnable() || showNonRunnable))
                moduleArray[count++] = module;
        }

        return moduleArray;

    }

    @Override
    public String getRawStringValue() {
        return selectedModuleID;

    }

    @Override
    public String getAlternativeString() {
        Module selectedModule = getSelectedModule();
        if (selectedModule == null)
            return "";

        return selectedModule.getNickname();

    }

    @Override
    public void setValueFromString(String string) {
        this.selectedModuleID = string;
    }

    @Override
    protected ParameterControl initialiseControl() {
        return new ModuleChoiceParameter(this);
    }

    public boolean getShowNonRunnable() {
        return showNonRunnable;
    }

    public void setShowNonRunnable(boolean showNonRunnable) {
        this.showNonRunnable = showNonRunnable;
    }

    @Override
    public <T> T getValue(Workspace workspace) {
        return (T) getSelectedModule();
    }

    @Override
    public <T> void setValue(T value) {
        selectedModuleID = ((Module) value).getModuleID();
    }

    @Override
    public boolean verify() {
        // Checking the selected module has been assigned and is present
        if (selectedModuleID == "")
            return false;

        // Checking if the selected module is in the module list. It doesn't need to be
        // enabled or runnable
        for (Module testModule : module.getModules()) {
            if (testModule.getModuleID().equals(selectedModuleID)) {
                return true;
            }
        }

        return false;

    }

    @Override
    public <T extends Parameter> T duplicate(Module newModule) {
        ModuleP newParameter = new ModuleP(name, newModule, showNonRunnable, selectedModuleID, getDescription());

        newParameter.setNickname(getNickname());
        newParameter.setVisible(isVisible());
        newParameter.setExported(isExported());

        return (T) newParameter;

    }
}
