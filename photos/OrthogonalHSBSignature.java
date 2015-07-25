package photos;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by john on 25/07/15.
 */
public class OrthogonalHSBSignature extends Signature<OrthogonalHSBSignature> {
    private float[] bytes = new float[432];

    public OrthogonalHSBSignature(BufferedImage image) {
        BufferedImage small = new BufferedImage(12, 12, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = small.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(12.0 / image.getWidth(), 12.0 / image.getHeight());
        graphics.drawRenderedImage(image, at);
        int index = 0;
        float[] hsv = new float[3];
        for (int x=0; x<12; x++) {
            for (int y=0; y<12; y++) {
                int rgb = small.getRGB(x, y);
                byte r = (byte) (rgb & 0xFF);
                byte g = (byte) ((rgb & 0xFF00) >> 8);
                byte b = (byte) ((rgb & 0xFF0000) >> 16);
                Color.RGBtoHSB(r,g,b,hsv);
                bytes[index++] = hsv[0];
                bytes[index++] = hsv[1];
                bytes[index++] = hsv[2];
            }
        }
    }

    float distance(OrthogonalHSBSignature sig) {
        float tot = 0;
        for (int i=0; i<bytes.length; i++) {
            tot += Math.abs(bytes[i] - sig.bytes[i]);
        }
        return (float) Math.sqrt(tot);
    }
}
