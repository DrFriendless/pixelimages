package photos;

/**
 * Created by john on 24/07/15.
 */
public class EvaluatedPixelImage {
    private double score;
    private PixelImage image;

    EvaluatedPixelImage(PixelImage image, double score) {
        this.image = image;
        this.score = score;
    }

    double getScore() {
        return score;
    }

    PixelImage getImage() {
        return image;
    }

    public String toString() {
        return image.getPath().toString();
    }
}
