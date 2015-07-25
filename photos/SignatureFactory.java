package photos;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

/**
 * Create signatures of a specified type.
 *
 * Created by john on 25/07/15.
 */
public class SignatureFactory<C extends Signature> {
    private Constructor<C> constr;

    SignatureFactory(Class<C> clazz) {
        try {
            constr = clazz.getConstructor(BufferedImage.class);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    C createSignature(BufferedImage bi) {
        try {
            return constr.newInstance(bi);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
