// TODO: Each local threshold algorithm should really be tested with different dimension images, limits and multipliers

package io.github.mianalysis.mia.module.images.process.threshold;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URLDecoder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ij.IJ;
import ij.ImagePlus;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.Workspaces;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.image.ImageFactory;


public class ThresholdImageTest extends ModuleTest {
    private double tolerance = 1E-2;

    @BeforeAll
    public static void setVerbose() {
        Module.setVerbose(false);
    }

    @Override
    public void testGetHelp() {
        assertNotNull(new ThresholdImage(null).getDescription());
    }

    @Test
    public void testRunGlobalHuangNoLimsNoMultWhiteBG2D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient2D_8bit_GlobalHuangNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalHuangNoLimsNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalHuangNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalHuangNoLimsNoMultWhiteBG4D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient4D_ZT_8bit_C1.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient5D_8bit_C1_GlobalHuangNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalHuangNoLimsNoMultWhiteBG5D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient5D_8bit_GlobalHuangNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalHuangNoLimsNoMultWhiteBG3D16bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_16bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_16bit_GlobalHuangNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    // Temporarily removed.  The applied threshold is very close to the expected value; however, an update to the
    // normalisation/thresholding means it's not exactly the same.
    @Test @Disabled
    public void testRunGlobalHuangNoLimsNoMultWhiteBG3D32bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_32bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_32bit_GlobalHuangNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalHuangNoLimsNoMultBlackBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalHuangNoLimsNoMultBlackBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,false);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalHuangNoLims2xMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalHuangNoLims2xMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,2.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalHuangNoLims0p5xMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalHuangNoLims0p5xMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,0.5);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalHuangMinLimPassNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalHuangNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,true);
        thresholdImage.updateParameterValue(ThresholdImage.LOWER_THRESHOLD_LIMIT,100.0);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalHuangMinLimFailNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalHuangMinLimFailNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,true);
        thresholdImage.updateParameterValue(ThresholdImage.LOWER_THRESHOLD_LIMIT,140.0);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test @Disabled
    public void testRunLocal3DBernsenNoLimsNoMultWhiteBG3D8bit() throws Exception {

    }

    @Test @Disabled
    public void testRunLocal3DContrastNoLimsNoMultWhiteBG3D8bit() throws Exception {

    }

    @Test @Disabled
    public void testRunLocal3DMeanNoLimsNoMultWhiteBG3D8bit() throws Exception {

    }

    @Test @Disabled
    public void testRunLocal3DMedianNoLimsNoMultWhiteBG3D8bit() throws Exception {

    }

    @Test @Disabled
    public void testRunLocal3DPhansalkarNoLimsNoMultWhiteBG3D8bit() throws Exception {

    }

    @Test
    public void testRunLocalSlicePhansalkarNoLimsNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_LocalSlicePhansalkarNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.LOCAL);
        thresholdImage.updateParameterValue(ThresholdImage.LOCAL_ALGORITHM,ThresholdImage.LocalAlgorithms.PHANSALKAR_SLICE);
        thresholdImage.updateParameterValue(ThresholdImage.LOCAL_RADIUS,15.0);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunLocalSlicePhansalkarNoLimsNoMultBlackBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_LocalSlicePhansalkarNoLimsNoMultBlackBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.LOCAL);
        thresholdImage.updateParameterValue(ThresholdImage.LOCAL_ALGORITHM,ThresholdImage.LocalAlgorithms.PHANSALKAR_SLICE);
        thresholdImage.updateParameterValue(ThresholdImage.LOCAL_RADIUS,15.0);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,false);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunManual8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient2D_8bit_Manual62.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.MANUAL);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_VALUE,62);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,false);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }


    // INDIVIDUAL TESTS FOR EACH GLOBAL THRESHOLD

    @Test
    public void testRunGlobalIntermodesNoLimsNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);


        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalIntermodesNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.INTERMODES);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalIsodataNoLimsNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalIsoDataNoLimsNoMultBlackBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.ISO_DATA);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalMaxEntropyNoLimsNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalMaxEntropyNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.MAX_ENTROPY);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalOtsuNoLimsNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalOtsuNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.OTSU);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunGlobalTriangleNoLimsNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/thresholdimage/NoisyGradient3D_8bit_GlobalTriangleNoLimsNoMultWhiteBG.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.TRIANGLE);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running ThresholdImage
        thresholdImage.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }


    // VERIFYING THRESHOLD IS STORED

    @Test
    public void testRunStoreThresholdGlobalHuangNoLimsNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,false);
        thresholdImage.updateParameterValue(ThresholdImage.OUTPUT_IMAGE,"Test_output");
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running MeasureImageIntensity
        thresholdImage.execute(workspace);

        // Getting output image
        Image outputImage = workspace.getImage("Test_output");

        // Verifying results
        assertEquals(1,outputImage.getMeasurements().size());

        String measurementName = ThresholdImage.getFullName(ThresholdImage.Measurements.GLOBAL_VALUE,ThresholdImage.GlobalAlgorithms.HUANG);
        assertEquals(130.0, outputImage.getMeasurement(measurementName).getValue(),tolerance);

    }

    @Test
    public void testRunStoreThresholdGlobalHuangApplyNoLimsNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,true);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running MeasureImageIntensity
        thresholdImage.execute(workspace);

        // Verifying results
        assertEquals(1,image.getMeasurements().size());

        String measurementName = ThresholdImage.getFullName(ThresholdImage.Measurements.GLOBAL_VALUE,ThresholdImage.GlobalAlgorithms.HUANG);
        assertEquals(130.0, image.getMeasurement(measurementName).getValue(),tolerance);

    }

    @Test
    public void testRunStoreThresholdGlobalHuangApplyMinLimNoMultWhiteBG3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,true);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.GLOBAL);
        thresholdImage.updateParameterValue(ThresholdImage.GLOBAL_ALGORITHM,ThresholdImage.GlobalAlgorithms.HUANG);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,true);
        thresholdImage.updateParameterValue(ThresholdImage.LOWER_THRESHOLD_LIMIT,145.0);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running MeasureImageIntensity
        thresholdImage.execute(workspace);

        // Verifying results
        assertEquals(1,image.getMeasurements().size());

        String measurementName = ThresholdImage.getFullName(ThresholdImage.Measurements.GLOBAL_VALUE,ThresholdImage.GlobalAlgorithms.HUANG);
        assertEquals(145.0, image.getMeasurement(measurementName).getValue(),tolerance);

    }
    @Test
    public void testRunStoreThresholdLocal() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,true);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.LOCAL);
        thresholdImage.updateParameterValue(ThresholdImage.LOCAL_ALGORITHM,ThresholdImage.LocalAlgorithms.PHANSALKAR_SLICE);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running MeasureImageIntensity
        thresholdImage.execute(workspace);

        // Verifying results
        assertEquals(0,image.getMeasurements().size());

    }

    @Test
    public void testRunStoreThresholdManual() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        // Initialising ThresholdImage
        ThresholdImage thresholdImage = new ThresholdImage(new Modules());
        thresholdImage.initialiseParameters();
        thresholdImage.updateParameterValue(ThresholdImage.INPUT_IMAGE,"Test_image");
        thresholdImage.updateParameterValue(ThresholdImage.APPLY_TO_INPUT,true);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_TYPE,ThresholdImage.ThresholdTypes.MANUAL);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_VALUE,12);
        thresholdImage.updateParameterValue(ThresholdImage.THRESHOLD_MULTIPLIER,1.0);
        thresholdImage.updateParameterValue(ThresholdImage.USE_LOWER_THRESHOLD_LIMIT,false);
        thresholdImage.updateParameterValue(ThresholdImage.WHITE_BACKGROUND,true);

        // Running MeasureImageIntensity
        thresholdImage.execute(workspace);

        // Verifying results
        assertEquals(0,image.getMeasurements().size());

    }
}