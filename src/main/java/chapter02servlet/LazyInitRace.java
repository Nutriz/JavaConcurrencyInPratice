package chapter02servlet;

import net.jcip.annotations.NotThreadSafe;

/**
 * Work perfectly in a single thread environment.
 * In multithreaded environment, we can get multiple instances of the singleton, that is very bad.
 * *
 * Why ? That happens if two or more threads enter getInstance() at the same time, or before ExpensiveObject as been
 * instanced and saved in instance variable. So the second thread check instance, instance is still null,
 * so start to create a new instance of ExpensiveObject
 * It's a common race condition called check-then-act.
 * <p>
 * Thread A  --------> instance == null --------> new ExpensiveObject() ---------> instance = ExpensiveObject -------->
 * Thread B  -------------------------------------------------> instance == null --------> new ExpensiveObject() --------> instance = ExpensiveObject -------->
 */
@NotThreadSafe
public class LazyInitRace {
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null)
            instance = new ExpensiveObject();
        return instance;
    }
}

class ExpensiveObject {
    public ExpensiveObject() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

