package io.github.mianalysis.MIA.Module.Visualisation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URLDecoder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import io.github.mianalysis.MIA.ExpectedObjects.DenseTracks2D;
import io.github.mianalysis.MIA.ExpectedObjects.ExpectedObjects;
import io.github.mianalysis.MIA.Module.Module;
import io.github.mianalysis.MIA.Module.ModuleTest;
import io.github.mianalysis.MIA.Module.ImageProcessing.Stack.Convert3DStack;
import io.github.mianalysis.MIA.Module.ObjectProcessing.Relationships.TrackObjects;
import io.github.mianalysis.MIA.Module.Visualisation.Overlays.AddObjectsOverlay;
import io.github.mianalysis.MIA.Object.Image;
import io.github.mianalysis.MIA.Object.Objs;
import io.github.mianalysis.MIA.Object.Workspace;
import io.github.mianalysis.MIA.Object.Workspaces;
import io.github.sjcross.common.Object.Volume.VolumeType;

public class AddObjectsOverlayTest extends ModuleTest {
    @BeforeAll
    public static void setVerbose() {
        Module.setVerbose(false);
    }

    @Test @Disabled
    public void getColours() {
    }

    @Test @Disabled
    public void getLabels() {
    }

    @Test @Disabled
    public void getPositionMeasurements() {
    }

    @Test @Disabled
    public void addAllPointsOverlay() {
    }

    @Test @Disabled
    public void addCentroidOverlay() {
    }

    @Test @Disabled
    public void addOutlineOverlay() {
    }

    @Test @Disabled
    public void addPositionMeasurementsOverlay() {
    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    @Disabled
    public void testCreateTrackOverlay(VolumeType volumeType) throws Exception {

        // THE OBJECTS FOR THIS TEST WILL NEED TO BE RE-CREATED - WE DON'T WANT OBJECTS SPANNING A 512 X 512 IMAGE

        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);
        
        // Setting object parameters
        String inputObjectsName = "Test_objects";
        String trackObjectsName = "Tracks";
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "um";

        // Creating objects and adding to workspace
        Objs testObjects = new DenseTracks2D(volumeType).getObjects(inputObjectsName, ExpectedObjects.Mode.EIGHT_BIT,dppXY,dppZ,calibratedUnits,true);
        workspace.addObjects(testObjects);

        // Tracking objects
        // FOR FINAL TEST DON'T RELY ON ANOTHER MODULE
        TrackObjects trackObjectsModule = (TrackObjects) new TrackObjects(null)
                .updateParameterValue(TrackObjects.INPUT_OBJECTS,inputObjectsName)
                .updateParameterValue(TrackObjects.TRACK_OBJECTS,trackObjectsName)
                .updateParameterValue(TrackObjects.LINKING_METHOD,TrackObjects.LinkingMethods.CENTROID)
                .updateParameterValue(TrackObjects.MAXIMUM_LINKING_DISTANCE,2d)
                .updateParameterValue(TrackObjects.MAXIMUM_MISSING_FRAMES,0);
        trackObjectsModule.execute(workspace);

        // Loading the test image and adding to workspace
        String imageName = "Test_image";
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/Tracks.tif").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Convert3DStack.process(ipl,Convert3DStack.Modes.OUTPUT_TIMESERIES);
        Image intensityImage = new Image(imageName,ipl);
        workspace.addImage(intensityImage);

        AddObjectsOverlay addObjectsOverlay = (AddObjectsOverlay) new AddObjectsOverlay(null)
                .updateParameterValue(AddObjectsOverlay.INPUT_OBJECTS,inputObjectsName)
                .updateParameterValue(AddObjectsOverlay.SPOT_OBJECTS,trackObjectsName)
                .updateParameterValue(AddObjectsOverlay.INPUT_IMAGE,imageName)
                .updateParameterValue(AddObjectsOverlay.POSITION_MODE,AddObjectsOverlay.PositionModes.TRACKS)
                .updateParameterValue(AddObjectsOverlay.LINE_WIDTH,1d)
                .updateParameterValue(AddObjectsOverlay.LIMIT_TRACK_HISTORY,false);

//        HashMap<Integer, Color> colours = testObjects.getHues(Objs.ColourModes.PARENT_ID,trackObjectsName, false);
//        Objs trackObjects = workspace.getObjectSet(trackObjectsName);
//        addObjectsOverlay.createTrackOverlay(ipl,trackObjects,colours);

        new ImageJ();
        addObjectsOverlay.execute(workspace);

        IJ.runMacro("waitForUser");

    }

    @Test @Disabled
    public void addLabelsOverlay() {
    }

    @Test
    public void testGetHelp() {
        assertNotNull(new AddObjectsOverlay(null).getDescription());
    }
}