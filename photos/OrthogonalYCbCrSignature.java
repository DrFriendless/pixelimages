package photos;

import java.awt.image.BufferedImage;

/**
 * Created by john on 26/07/15.
 */
public class OrthogonalYCbCrSignature extends Signature<OrthogonalYCbCrSignature> {
    public OrthogonalYCbCrSignature(BufferedImage image) {
        ImageTools.buildYCbCrSignature(image, bytes);
    }

    public double distance(OrthogonalYCbCrSignature sig) {
        return ImageTools.orthogonalDistance(bytes, sig.bytes);
    }
}
