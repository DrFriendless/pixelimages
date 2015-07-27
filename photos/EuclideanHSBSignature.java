package photos;

import java.awt.image.BufferedImage;

/**
 * Very small version of an image, used to quickly decide how close images are to other images.
 *
 * Created by IntelliJ IDEA.
 * User: john
 * Date: 30/12/11
 * Time: 08:42
 * To change this template use File | Settings | File Templates.
 */
public class EuclideanHSBSignature extends Signature<EuclideanHSBSignature> {
    public EuclideanHSBSignature(BufferedImage image) {
        ImageTools.buildHSBSignature(image, bytes);
    }

    double distance(EuclideanHSBSignature sig) {
        return ImageTools.euclideanDistance(this.bytes, sig.bytes);
    }
}
