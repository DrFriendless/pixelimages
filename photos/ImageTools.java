package photos;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by john on 24/07/15.
 */
public class ImageTools {
    static final ExecutorService executor = Executors.newFixedThreadPool(7);

    static Future<BufferedImage> readFutureImage(Path file) {
        return executor.submit(() -> readImage(file));
    }

    static BufferedImage readImage(Path file) {
        try {
            return javax.imageio.ImageIO.read(file.toFile());
        } catch (Throwable ex) {
            throw new RuntimeException("Error reading " + file.toAbsolutePath(), ex);
        }
    }
}
