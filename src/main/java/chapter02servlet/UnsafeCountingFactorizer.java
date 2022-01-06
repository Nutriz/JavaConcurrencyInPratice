package chapter02servlet;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Work perfectly in a single thread environment.
 * In multithreaded environment, we can get bad count if two (or more) threads access the
 * method at the same time, it's a common concurrency hazard called "race condition".
 * Why ? Because the increment notation (++count) appearing as a single operation, is under the hood three operations:
 * - Read current value
 * - Add one to it
 * - Write the new value
 * <p>
 * READ               ADD                WRITE
 * Thread A  ---------> value=9 ---------> 9+1=10 ---------> value=10 ----------
 * Thread B  ------------------> value=9 ---------> 9+1=10 ---------> value=10 ---
 */
@ThreadSafe
public class UnsafeCountingFactorizer {

    private long count = 0;

    public long getCount() {
        return count;
    }

    public String service(String request) {
        BigInteger i = extractFromRequest(request);
        if (i != null) {
            BigInteger[] factors = factor(i);
            String response = encodeResponse(factors);
            ++count;
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