package photos;

import java.nio.file.Path;

/**
 * Created by john on 24/07/15.
 */
public class Region {
    Path path;
    int x, y, w, h;

    Region(Path p, int x, int y, int w, int h) {
        this.path = p;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
