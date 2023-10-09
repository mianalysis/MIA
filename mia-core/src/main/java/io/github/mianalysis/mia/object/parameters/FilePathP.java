package io.github.mianalysis.mia.object.parameters;

import io.github.mianalysis.mia.gui.parametercontrols.ParameterControl;
import io.github.mianalysis.mia.gui.parametercontrols.FileParameter;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.object.parameters.abstrakt.FileFolderType;
import io.github.mianalysis.mia.object.parameters.abstrakt.Parameter;

import com.drew.lang.annotations.NotNull;
import java.io.File;

public class FilePathP extends FileFolderType {
    public FilePathP(String name, Module module) {
        super(name, module);
    }

    public FilePathP(String name, Module module, @NotNull String filePath) {
        super(name, module, filePath);
    }

    public FilePathP(String name, Module module, @NotNull String filePath, String description) {
        super(name, module, filePath, description);
    }


    @Override
    public void setPath(String path) {
        if (path == null) return;

        this.path = path;

    }

    @Override
    protected ParameterControl initialiseControl() {
        return new FileParameter(this,FileParameter.FileTypes.FILE_TYPE);
    }

    @Override
    public <T extends Parameter> T duplicate(Module newModule) {
        FilePathP newParameter = new FilePathP(name,newModule,getPath(),getDescription());

        newParameter.setNickname(getNickname());
        newParameter.setVisible(isVisible());
        newParameter.setExported(isExported());

        return (T) newParameter;

    }

    @Override
    public boolean verify() {
        // Check file is specified
        if (!super.verify()) return false;

        // Check file exists
        if (!new File(path).exists()) return false;

        // Finally, check file is a file (not a folder)
        return new File(path).isFile();

    }
}
