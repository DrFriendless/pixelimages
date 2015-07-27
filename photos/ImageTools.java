package photos;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by john on 24/07/15.
 */
public class ImageTools {
    static final ExecutorService executor = Executors.newFixedThreadPool(7);

    static BufferedImage readImage(Path file) {
        try {
            return javax.imageio.ImageIO.read(file.toFile());
        } catch (Throwable ex) {
            throw new RuntimeException("Error reading " + file.toAbsolutePath(), ex);
        }
    }

    static double multidimensionalEuclideanDistance(int[] sig1, int[] sig2) {
        double tot = 0;
        for (int i=0; i<sig1.length; i++) {
            tot += diffSquare(sig1[i], sig2[i]);
        }
        return Math.sqrt(tot);
    }

    static double euclideanDistance(int[] sig1, int[] sig2) {
        double tot = 0.0;
        int i = 0;
        while (i < sig1.length) {
            double t = 0.0;
            t += diffSquare(sig1[i], sig2[i]);
            i++;
            t += diffSquare(sig1[i], sig2[i]);
            i++;
            t += diffSquare(sig1[i], sig2[i]);
            i++;
            tot += Math.sqrt(t);
        }
        return tot;
    }

    static int diff(int i1, int i2) {
        return Math.abs(i1-i2);
    }

    static int diffSquare(int i1, int i2) {
        int d = i1 - i2;
        return d*d;
    }

    static double orthogonalDistance(int[] sig1, int[] sig2) {
        double tot = 0.0;
        for (int i=0; i<sig1.length; i++) {
            tot += diff(sig1[i], sig2[i]);
        }
        return tot;
    }

    static double weightedEuclideanDistance(int[] sig1, int[] sig2, int weight0, int weight1, int weight2) {
        double tot = 0.0;
        int i = 0;
        while (i < sig1.length) {
            double t = 0.0;
            t += diffSquare(sig1[i], sig2[i]) * weight0;
            i++;
            t += diffSquare(sig1[i], sig2[i]) * weight1;
            i++;
            t += diffSquare(sig1[i], sig2[i]) * weight2;
            i++;
            tot += Math.sqrt(t);
        }
        return tot;
    }

    static void rgbToHsbBytes(int r, int g, int b, int[] result) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(r, g, b, hsv);
        result[0] = (int) (hsv[0] * 255.9999f);
        result[1] = (int) (hsv[1] * 255.9999f);
        result[2] = (int) (hsv[2] * 255.9999f);
    }

    static BufferedImage miniaturise(BufferedImage image) {
        BufferedImage small = new BufferedImage(12, 12, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = small.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(12.0 / image.getWidth(), 12.0 / image.getHeight());
        graphics.drawRenderedImage(image, at);
        return small;
    }

    static void buildHSBSignature(BufferedImage image, int[] bytes) {
        BufferedImage small = miniaturise(image);
        int index = 0;
        int[] hsv = new int[3];
        List<Integer> rgbs = new ArrayList<>();
        for (int x=0; x<12; x++) {
            for (int y=0; y<12; y++) {
                int rgb = small.getRGB(x, y);
                rgbs.add(rgb);
                int r = rgb & 0xFF;
                int g = (rgb & 0xFF00) >> 8;
                int b = (rgb & 0xFF0000) >> 16;
                rgbToHsbBytes(r, g, b, hsv);
                bytes[index++] = hsv[0];
                bytes[index++] = hsv[1];
                bytes[index++] = hsv[2];
            }
        }
    }

    static void buildYCbCrSignature(BufferedImage image, int[] bytes) {
        BufferedImage small = miniaturise(image);
        int index = 0;
        int[] ycbcr = new int[3];
        for (int x=0; x<12; x++) {
            for (int y=0; y<12; y++) {
                int rgb = small.getRGB(x, y);
                int r = (rgb & 0xFF);
                int g = ((rgb & 0xFF00) >> 8);
                int b = ((rgb & 0xFF0000) >> 16);
                fromRGB(r, g, b, ycbcr);
                bytes[index++] = ycbcr[0];
                bytes[index++] = ycbcr[1];
                bytes[index++] = ycbcr[2];
            }
        }
    }

    private static void fromRGB(int r, int g, int b, int[] yCbCr) {
        int y = 16 + ((int) ( 65.738 * r + 129.057 * g + 25.064 * b  )) >> 8;
        int Cb = 128 + ((int) (-37.945 * r + -74.494 * g + 112.439 * b  )) >> 8;
        int Cr = 128 + ((int) (112.439 * r + -97.154 * g + -18.285 * b  )) >> 8;
        if (y > 255) {
            yCbCr[0] = 255;
        } else if (y < 0) {
            yCbCr[0] = 0;
        } else {
            yCbCr[0] = y;
        }
        if (Cb > 255) {
            yCbCr[1] = 255;
        } else if (Cb < 0) {
            yCbCr[1] = 0;
        } else {
            yCbCr[1] = Cb;
        }
        if (Cr > 255) {
            yCbCr[2] = 255;
        } else if (Cr < 0) {
            yCbCr[2] = 0;
        } else {
            yCbCr[2] = Cr;
        }
    }

    static void buildRGBSignature(BufferedImage image, int[] bytes) {
        BufferedImage small = miniaturise(image);
        int index = 0;
        for (int x=0; x<12; x++) {
            for (int y=0; y<12; y++) {
                int rgb = small.getRGB(x, y);
                bytes[index++] = rgb & 0xFF;
                bytes[index++] = (rgb & 0xFF00) >> 8;
                bytes[index++] = (rgb & 0xFF0000) >> 16;
            }
        }
    }
}
