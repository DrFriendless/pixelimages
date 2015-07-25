package photos;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * Created by john on 24/07/15.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Path mainImagePath = Paths.get(args[0]);
        Path imageDir = Paths.get(args[1]);
        int xImages = Integer.parseInt(args[2]);
        int yImages = Integer.parseInt(args[3]);
        int resultWidth = Integer.parseInt(args[4]);
        if (!imageDir.toFile().isDirectory()) {
            System.out.println(imageDir.toString() + " is not a directory.");
            return;
        }
        BufferedImage mainImage = ImageTools.readImage(mainImagePath);
        // maintain the aspect ratio
        int resultHeight = resultWidth * mainImage.getHeight() / mainImage.getWidth();
//        process(mainImage, xImages, yImages, resultWidth, resultHeight, mainImagePath,
//                new SignatureFactory(OrthogonalRGBSignature.class), "orthrgb", imageDir);
//        process(mainImage, xImages, yImages, resultWidth, resultHeight, mainImagePath,
//                new SignatureFactory(OrthogonalHSBSignature.class), "orthhsb", imageDir);
//        process(mainImage, xImages, yImages, resultWidth, resultHeight, mainImagePath,
//                new SignatureFactory(EuclideanRGBSignature.class), "eucrgb", imageDir);
//        process(mainImage, xImages, yImages, resultWidth, resultHeight, mainImagePath,
//                new SignatureFactory(EuclideanHSBSignature.class), "euchsb", imageDir);
        process(mainImage, xImages, yImages, resultWidth, resultHeight, mainImagePath,
                new SignatureFactory(CustomHSBSignature.class), "custhsb", imageDir);
        System.exit(0);
    }

    private static void process(BufferedImage mainImage, int xImages, int yImages, int resultWidth, int resultHeight,
                                Path mainImagePath, SignatureFactory fac, String nameKey, Path imageDir) throws IOException {
        // load the pixel images
        Set<PixelImage> pixelImages = new HashSet<>();
        System.out.println("Loading...");
        try (DirectoryStream<Path> images = Files.newDirectoryStream(imageDir, p -> !p.toFile().isDirectory())) {
            images.forEach(p -> pixelImages.add(new PixelImage(p, fac)));
        }
        System.out.println("There are " + pixelImages.size() + " pixel images.");
        List<Region> regions = calculate(mainImage, xImages, yImages, resultWidth, resultHeight, fac, pixelImages);
        BufferedImage result = compose(regions, resultWidth, resultHeight);
        Path resultPath = Paths.get(mainImagePath.getParent().toString(), "result_" + nameKey + ".jpg");
        ImageIO.write(result, "jpg", resultPath.toFile());
    }

    private static List<Region> calculate(BufferedImage src, int xImages, int yImages, int resultWidth, int resultHeight,
                                          SignatureFactory fac, Set<PixelImage> pixelImages) {
        System.out.println("Calculating...");
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        List<Region> result = new ArrayList<>();
        for (int x=0; x<xImages; x++) {
            for (int y=0; y<yImages; y++) {
                int xc = x * srcWidth / xImages;
                int xc1 = (x + 1) * srcWidth / xImages;
                int yc = y * srcHeight / yImages;
                int yc1 = (y + 1) * srcHeight / yImages;
                BufferedImage sample = src.getSubimage(xc, yc, xc1-xc, yc1-yc);
                Signature sampleSig = fac.createSignature(sample);
                Optional<EvaluatedPixelImage> best = pixelImages.
                        parallelStream().
                        map(img -> new EvaluatedPixelImage(img, img.distance(sampleSig))).
                        min((i1, i2) -> Float.compare(i1.getScore(), i2.getScore()));
                if (best.isPresent()) {
                    int dx = x * resultWidth / xImages;
                    int dx1 = (x + 1) * resultWidth / xImages;
                    int dy = y * resultHeight / yImages;
                    int dy1 = (y + 1) * resultHeight / yImages;
                    result.add(new Region(best.get().getImage().getPath(), dx, dy, dx1 - dx, dy1 - dy));
                }
            }
        }
        return result;
    }

    private static BufferedImage getFromCache(Map<Path, BufferedImage> cache, Path p) {
        if (!cache.containsKey(p)) {
            cache.put(p, ImageTools.readImage(p));
        }
        return cache.get(p);
    }

    private static BufferedImage compose(List<Region> regions, int resultWidth, int resultHeight) {
        System.out.println("Composing...");
        Map<Path, BufferedImage> cache = new HashMap<>();
        BufferedImage result = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_RGB);
        Graphics resultGraphics = result.getGraphics();
        for (Region region : regions) {
            BufferedImage bi = getFromCache(cache, region.path);
            resultGraphics.drawImage(bi, region.x, region.y, region.w, region.h, null);

        }
        return result;
    }
}
