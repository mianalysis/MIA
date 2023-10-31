package io.github.mianalysis.mia.module.objects.process;

import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.module.objects.convert.CreateDistanceMap;

import static org.junit.jupiter.api.Assertions.*;


public class CreateDistanceMapTest extends ModuleTest {

    @Override
    public void testGetHelp() {
        assertNotNull(new CreateDistanceMap(null).getDescription());
    }
}