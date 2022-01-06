package chapter02servlet;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Work perfectly in a single thread environment.
 * In multithreaded environment, we can get a wrong last factor value, even if we use AtomicReference.
 * <p>
 * Why ? Because set of lastNumber and set of lastFactors is not atomic (indivisible), same for the get part.
 * So we can have one thread setting lastNumber and before setting lastFactors, other thread check lastNumber and get
 * wrong (old) lastFactors.
 * <p>
 * lastNumber = 5
 * lastFactors = factor of 5
 * <p>
 * Thread A  ---> lastNumber=7 ----------------------------> lastFactors=factor of 7 -------
 * Thread B  ----------> i=7==lastNumber ----------> return factor of 5 from cache ---------
 */
@ThreadSafe
public class UnsafeCachingFactorizer {

    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();

    public String service(String request) {
        BigInteger i = extractFromRequest(request);
        if (i != null) {
            if (i.equals(lastNumber.get())) {
                return encodeResponse(lastFactors.get());
            } else {
                BigInteger[] factors = factor(i);
                lastNumber.set(i);
                lastFactors.set(factors);
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