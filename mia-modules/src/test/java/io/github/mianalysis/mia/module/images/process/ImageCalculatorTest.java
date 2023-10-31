package io.github.mianalysis.mia.module.images.process;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URLDecoder;

import org.junit.jupiter.api.Test;

import ij.IJ;
import ij.ImagePlus;
import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.Workspaces;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.image.ImageFactory;


public class ImageCalculatorTest extends ModuleTest {
    private double tolerance = 1E-2;

    @Override
    public void testGetHelp() {
        assertNotNull(new ImageCalculator(null).getDescription());
    }

    // ADD OPERATION

    @Test
    public void testRun5DAddCreateNot32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientAddNot32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.CREATE_NEW);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_IMAGE,"Output_image");
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,false);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.ADD);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(3,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));
        assertNotNull(workspace.getImage("Output_image"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Output_image").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(expectedImage.getBitDepth(),outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }

    @Test
    public void testRun5DAddOverwrite1Not32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientAddNot32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.OVERWRITE_IMAGE1);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,false);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.ADD);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Test_image_1").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(expectedImage.getBitDepth(),outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }

    @Test
    public void testRun5DAddOverwrite2Not32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientAddNot32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.OVERWRITE_IMAGE2);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,false);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.ADD);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Test_image_2").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(expectedImage.getBitDepth(),outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }

    @Test
    public void testRun5DAddCreateIs32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientAddIs32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.CREATE_NEW);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_IMAGE,"Output_image");
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,true);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.ADD);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(3,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));
        assertNotNull(workspace.getImage("Output_image"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Output_image").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(32,outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }

    @Test
    public void testRun5DAddOverwrite1Is32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientAddIs32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.OVERWRITE_IMAGE1);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,true);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.ADD);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Test_image_1").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(32,outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }

    @Test
    public void testRun5DAddOverwrite2Is32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientAddIs32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.OVERWRITE_IMAGE2);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,true);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.ADD);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Test_image_2").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(32,outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }


    // SUBTRACT OPERATION

    @Test
    public void testRun5DSubtractCreateNot32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientSubtractNot32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.CREATE_NEW);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_IMAGE,"Output_image");
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,false);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.SUBTRACT);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(3,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));
        assertNotNull(workspace.getImage("Output_image"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Output_image").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(expectedImage.getBitDepth(),outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }

    @Test
    public void testRun5DSubtractCreateIs32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientSubtractIs32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.CREATE_NEW);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_IMAGE,"Output_image");
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,true);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.SUBTRACT);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(3,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));
        assertNotNull(workspace.getImage("Output_image"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Output_image").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(32,outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }


    // MULTIPLY OPERATION

    @Test
    public void testRun5DMultiplyCreateNot32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientMultiplyNot32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.CREATE_NEW);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_IMAGE,"Output_image");
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,false);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.MULTIPLY);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(3,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));
        assertNotNull(workspace.getImage("Output_image"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Output_image").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(expectedImage.getBitDepth(),outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }

    @Test
    public void testRun5DMultiplyCreateIs32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientMultiplyIs32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.CREATE_NEW);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_IMAGE,"Output_image");
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,true);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.MULTIPLY);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(3,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));
        assertNotNull(workspace.getImage("Output_image"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Output_image").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(32,outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }


    // DIVIDE OPERATION

    @Test
    public void testRun5DDivideCreateNot32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientDivideNot32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.CREATE_NEW);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_IMAGE,"Output_image");
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,false);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.DIVIDE);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(3,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));
        assertNotNull(workspace.getImage("Output_image"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Output_image").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(expectedImage.getBitDepth(),outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }

    @Test
    public void testRun5DDivideCreateIs32() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/binaryobjects/BinaryObjects5D_8bit_blackBG.zip").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = ImageFactory.createImage("Test_image_1",ipl1);
        workspace.addImage(image1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = ImageFactory.createImage("Test_image_2",ipl2);
        workspace.addImage(image2);

        pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/imagecalculator/BinaryAndNoisyGradientDivideIs32.zip").getPath(),"UTF-8");
        ImagePlus expectedImage = IJ.openImage(pathToImage2);

        // Initialising BinaryOperations
        ImageCalculator calculator = new ImageCalculator(null);
        calculator.initialiseParameters();
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE1,"Test_image_1");
        calculator.updateParameterValue(ImageCalculator.INPUT_IMAGE2,"Test_image_2");
        calculator.updateParameterValue(ImageCalculator.OVERWRITE_MODE,ImageCalculator.OverwriteModes.CREATE_NEW);
        calculator.updateParameterValue(ImageCalculator.OUTPUT_IMAGE,"Output_image");
        calculator.updateParameterValue(ImageCalculator.OUTPUT_32BIT,true);
        calculator.updateParameterValue(ImageCalculator.CALCULATION_METHOD, ImageCalculator.CalculationMethods.DIVIDE);

        // Running Module
        calculator.execute(workspace);

        // Checking the images in the workspace
        assertEquals(3,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image_1"));
        assertNotNull(workspace.getImage("Test_image_2"));
        assertNotNull(workspace.getImage("Output_image"));

        // Checking the output image has the expected calibration
        ImagePlus outputImage = workspace.getImage("Output_image").getImagePlus();
        assertEquals(dppXY,outputImage.getCalibration().pixelWidth,tolerance);
        assertEquals(dppZ,outputImage.getCalibration().pixelDepth,tolerance);
        assertEquals(calibratedUnits,outputImage.getCalibration().getXUnit());
        assertEquals(32,outputImage.getBitDepth());

        // Checking the size of the output image
        assertEquals(expectedImage.getWidth(),outputImage.getWidth());
        assertEquals(expectedImage.getHeight(),outputImage.getHeight());
        assertEquals(expectedImage.getNChannels(),outputImage.getNChannels());
        assertEquals(expectedImage.getNSlices(),outputImage.getNSlices());
        assertEquals(expectedImage.getNFrames(),outputImage.getNFrames());

        // Checking the individual image pixel values
        for (int c=0;c<outputImage.getNChannels();c++) {
            for (int z = 0; z < outputImage.getNSlices(); z++) {
                for (int t = 0; t < outputImage.getNFrames(); t++) {
                    expectedImage.setPosition(c+1, z + 1, t + 1);
                    outputImage.setPosition(c+1, z + 1, t + 1);

                    float[][] expectedValues = expectedImage.getProcessor().getFloatArray();
                    float[][] actualValues = outputImage.getProcessor().getFloatArray();

                    assertArrayEquals(expectedValues, actualValues);

                }
            }
        }
    }

}