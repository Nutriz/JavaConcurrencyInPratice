package chapter02servlet;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Work perfectly in a single thread environment.
 * Work perfectly in a multithreaded environment too.
 * <p>
 * Why ? Because an AtomicLong transform the ++count 3 operations (READ/ADD/WRITE) to one single atomic (indivisible)
 * operation. Also, getCount() use AtomicLong to return the count, if a thread is incrementing count and another thread call
 * getCount(), the thread will be locked by get() method of AtomicLong until the other thread has finished incrementing
 * the count.
 * <p>
 * Thread A  ---------> enter service() ---------> start increment -----------------------------> end increment ----------> exit service()
 * Thread B  -------------------------------------------> getCount() ---> get() block  ---> get() block ---------> get() return count --------->
 */
@ThreadSafe
public class SafeCountingFactorizer {

    private final AtomicLong count = new AtomicLong(0);

    public long getCount() {
        return count.get();
    }

    public String service(String request) {
        BigInteger i = extractFromRequest(request);
        if (i != null) {
            BigInteger[] factors = factor(i);
            String response = encodeResponse(factors);
            count.incrementAndGet();
            return response;
        }
        return "";
    }

    private String encodeResponse(BigInteger[] factors) {
        StringJoiner joiner = new StringJoiner(",", "Values: ", "");
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