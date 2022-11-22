// package io.github.mianalysis.mia.module.images.process;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assumptions.assumeTrue;

// import java.io.UnsupportedEncodingException;
// import java.net.URLDecoder;
// import java.util.stream.Stream;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.Arguments;
// import org.junit.jupiter.params.provider.MethodSource;

// import ij.IJ;
// import ij.ImagePlus;
// import io.github.mianalysis.enums.BitDepth;
// import io.github.mianalysis.enums.Dimension;
// import io.github.mianalysis.enums.OutputMode;
// import io.github.mianalysis.mia.module.Modules;
// import io.github.mianalysis.mia.object.Measurement;
// import io.github.mianalysis.mia.object.Workspace;
// import io.github.mianalysis.mia.object.Workspaces;
// import io.github.mianalysis.mia.object.image.Image;
// import io.github.mianalysis.mia.object.image.ImageFactory;
// import io.github.mianalysis.mia.object.image.ImageType;
// import io.github.mianalysis.mia.object.system.Status;

// public class ImageMathMSTest {
//     enum Operation {
//         OABSOLUTE,
//         OADD,
//         ODIVIDE,
//         OMULTIPLY,
//         OSQUARE,
//         OSQRT,
//         OSUBTRACT
//     }

//     enum ValueSource {
//         SFIXED,
//         SIMAGE
//     }

//     public static Stream<Arguments> dimOperationInputProvider() {
//         Stream.Builder<Arguments> argumentBuilder = Stream.builder();
//         for (Dimension dimension : Dimension.values())
//             for (Operation operation : Operation.values())
//                 for (OutputMode outputMode : OutputMode.values())
//                     for (ImageType imageType : ImageType.values())
//                         for (ValueSource valueSource : ValueSource.values())
//                             argumentBuilder.add(Arguments.of(dimension, operation, outputMode, imageType, valueSource));

//         return argumentBuilder.build();

//     }

//     @ParameterizedTest
//     @MethodSource("dimOperationInputProvider")
//     void test8BitValue3p2(Dimension dimension, Operation operation, OutputMode outputMode, ImageType imageType,
//             ValueSource valueSource)
//             throws UnsupportedEncodingException {
//         runTest(dimension, BitDepth.B8, operation, 3.2, outputMode, imageType, valueSource);

//     }

//     @Test
//     void testTest()
//             throws UnsupportedEncodingException {
//         runTest(Dimension.D3Z, BitDepth.B8, Operation.OADD, 3.2, OutputMode.APPLY_TO_INPUT, ImageType.IMAGEPLUS, ValueSource.SFIXED);

//     }

//     public static void runTest(Dimension dimension, BitDepth bitDepth, Operation operation, double value,
//             OutputMode outputMode, ImageType imageType, ValueSource valueSource)
//             throws UnsupportedEncodingException {
//         boolean applyToInput = outputMode.equals(OutputMode.APPLY_TO_INPUT);

//         // Checks input image and expected images are available. If not found, the test
//         // skips
//         String inputName = "/msimages/noisygradient/NoisyGradient_" + dimension + "_" + bitDepth + ".zip";
//         assumeTrue(FilterImageMSTest.class.getResource(inputName) != null);

//         // Doing the main part of the test
//         // Creating a new workspace
//         Workspaces workspaces = new Workspaces();
//         Workspace workspace = workspaces.getNewWorkspace(null, 1);

//         // Loading the test image and adding to workspace
//         String inputPath = URLDecoder.decode(FilterImageMSTest.class.getResource(inputName).getPath(), "UTF-8");
//         ImagePlus ipl = IJ.openImage(inputPath);
//         Image image = ImageFactory.createImage("Test_image", ipl, imageType);
//         workspace.addImage(image);

//         // Loading the expected image
//         String expectedName = "/msimages/imagemath/ImageMath_" + dimension + "_" + bitDepth + "_" + operation + "_V"
//                 + value + ".zip";
//         assumeTrue(FilterImageMSTest.class.getResource(expectedName) != null);

//         String expectedPath = URLDecoder.decode(FilterImageMSTest.class.getResource(expectedName).getPath(), "UTF-8");
//         Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(expectedPath), imageType);

//         // Initialising module and setting parameters
//         ImageMath imageMath = new ImageMath(new Modules());
//         imageMath.updateParameterValue(ImageMath.INPUT_IMAGE, "Test_image");
//         imageMath.updateParameterValue(ImageMath.APPLY_TO_INPUT, applyToInput);
//         if (!applyToInput)
//             imageMath.updateParameterValue(ImageMath.OUTPUT_IMAGE, "Test_output");

//         switch (operation) {
//             case OABSOLUTE:
//                 imageMath.updateParameterValue(ImageMath.CALCULATION_MODE, ImageMath.CalculationModes.ABSOLUTE);
//                 break;
//             case OADD:
//                 imageMath.updateParameterValue(ImageMath.CALCULATION_MODE, ImageMath.CalculationModes.ADD);
//                 break;
//             case ODIVIDE:
//                 imageMath.updateParameterValue(ImageMath.CALCULATION_MODE, ImageMath.CalculationModes.DIVIDE);
//                 break;
//             case OMULTIPLY:
//                 imageMath.updateParameterValue(ImageMath.CALCULATION_MODE, ImageMath.CalculationModes.MULTIPLY);
//                 break;
//             case OSQRT:
//                 imageMath.updateParameterValue(ImageMath.CALCULATION_MODE, ImageMath.CalculationModes.SQUAREROOT);
//                 break;
//             case OSQUARE:
//                 imageMath.updateParameterValue(ImageMath.CALCULATION_MODE, ImageMath.CalculationModes.SQUARE);
//                 break;
//             case OSUBTRACT:
//                 imageMath.updateParameterValue(ImageMath.CALCULATION_MODE, ImageMath.CalculationModes.SUBTRACT);
//                 break;
//         }

//         switch (valueSource) {
//             case SFIXED:
//                 imageMath.updateParameterValue(ImageMath.VALUE_SOURCE, ImageMath.ValueSources.FIXED);
//                 imageMath.updateParameterValue(ImageMath.MATH_VALUE, value);
//                 break;
//             case SIMAGE:
//                 // We can still use the fixed value from the arguments, it's just assigned to
//                 // the image.
//                 image.addMeasurement(new Measurement("MATH_VAL", value));
//                 imageMath.updateParameterValue(ImageMath.VALUE_SOURCE, ImageMath.ValueSources.MEASUREMENT);
//                 imageMath.updateParameterValue(ImageMath.IMAGE_FOR_MEASUREMENT, "Test_image");
//                 imageMath.updateParameterValue(ImageMath.MEASUREMENT, "MATH_VAL");

//                 // To make doubly-sure, the fixed value parameter is set to NaN
//                 imageMath.updateParameterValue(ImageMath.MATH_VALUE, Double.NaN);
//                 break;
//         }

//         // Running Module
//         Status status = imageMath.execute(workspace);
//         assertEquals(Status.PASS, status);

//         // Checking the images in the workspace
//         if (applyToInput) {
//             assertEquals(1, workspace.getImages().size());
//             assertNotNull(workspace.getImage("Test_image"));

//             Image outputImage = workspace.getImage("Test_image");
//             assertEquals(expectedImage, outputImage);

//         } else {
//             assertEquals(2, workspace.getImages().size());
//             assertNotNull(workspace.getImage("Test_image"));
//             assertNotNull(workspace.getImage("Test_output"));

//             Image outputImage = workspace.getImage("Test_output");
//             assertEquals(expectedImage, outputImage);

//         }
//     }
// }
