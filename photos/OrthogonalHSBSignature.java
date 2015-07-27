package photos;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Created by john on 25/07/15.
 */
public class OrthogonalHSBSignature extends Signature<OrthogonalHSBSignature> {
    public OrthogonalHSBSignature(BufferedImage image) {
        ImageTools.buildHSBSignature(image, bytes);
    }

    double distance(OrthogonalHSBSignature sig) {
        return ImageTools.orthogonalDistance(bytes, sig.bytes);
    }
}
