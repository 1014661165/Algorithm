package thread;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadTests {

    @Test
    public void testCountDownLatch() throws InterruptedException {

        int threadNum = 100;
        ExecutorService service = Executors.newFixedThreadPool(threadNum);
        final CountDownLatch latch = new CountDownLatch(threadNum);
        for (int i=0; i<threadNum; i++){
            int finalI = i;
            service.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(finalI);
                    latch.countDown();
                }
            });
        }
        latch.await();
        System.out.println(-threadNum);
    }
}
