package photos;

import java.awt.image.BufferedImage;

/**
 * Created by john on 25/07/15.
 */
public class OrthogonalRGBSignature extends Signature<OrthogonalRGBSignature> {
    public OrthogonalRGBSignature(BufferedImage image) {
        ImageTools.buildRGBSignature(image, bytes);
    }

    public double distance(OrthogonalRGBSignature sig) {
        return ImageTools.orthogonalDistance(bytes, sig.bytes);
    }
}

