package wbif.sjx.MIA.Module.ImageProcessing.Pixel;

import wbif.sjx.MIA.Module.ModuleTest;

import static org.junit.Assert.*;

public class CombingCorrectionTest extends ModuleTest {

    @Override
    public void testGetTitle() {
        assertNotNull(new CombingCorrection().getTitle());
    }

    @Override
    public void testGetHelp() {
        assertNotNull(new CombingCorrection().getHelp());
    }
}