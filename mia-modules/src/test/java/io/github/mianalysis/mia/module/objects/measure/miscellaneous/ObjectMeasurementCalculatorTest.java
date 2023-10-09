package io.github.mianalysis.mia.module.objects.measure.miscellaneous;

import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.module.objects.measure.miscellaneous.ObjectMeasurementCalculator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Stephen Cross on 19/03/2019.
 */

public class ObjectMeasurementCalculatorTest extends ModuleTest {

    @Override
    public void testGetHelp() {
        assertNotNull(new ObjectMeasurementCalculator(null).getDescription());
    }
}