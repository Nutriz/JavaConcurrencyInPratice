package utils;

public class Utils {

    public static void log(String text) {
        System.out.println("Thread " + Thread.currentThread().getName() + ": " + text);
    }
}
