package photos;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.concurrent.Future;

/**
 * Created by john on 24/07/15.
 */
class PixelImage {
    private Future<Signature> signature;
    private Path path;
    private boolean broken = false;

    PixelImage(Path p, SignatureFactory fac) {
        this.path = p;
        BufferedImage bi = ImageTools.readImage(p);
        this.signature = ImageTools.executor.submit(() -> fac.createSignature(bi));
    }

    Path getPath() {
        return path;
    }

    double distance(Signature sig) {
        if (broken) return Double.MAX_VALUE;
        try {
            return signature.get().distance(sig);
        } catch (Exception ex) {
            if (!broken) {
                ex.printStackTrace();
                broken = true;
            }
            return Double.MAX_VALUE;
        }
    }

    public String toString() {
        return getPath().toString();
    }
}
