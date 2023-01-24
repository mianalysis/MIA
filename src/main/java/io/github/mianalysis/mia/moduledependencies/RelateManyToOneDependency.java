package io.github.mianalysis.mia.moduledependencies;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Dependency.class, priority = Priority.LOW, visible = true)
public class RelateManyToOneDependency extends Dependency {
    @Override
    public String getModuleName() {
        return "RelateManyToOne";
    }

    @Override
    public String getClassName() {
        return "fiji.plugin.trackmate.tracking.jaqaman.JaqamanLinker";
    }

    @Override
    public String getMessage() {
        return "Please update TrackMate dependency";
    }

    @Override
    public String getVersionThreshold() {
        return "0.0.0";
    }

    @Override
    public Relationship getRelationship() {
        return Relationship.GREATER_THAN_OR_EQUAL_TO;
    }
}
