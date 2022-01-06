package chapter01sequence;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class SafeSequenceSynchronizedMethod {
    @GuardedBy("this")
    private int value;

    /**
     * Work perfectly in a single thread environment.
     * Work perfectly in a multithreaded environment too. If a thread B try to access the method at the same time as
     * another thread A is in, thread B is block and will wait until the thread A has finished the execution of the
     * method.
     * Why ? Because the "synchronized" keyword allows only one thread to execute the method at any given time.
     * <p>
     * LOCK       READ               ADD                WRITE    UNLOCK
     * Thread A  ---------> value=9 ---------> 9+1=10 ---------> value=10 ----------
     * Thread B  -------------------------------------------------------------------> value=9 ---------> 9+1=10 ---------> value=10 ---
     * <p>
     * Thread B can't enter the method while Thread A has not finished.
     *
     * @return the next value
     */
    public synchronized int getNext() {
        return value++;
    }
}
