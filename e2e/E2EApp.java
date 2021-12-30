import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

class E2EApp {

    private static final Logger logger = LoggerFactory.getLogger(E2EApp.class);

    public static void main(String[] paramArrayOfString) {
        while (true) {
            unstructuredPrimitives();
            structuredPrimitives();

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
            }
        }
    }

    public static void unstructuredPrimitives() {
        int i = ThreadLocalRandom.current().nextInt();
        char c = (char) (ThreadLocalRandom.current().nextInt(26) + 'a');
        String s = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
        float f = ThreadLocalRandom.current().nextFloat();
        long max = ThreadLocalRandom.current().nextLong();
        byte b = (byte) ThreadLocalRandom.current().nextInt(Byte.MAX_VALUE);
        short sh = (short) ThreadLocalRandom.current().nextInt(Short.MAX_VALUE);
        double d = ThreadLocalRandom.current().nextDouble();
        boolean bool = ThreadLocalRandom.current().nextBoolean();
        logger.info(
                "i=" + i + ", c=" + c + ", s=" + s + ", f=" + f + ", max=" + max + ", b=" + b + ", sh=" + sh + ", d=" + d + ", bool=" + bool
        );
    }

    public static void structuredPrimitives() {
        int i = ThreadLocalRandom.current().nextInt();
        char c = (char) (ThreadLocalRandom.current().nextInt(26) + 'a');
        String s = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
        float f = ThreadLocalRandom.current().nextFloat();
        long max = ThreadLocalRandom.current().nextLong();
        byte b = (byte) ThreadLocalRandom.current().nextInt(Byte.MAX_VALUE);
        short sh = (short) ThreadLocalRandom.current().nextInt(Short.MAX_VALUE);
        double d = ThreadLocalRandom.current().nextDouble();
        boolean bool = ThreadLocalRandom.current().nextBoolean();
        logger.info(
                "i={}, c={}, s={}, f={}, max={}, b={}, sh={}, d={}, bool={}",
                i, c, s, f, max, b, sh, d, bool
        );
    }
}
