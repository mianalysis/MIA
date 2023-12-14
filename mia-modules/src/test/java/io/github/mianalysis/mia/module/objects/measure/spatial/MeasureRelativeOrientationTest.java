package io.github.mianalysis.mia.module.objects.measure.spatial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ome.units.UNITS;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.module.objects.measure.spatial.MeasureRelativeOrientation;
import io.github.mianalysis.mia.object.Obj;
import io.github.mianalysis.mia.object.Objs;
import io.github.mianalysis.mia.object.coordinates.Point;
import io.github.mianalysis.mia.object.coordinates.volume.PointOutOfRangeException;
import io.github.mianalysis.mia.object.coordinates.volume.SpatCal;
import io.github.mianalysis.mia.object.coordinates.volume.VolumeType;
import io.github.mianalysis.mia.process.exceptions.IntegerOverflowException;


public class MeasureRelativeOrientationTest extends ModuleTest {
    private double tolerance = 1E-2;

    @BeforeAll
    public static void setVerbose() {
        Module.setVerbose(false);
    }

    @Override
    public void testGetHelp() {
        assertNotNull(new MeasureRelativeOrientation(null).getDescription());
    }


    //// ZERO TO NINETY DEGREE TESTS ////

    // POSITIVE X-AXIS
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation180ZeroNinety(VolumeType volumeType)
            throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation135ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation90ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation30ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 30;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 30;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation0ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientationMinus30ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = -30;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 30;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientationMinus45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }




    // TOP RIGHT QUADRANT IN 2D

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation180ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation135ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation90ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {


        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation0ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientationMinus45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientationMinus90ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = -90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientationMinus135ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = -135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientationMinus180ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = -180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }


    // BOTTOM RIGHT QUADRANT IN 2D

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation180ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation135ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation90ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation0ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientationMinus45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientationMinus90ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = -90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientationMinus135ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = -135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientationMinus180ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = -180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }


    // BOTTOM LEFT QUADRANT IN 2D

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation180ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation135ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation90ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation0ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientationMinus45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientationMinus90ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = -90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientationMinus135ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = -135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientationMinus180ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = -180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }


    // TOP LEFT QUADRANT IN 2D

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation180ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation135ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation90ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation0ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientationMinus45ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientationMinus90ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = -90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientationMinus135ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = -135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientationMinus180ZeroNinety(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = -180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_NINETY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }



    //// ZERO TO ONE HUNDRED AND EIGHTY DEGREE TESTS ////

    // POSITIVE X-AXIS
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation180ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation135ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation90ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation30ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 30;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 150;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientation0ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 180;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientationMinus30ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = -30;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 150;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAnglePosXOrientationMinus45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,10,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }


    // TOP RIGHT QUADRANT IN 2D

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation180ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation135ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation90ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 180;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientation0ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientationMinus45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientationMinus90ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = -90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientationMinus135ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = -135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopRightOrientationMinus180ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,15,0);
        double xyOrientation = -180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }


    // BOTTOM RIGHT QUADRANT IN 2D

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation180ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation135ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation90ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientation0ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientationMinus45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 180;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientationMinus90ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = -90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientationMinus135ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = -135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomRightOrientationMinus180ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(15,5,0);
        double xyOrientation = -180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }


    // BOTTOM LEFT QUADRANT IN 2D

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation180ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation135ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation90ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientation0ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientationMinus45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientationMinus90ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = -90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientationMinus135ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = -135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 180;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleBottomLeftOrientationMinus180ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,5,0);
        double xyOrientation = -180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }


    // TOP LEFT QUADRANT IN 2D

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation180ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation135ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 180;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation90ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientation0ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = 0;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientationMinus45ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = -45;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 0;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientationMinus90ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = -90;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 45;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientationMinus135ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = -135;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 90;

        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYAngleTopLeftOrientationMinus180ZeroOneEighty(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        SpatCal spatCal = new SpatCal(0.02,0.1,"um",30,30,1);
        Objs objects = new Objs("Objects",spatCal, 1, 0.02, UNITS.SECOND);
        Obj object = objects.createAndAddNewObject(volumeType);
        object.add(5,15,0);
        double xyOrientation = -180;
        String measurementRange = MeasureRelativeOrientation.MeasurementRanges.ZERO_ONE_EIGHTY;

        Point<Double> referencePoint = new Point<>(10d,10d,0d);

        double actual = MeasureRelativeOrientation.getXYAngle(object,xyOrientation,measurementRange,referencePoint);
        double expected = 135;

        assertEquals(expected,actual,tolerance);

    }
}