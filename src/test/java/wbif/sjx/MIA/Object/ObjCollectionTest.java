package wbif.sjx.MIA.Object;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Volume.VolumeType;

import static org.junit.jupiter.api.Assertions.*;

public class ObjCollectionTest {
    private double tolerance = 1E-2;

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetFirstPresent(VolumeType volumeType) {
        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        ObjCollection collection = new ObjCollection("TestObj");

        Obj obj = new Obj(volumeType,"New obj",0,1,1,1,dppXY,dppZ,calibratedUnits);
        collection.add(obj);

        obj = new Obj(volumeType,"New obj",1,1,1,1,dppXY,dppZ,calibratedUnits);
        collection.add(obj);

        obj = new Obj(volumeType,"New obj",2,1,1,1,dppXY,dppZ,calibratedUnits);
        collection.add(obj);

        Obj firstObj = collection.getFirst();
        assertNotNull(firstObj);
        assertEquals(0,firstObj.getID());

    }

    @Test
    public void testGetFirstAbsent() {
        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        ObjCollection collection = new ObjCollection("TestObj");
        Obj firstObj = collection.getFirst();
        assertNull(firstObj);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetSpatialLimits(VolumeType volumeType) throws IntegerOverflowException {
        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Creating the ObjCollection
        ObjCollection collection = new ObjCollection("Obj");

        // Adding objects
        Obj obj = new Obj(volumeType,"Obj",0,10,3,12,dppXY,dppZ,calibratedUnits);
        obj.add(3,1,6);
        obj.add(2,2,8);
        collection.add(obj);

        obj = new Obj(volumeType,"Obj",1,10,3,12,dppXY,dppZ,calibratedUnits);
        obj.add(3,2,2);
        obj.add(2,2,9);
        collection.add(obj);

        obj = new Obj(volumeType,"Obj",2,10,3,12,dppXY,dppZ,calibratedUnits);
        obj.add(4,1,2);
        obj.add(6,2,10);
        collection.add(obj);

        // Getting expected spatial limits
        int[][] expected = new int[][]{{0,9},{0,2},{0,11}};

        // Checking actual values
        int[][] actual = collection.getSpatialLimits();

        for (int i=0;i<expected.length;i++) {
            assertArrayEquals(expected[i],actual[i]);
        }
    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetTimepointLimits(VolumeType volumeType) {
        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Creating the ObjCollection
        ObjCollection collection = new ObjCollection("Obj");

        // Adding objects
        Obj obj = new Obj(volumeType,"Obj",0,1,1,1,dppXY,dppZ,calibratedUnits);
        obj.setT(9);
        collection.add(obj);

        obj = new Obj(volumeType,"Obj",1,1,1,1,dppXY,dppZ,calibratedUnits);
        obj.setT(3);
        collection.add(obj);

        obj = new Obj(volumeType,"Obj",2,1,1,1,dppXY,dppZ,calibratedUnits);
        obj.setT(12);
        collection.add(obj);

        int[] expected = new int[]{3,12};
        int[] actual = collection.getTemporalLimits();

        assertArrayEquals(expected,actual);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetLargestID(VolumeType volumeType) {
        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Creating the ObjCollection
        ObjCollection collection = new ObjCollection("Obj");

        // Adding objects
        Obj obj = new Obj(volumeType,"Obj",4,1,1,1,dppXY,dppZ,calibratedUnits);
        collection.add(obj);

        obj = new Obj(volumeType,"Obj",7,1,1,1,dppXY,dppZ,calibratedUnits);
        collection.add(obj);

        obj = new Obj(volumeType,"Obj",2,1,1,1,dppXY,dppZ,calibratedUnits);
        collection.add(obj);

        assertEquals(7,collection.getLargestID());

    }

    @Test @Disabled
    public void testConvertObjectsToImageSingleColour() {
    }

    @Test @Disabled
    public void testConvertObjectsToImageSingleColourNoTemplateImage() {
    }

    @Test @Disabled
    public void testConvertObjectsToImageRandomColour() {
    }

    @Test @Disabled
    public void testConvertObjectsToImageMeasurementColour() {
    }

    @Test @Disabled
    public void testConvertObjectsToImageIDColour() {
    }

    @Test @Disabled
    public void testConvertObjectsToImageParentIDColour() {
    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetByEquals(VolumeType volumeType) throws IntegerOverflowException {
        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Creating the ObjCollection
        ObjCollection collection = new ObjCollection("Obj");

        // Adding objects
        Obj obj1 = new Obj(volumeType,"Obj",1,10,4,12,dppXY,dppZ,calibratedUnits);
        obj1.add(3,2,2);
        obj1.add(2,2,9);
        collection.add(obj1);

        Obj obj2 = new Obj(volumeType,"Obj",0,10,4,12,dppXY,dppZ,calibratedUnits);
        obj2.add(3,2,2);
        obj2.add(2,2,9);
        obj2.add(3,1,6);
        obj2.add(2,2,8);
        collection.add(obj2);

        Obj obj3 = new Obj(volumeType,"Obj",2,10,4,12,dppXY,dppZ,calibratedUnits);
        obj3.add(4,1,2);
        obj3.add(6,2,10);
        collection.add(obj3);

        Obj oj4 = new Obj(volumeType,"Obj",2,10,4,12,dppXY,dppZ,calibratedUnits);
        oj4.add(4,1,2);
        oj4.add(6,2,10);
        oj4.add(3,2,2);
        oj4.add(2,2,9);
        collection.add(oj4);

        // Creating a test object with the same coordinates as one of the other objects
        Obj testObj = new Obj(volumeType,"Obj",5,10,4,12,dppXY,dppZ,calibratedUnits);
        testObj.add(3,1,6);
        testObj.add(2,2,8);
        testObj.add(3,2,2);
        testObj.add(2,2,9);
        collection.add(testObj);

        Obj actual = collection.getByEquals(testObj);

        assertEquals(obj2,actual);
        assertEquals(obj2.getID(),actual.getID());

    }
}