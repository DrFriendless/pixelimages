package photos;

import java.awt.image.BufferedImage;

/**
 * Created by john on 25/07/15.
 */
public class EuclideanRGBSignature extends Signature<EuclideanRGBSignature> {
    public EuclideanRGBSignature(BufferedImage image) {
        ImageTools.buildRGBSignature(image, bytes);
    }

    public double distance(EuclideanRGBSignature sig) {
        return ImageTools.euclideanDistance(bytes, sig.bytes);
    }
}

