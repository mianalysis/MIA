package io.github.mianalysis.mia.module.objects.transform;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.mianalysis.mia.module.ModuleTest;


public class ReassignEnclosedObjectsTest extends ModuleTest {

    @Override
    public void testGetHelp() {
        assertNotNull(new ReassignEnclosedObjects(null).getDescription());
    }
}