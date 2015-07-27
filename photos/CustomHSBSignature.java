package photos;

import java.awt.image.BufferedImage;

/**
 * Created by john on 25/07/15.
 */
public class CustomHSBSignature extends Signature<CustomHSBSignature> {
    public CustomHSBSignature(BufferedImage image) {
        ImageTools.buildHSBSignature(image, bytes);
    }

    double distance(CustomHSBSignature sig) {
        return ImageTools.weightedEuclideanDistance(bytes, sig.bytes, 2, 1, 1);
    }
}
