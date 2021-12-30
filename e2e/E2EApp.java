import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class E2EApp {

    private static final Logger logger = LoggerFactory.getLogger(E2EApp.class);

    public static void main(String[] paramArrayOfString) {
        while (true) {
            primitiveLocalVariables();

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
            }
        }
    }

    public static void primitiveLocalVariables() {
        int i = 1;
        char c = 'h';
        String s = "hi";
        float f = 1.0f;
        long max = Long.MAX_VALUE;
        byte b = -2;
        short sh = Short.MIN_VALUE;
        double d = 00.23d;
        boolean bool = true;
        logger.info(
                "i={}, c={}, s={}, f={}, max={}, b={}, sh={}, d={}, bool={}",
                i, c, s, f, max, b, sh, d, bool
        );
    }
}
