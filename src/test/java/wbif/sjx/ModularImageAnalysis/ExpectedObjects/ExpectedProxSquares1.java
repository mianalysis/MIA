package wbif.sjx.ModularImageAnalysis.ExpectedObjects;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sc13967 on 12/02/2018.
 */
public class ExpectedProxSquares1 extends ExpectedObjects {
    public enum Measures {};

    public HashMap<Integer,HashMap<String,Double>> getMeasurements() {
        return null;
    }

    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/ExpectedProxSquares1.csv");
    }

    @Override
    public boolean is2D() {
        return true;
    }
}