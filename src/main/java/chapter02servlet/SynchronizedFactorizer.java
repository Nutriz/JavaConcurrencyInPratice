package chapter02servlet;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Work perfectly in a single thread environment.
 * Work in a multithreaded environment too, but with poor concurrency.
 * <p>
 * Why ? With synchronized for the entire method, we blocked the performance improvement that can be got with multithreading.
 * Worth, synchronized disable some jvm/runtime optimization, so it will longer to execute that in a multithreading
 * environment than in a single threaded one.
 * Single thread: 140-160ms
 * multi thread: 305-150ms
 * <p>
 * lastNumber = 5
 * lastFactors = factor of 5
 * <p>
 * Thread A  ----> service() ---------------> service() ---------------> service() ---------------> service()
 * Thread B  -----------------> service() ---------------> service() ---------------> service() ------------>
 */
@ThreadSafe
public class SynchronizedFactorizer {

    private BigInteger lastNumber;
    private BigInteger[] lastFactors;

    public synchronized String service(String request) {
        BigInteger i = extractFromRequest(request);
        if (i != null) {
            if (i.equals(lastNumber)) {
                return encodeResponse(lastFactors);
            } else {
                BigInteger[] factors = factor(i);
                lastNumber = i;
                lastFactors = factors;
                return encodeResponse(factors);
            }
        }
        return "";
    }

    private String encodeResponse(BigInteger[] factors) {
        StringJoiner joiner = new StringJoiner(",");
        for (BigInteger factor : factors) {
            joiner.add(factor.toString());
        }
        return joiner.toString();
    }

    private BigInteger extractFromRequest(String req) {
        if (req != null && req.contains("value=")) {
            String value = req.split("=")[1];
            return new BigInteger(value);
        }
        return null;
    }

    private BigInteger[] factor(BigInteger number) {
        List<BigInteger> factors = new ArrayList<>();
        for (int i = 1; i <= number.longValue(); ++i) {
            if (number.longValue() % i == 0) {
                factors.add(new BigInteger(String.valueOf(i)));
            }
        }
        return factors.toArray(new BigInteger[0]);
    }
}