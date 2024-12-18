package io.github.mianalysis.mia.expectedobjects;

import java.util.HashMap;
import java.util.List;

import io.github.mianalysis.mia.object.coordinates.volume.CoordinateSetFactoryI;
import ome.units.UNITS;

/**
 * Created by Stephen Cross on 10/09/2017.
 */
public class Spots3D extends ExpectedObjects {
    public Spots3D(CoordinateSetFactoryI factory) {
        super(factory, 64,76,12,1, 0.02, UNITS.SECOND);
    }

    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/Spots3D.csv");
    }

    @Override
    public HashMap<Integer, HashMap<String, Double>> getMeasurements() {
        return null;
    }
}
