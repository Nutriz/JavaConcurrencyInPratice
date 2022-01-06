package chapter01sequence;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class SafeSequenceAtomicInt {
    private final AtomicInteger value = new AtomicInteger(0);

    /**
     * Work perfectly in a single thread environment.
     * Work perfectly in a multithreaded environment too.
     * Why ? Because an AtomicInteger transform the value++ 3 operations (READ/ADD/WRITE) to one single atomic (indivisible) operation.
     * <p>
     * READ+ADD+WRITE (as an atomic operation)
     * Thread A  ---------> value=9 9+1=10 value=10 ------------------------------------------------------------>
     * Thread B  ------------------------------------> value=10 10+1=11 value=11 ------------------------------->
     *
     * @return the next value
     */
    public int getNext() {
        return value.getAndIncrement();
    }
}
