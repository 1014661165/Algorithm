package thread;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadTests {

    static int ticket = 100;
    static Lock lock = new ReentrantLock();
    static CountDownLatch latch = new CountDownLatch(100);

    @Test
    public void testCountDownLatch() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        for (int i=0; i<100; i++){
            executorService.execute(new Processor());
        }
        latch.await();
        System.out.println("main exit");
    }

    static class Processor implements Runnable{

        @Override
        public void run() {
            if (ticket > 0){
                lock.lock();
                ticket--;
                System.out.printf("name: %s  ticket: %d\n", Thread.currentThread().getName(), ticket);
                lock.unlock();
                latch.countDown();
            }
        }
    }
}
