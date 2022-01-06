package chapter01sequence;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class UnsafeSequence {
    private int value;

    /**
     * Work perfectly in a single thread environment.
     * In multithreaded environment, we can get bad sequences if two (or more) threads access the
     * method at the same time, it's a common concurrency hazard called "race condition".
     * Why ? Because the increment notation (value++) appearing as a single operation, is under the hood three operations:
     * - Read current value
     * - Add one to it
     * - Write the new value
     * <p>
     * READ               ADD                WRITE
     * Thread A  ---------> value=9 ---------> 9+1=10 ---------> value=10 ----------
     * Thread B  ------------------> value=9 ---------> 9+1=10 ---------> value=10 ---
     * <p>
     * In Thread B, when we access value, it is still 9 when we except to have 10 to continue the sequence.
     *
     * @return the next value
     * @see {@link SafeSequenceSynchronizedMethod } or {@link SafeSequenceAtomicInt } for a safe thread version
     */
    public int getNext() {
        return value++;
    }
}
