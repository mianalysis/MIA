package io.github.mianalysis.mia.module.images.process.threshold;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ij.IJ;
import ij.ImagePlus;
import io.github.mianalysis.enums.Dimension;
import io.github.mianalysis.enums.Logic;
import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.Workspaces;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.image.ImageFactory;

/**
 * Created by Gemma and George on 05/10/2022.
 */
public class GlobalAutoThresholdMSTest extends ModuleTest {

    enum Threshold {
        THUANG,
        TINTERMODES,
        TISO_DATA,
        TLI,
        TMAX_ENTROPY,
        TMEAN,
        TMIN_ERROR,
        TMINIMUM,
        TMOMENTS,
        TOTSU,
        TPERCENTILE,
        TRENYI_ENTROPY,
        TSHANBHAG,
        TTRIANGLE,
        TYEN
    }

    /**
     * Generates all permutations
     */
    public static Stream<Arguments> dimThresholdLogicInputProvider() {
        Stream.Builder<Arguments> argumentBuilder = Stream.builder();
        for (Dimension dimension : Dimension.values())
            for (Threshold threshold : Threshold.values())
                for (Logic logic : Logic.values())
                    argumentBuilder.add(Arguments.of(dimension, threshold, logic));

        return argumentBuilder.build();

    }

    /**
     * Parameterized test run with 8-bit bit depth and all dimensions, all threshold algorithms and all logics. 
     * The reduced testing here is to keep storage requirements down.
     * 
     * @throws UnsupportedEncodingException
     */
    @ParameterizedTest
    @MethodSource("dimThresholdLogicInputProvider")
    void testAll(Dimension dimension, Threshold threshold, Logic logic) throws UnsupportedEncodingException {
        runTest(dimension, threshold, logic);

    }

    /**
     * Performs the test
     * 
     * @throws UnsupportedEncodingException
     */
    public static void runTest(Dimension dimension, Threshold threshold, Logic logic)
            throws UnsupportedEncodingException {
        // Checks input image and expected images are available. If not found, the test
        // skips
        String inputName = "/msimages/noisygradient/NoisyGradient_" + dimension + "_B8.zip";
        assumeTrue(GlobalAutoThresholdMSTest.class.getResource(inputName) != null);

        String expectedName = "/msimages/globalautothreshold/GAThreshold_" + dimension + "_B8_" + threshold + "_"
                + logic + ".zip";
        assumeTrue(GlobalAutoThresholdMSTest.class.getResource(expectedName) != null);

        // Doing the main part of the test
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null, 1);

        // Loading the test image and adding to workspace
        String inputPath = URLDecoder.decode(GlobalAutoThresholdMSTest.class.getResource(inputName).getPath(), "UTF-8");
        ImagePlus ipl = IJ.openImage(inputPath);
        Image image = ImageFactory.createImage("Test_image", ipl);
        workspace.addImage(image);

        String expectedPath = URLDecoder.decode(GlobalAutoThresholdMSTest.class.getResource(expectedName).getPath(),
                "UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(expectedPath));

        // Initialising module and setting parameters
        GlobalAutoThreshold module = new GlobalAutoThreshold(new Modules());
        module.updateParameterValue(GlobalAutoThreshold.INPUT_IMAGE, "Test_image");
        module.updateParameterValue(GlobalAutoThreshold.APPLY_TO_INPUT, false);
        module.updateParameterValue(GlobalAutoThreshold.OUTPUT_IMAGE, "Test_output");

        switch (threshold) {
            case THUANG:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.HUANG);
                break;
            case TINTERMODES:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.INTERMODES);
                break;
            case TISO_DATA:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.ISO_DATA);
                break;
            case TLI:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.LI);
                break;
            case TMAX_ENTROPY:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.MAX_ENTROPY);
                break;
            case TMEAN:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.MEAN);
                break;
            case TMINIMUM:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.MINIMUM);
                break;
            case TMIN_ERROR:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.MIN_ERROR);
                break;
            case TMOMENTS:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.MOMENTS);
                break;
            case TOTSU:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.OTSU);
                break;
            case TPERCENTILE:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.PERCENTILE);
                break;
            case TRENYI_ENTROPY:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM,
                        GlobalAutoThreshold.Algorithms.RENYI_ENTROPY);
                break;
            case TSHANBHAG:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.SHANBHAG);
                break;
            case TTRIANGLE:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.TRIANGLE);
                break;
            case TYEN:
                module.updateParameterValue(GlobalAutoThreshold.ALGORITHM, GlobalAutoThreshold.Algorithms.YEN);
                break;
        }

        switch (logic) {
            case LB:
                module.updateParameterValue(GlobalAutoThreshold.BINARY_LOGIC,
                        GlobalAutoThreshold.BinaryLogic.BLACK_BACKGROUND);
                break;
            case LW:
                module.updateParameterValue(GlobalAutoThreshold.BINARY_LOGIC,
                        GlobalAutoThreshold.BinaryLogic.WHITE_BACKGROUND);
                break;
        }

        // Running Module
        module.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2, workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage, outputImage);

    }

    /**
     * Test to check this module has an assigned description
     */
    @Override
    public void testGetHelp() {
        assertNotNull(new GlobalAutoThreshold(null).getDescription());
    }

}