package photos;

/**
 * Very small version of an image, used to quickly decide how close images are to other images.
 *
 * Created by IntelliJ IDEA.
 * User: john
 * Date: 30/12/11
 * Time: 08:42
 * To change this template use File | Settings | File Templates.
 */
abstract class Signature<T extends Signature> {
    protected int[] bytes = new int[432];

    abstract double distance(T sig);
}
