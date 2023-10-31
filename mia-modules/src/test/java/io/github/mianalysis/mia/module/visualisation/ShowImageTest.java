package io.github.mianalysis.mia.module.visualisation;

import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.module.visualise.ShowImage;

import static org.junit.jupiter.api.Assertions.*;


public class ShowImageTest extends ModuleTest {

    @Override
    public void testGetHelp() {
        assertNotNull(new ShowImage(null).getDescription());
    }
}