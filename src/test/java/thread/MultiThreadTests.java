package thread;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class MultiThreadTests {

    @Test
    public void testForeach(){
        List<Boolean> arrayList = new ArrayList<>(100000);
        for (int i=0; i<100000; i++){
            arrayList.add(true);
        }

        List<Boolean> linkedList = new LinkedList<>(arrayList);

        long start = System.nanoTime();
        for (int i=0; i<arrayList.size(); i++){
            arrayList.get(i);
        }
        System.out.println(Duration.ofNanos(System.nanoTime() - start).toNanos());

        start = System.nanoTime();
        for (Boolean b: arrayList){
        }
        System.out.println(Duration.ofNanos(System.nanoTime() - start).toNanos());

        start = System.nanoTime();
        for (int i=0; i<linkedList.size(); i++){
            linkedList.get(i);
        }
        System.out.println(Duration.ofNanos(System.nanoTime() - start).toNanos());

        start = System.nanoTime();
        linkedList.forEach(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
            }
        });
        System.out.println(Duration.ofNanos(System.nanoTime() - start).toNanos());

        start = System.nanoTime();
        for (Boolean b: linkedList){
        }
        System.out.println(Duration.ofNanos(System.nanoTime() - start).toNanos());
    }


    @Test
    public void testThreadLocal(){
        ThreadLocal<Integer> b = new ThreadLocal<>();
        for (int i=0; i<100; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    b.set(new Random().nextInt(10));
                    System.out.printf("%d ", b.get());
                }
            }).start();
        }
    }
}
