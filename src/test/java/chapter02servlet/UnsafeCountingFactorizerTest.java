package chapter02servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UnsafeCountingFactorizerTest {

    private static final int REPEAT = Common.REPEAT_TEST_COUNT;
    private final String factorTarget = "value=2202222";
    private UnsafeCountingFactorizer servletUnderTest;

    @BeforeEach
    void setUp() {
        servletUnderTest = new UnsafeCountingFactorizer();
    }

    @RepeatedTest(REPEAT)
    void singleThread() {
        Thread thread1 = new Thread(() -> {
            for (int j = 0; j < 10; j++) {
                String response = servletUnderTest.service(factorTarget);
            }
        });
        thread1.start();
        try {
            thread1.join();
            assertEquals(10, servletUnderTest.getCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RepeatedTest(REPEAT)
    public void multiThreadThreadPool() {
        ExecutorService executor = Executors.newFixedThreadPool(Common.THREAD_COUNT_FOR_PARALLEL);

        for (int j = 0; j < 10; j++) {
            executor.execute(() -> {
                String response = servletUnderTest.service(factorTarget);
            });
        }

        try {
            executor.shutdown();
            boolean success = executor.awaitTermination(60_000, TimeUnit.MILLISECONDS);
            assertEquals(10, servletUnderTest.getCount());
            assertTrue(success);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}