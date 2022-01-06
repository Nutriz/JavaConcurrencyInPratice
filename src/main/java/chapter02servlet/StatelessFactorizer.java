package chapter02servlet;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * StatelessFactorizer
 * Work perfectly in a single thread environment.
 * Work perfectly in a multithreaded environment too.
 * Why ? Because they are no mutable state shared, only parameters and local variables.
 */
@ThreadSafe
public class StatelessFactorizer {

    public String service(String request) {
        BigInteger i = extractFromRequest(request);
        if (i != null) {
            BigInteger[] factors = factor(i);
            return encodeResponse(factors);
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