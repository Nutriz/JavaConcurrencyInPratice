package chapter02servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class LazyInitRaceTest {

    private static final int REPEAT = 10;
    private LazyInitRace lazyUnderTest;

    @BeforeEach
    void setUp() {
        lazyUnderTest = new LazyInitRace();
    }

    @RepeatedTest(REPEAT)
    void singleThread() {
        Thread thread1 = new Thread(() -> {
            ExpensiveObject instance1 = lazyUnderTest.getInstance();
            ExpensiveObject instance2 = lazyUnderTest.getInstance();
            assertEquals(instance1, instance2);
        });
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RepeatedTest(REPEAT)
    public void multiThreadThreadPool() {
        ExecutorService executor = Executors.newFixedThreadPool(Common.THREAD_COUNT_FOR_PARALLEL);
        Set<ExpensiveObject> list = new HashSet<>();
        for (int j = 0; j < 10; j++) {
            executor.execute(() -> {
                ExpensiveObject instance = lazyUnderTest.getInstance();
                list.add(instance);
            });
        }

        try {
            executor.shutdown();
            boolean success = executor.awaitTermination(60_000, TimeUnit.MILLISECONDS);
            assertEquals(1, list.size());
            assertTrue(success);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}