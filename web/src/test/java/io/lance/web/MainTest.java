package io.lance.web;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Author Lance.
 * Date: 2017-09-08 17:48
 * Desc:
 */
public class MainTest {

    final static ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));

    @Test
    public void run(){

        for(int i=0;i<10;i++){
            service.execute(()->{
                System.out.println(Thread.currentThread().getId());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();

        // 如果关闭后所有任务都已完成，则返回 true。
        while (!service.isTerminated()) {
            try {
                // 用于等待子线程结束，再继续执行下面的代码
                service.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
