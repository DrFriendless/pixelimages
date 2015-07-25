package photos;

/**
 * Created by john on 24/07/15.
 */
public class EvaluatedPixelImage {
    private float score;
    private PixelImage image;

    EvaluatedPixelImage(PixelImage image, float score) {
        this.image = image;
        this.score = score;
    }

    float getScore() {
        return score;
    }

    PixelImage getImage() {
        return image;
    }
}
