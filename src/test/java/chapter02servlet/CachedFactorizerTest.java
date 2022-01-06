package chapter02servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CachedFactorizerTest {

    private static final int REPEAT = 1000;
    private CachedFactorizer servletUnderTest;
    private List<String> factorTarget;

    @BeforeEach
    void setUp() {
        servletUnderTest = new CachedFactorizer();
        factorTarget = List.of("value=55555", "value=55555", "value=3", "value=3", "value=12345678", "value=12345678", "value=222221", "value=222221");
    }

    @RepeatedTest(REPEAT)
    void singleThread() {
        List<String> responses = new ArrayList<>();
        Thread thread1 = new Thread(() -> {
            for (String factor : factorTarget) {
                String response = servletUnderTest.service(factor);
                responses.add(response);
            }
        });
        thread1.start();
        try {
            thread1.join();
            int sum = sumFactors(responses);
            assertEquals(55223432, sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RepeatedTest(REPEAT)
    public void multiThreadThreadPool() {
        ExecutorService executor = Executors.newFixedThreadPool(Common.THREAD_COUNT_FOR_PARALLEL);
        List<String> responses = new ArrayList<>();

        for (String factor : factorTarget) {
            executor.execute(() -> {
                String response = servletUnderTest.service(factor);
                responses.add(response);
            });
        }

        try {
            executor.shutdown();
            boolean success = executor.awaitTermination(60_000, TimeUnit.MILLISECONDS);
            assertTrue(success);
            int sum = sumFactors(responses);
            assertEquals(55223432, sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int sumFactors(List<String> responses) {
        int sum = 0;
        for (String response : responses) {
            List<Integer> collect = Arrays.stream(response.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            for (Integer integer : collect) {
                sum += integer;
            }
        }
        return sum;
    }
}