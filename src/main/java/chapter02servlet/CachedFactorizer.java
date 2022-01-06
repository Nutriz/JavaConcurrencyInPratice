package chapter02servlet;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


@ThreadSafe
public class CachedFactorizer {

    private BigInteger lastNumber;
    private BigInteger[] lastFactors;

    public String service(String request) {
        BigInteger i = extractFromRequest(request);
        BigInteger[] factors = null;
        if (i != null) {
            synchronized (this) {
                if (i.equals(lastNumber)) {
                    factors = lastFactors.clone();
                }
            }
            if (factors == null) {
                factors = factor(i);
                synchronized (this) {
                    lastNumber = i;
                    lastFactors = factors.clone();
                }
            }
        }
        return encodeResponse(factors);
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