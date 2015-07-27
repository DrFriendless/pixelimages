package photos;

import java.awt.image.BufferedImage;

/**
 * Created by john on 26/07/15.
 */
public class EuclideanYCbCrSignature extends Signature<EuclideanYCbCrSignature> {
    public EuclideanYCbCrSignature(BufferedImage image) {
        ImageTools.buildYCbCrSignature(image, bytes);
    }

    public double distance(EuclideanYCbCrSignature sig) {
        return ImageTools.euclideanDistance(bytes, sig.bytes);
    }
}
