package chapter01sequence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SafeSequenceAtomicIntTest {

    private SafeSequenceAtomicInt sequenceUnderTest;

    @BeforeEach
    void setUp() {
        sequenceUnderTest = new SafeSequenceAtomicInt();
    }

    @RepeatedTest(Common.REPEAT_TEST_COUNT)
    void singleThread() {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Common.GOAL; i++) {
                int value = sequenceUnderTest.getNext();
            }
        });
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(Common.GOAL, sequenceUnderTest.getNext());
    }

    @RepeatedTest(Common.REPEAT_TEST_COUNT)
    public void multiThread() {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Common.GOAL / 2; i++) {
                int value = sequenceUnderTest.getNext();
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Common.GOAL / 2; i++) {
                int value = sequenceUnderTest.getNext();
            }
        });
        thread2.start();

        try {
            thread1.join();
            thread2.join();
            Assertions.assertEquals(Common.GOAL, sequenceUnderTest.getNext());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RepeatedTest(Common.REPEAT_TEST_COUNT)
    public void multiThreadThreadPool() {
        ExecutorService executor = Executors.newFixedThreadPool(Common.THREAD_COUNT_FOR_PARALLEL);

        for (int j = 0; j < 10; j++) {
            executor.execute(() -> {
                for (int i = 0; i < Common.GOAL / 10; i++) {
                    int value = sequenceUnderTest.getNext();
                }
            });
        }

        try {
            executor.shutdown();
            boolean success = executor.awaitTermination(10_000, TimeUnit.MILLISECONDS);
            Assertions.assertEquals(Common.GOAL, sequenceUnderTest.getNext());
            assertTrue(success);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}