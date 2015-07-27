package photos;

import java.awt.image.BufferedImage;

/**
 * Created by john on 26/07/15.
 */
public class EuclideanMultidimensionalRGBSignature extends Signature<EuclideanMultidimensionalRGBSignature> {
    public EuclideanMultidimensionalRGBSignature(BufferedImage image) {
        ImageTools.buildRGBSignature(image, bytes);
    }

    public double distance(EuclideanMultidimensionalRGBSignature sig) {
        return ImageTools.multidimensionalEuclideanDistance(bytes, sig.bytes);
    }
}
