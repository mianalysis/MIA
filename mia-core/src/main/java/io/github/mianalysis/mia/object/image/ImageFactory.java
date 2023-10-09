package io.github.mianalysis.mia.object.image;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import ij.ImagePlus;
import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.object.system.Preferences;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class ImageFactory {
    public static <T extends RealType<T> & NativeType<T>> Image<T> createImage(String name, ImagePlus imagePlus,
            ImageType imageType) {
        switch (imageType) {
            case IMAGEPLUS:
                return new ImagePlusImage(name, imagePlus);
            case IMGLIB2:
                return new ImgPlusImage(name, imagePlus);
        }

        return null;
    }

    public static <T extends RealType<T> & NativeType<T>> Image<T> createImage(String name, ImgPlus img,
            ImageType imageType) {
        switch (imageType) {
            case IMAGEPLUS:
                return new ImagePlusImage(name, img);
            case IMGLIB2:
                return new ImgPlusImage(name, img);
        }

        return null;
    }

    public static <T extends RealType<T> & NativeType<T>> Image<T> createImage(String name, ImagePlus imagePlus) {
        // switch (MIA.getPreferences().getDataStorageMode()) {
        // case Preferences.DataStorageModes.KEEP_IN_RAM:
        // default:
        return new ImagePlusImage(name, imagePlus);
        // case Preferences.DataStorageModes.STREAM_FROM_DRIVE:
        // return new ImgPlusImage(name, imagePlus);
        // }
    }

    public static <T extends RealType<T> & NativeType<T>> Image<T> createImage(String name, ImgPlus img) {
        // switch (MIA.getPreferences().getDataStorageMode()) {
        // case Preferences.DataStorageModes.KEEP_IN_RAM:
        // default:
        return new ImagePlusImage(name, img);
        // case Preferences.DataStorageModes.STREAM_FROM_DRIVE:
        // return new ImgPlusImage(name, img);
        // }
    }
}
