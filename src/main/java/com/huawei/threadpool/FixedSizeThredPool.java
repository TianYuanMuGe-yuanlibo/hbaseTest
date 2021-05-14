package com.huawei.threadpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class FixedSizeThredPool {
    //队列相当于是仓库存放任务
    private BlockingQueue<Runnable> taskQueue;
    //工作线程
    private List<Thread> workers;

    public FixedSizeThredPool(int poolSize, int taskQueueSize) {
        if (poolSize <= 0 || taskQueueSize <= 0) {
            throw new IllegalArgumentException("the input arguments are illegal");
        }
        taskQueue = new LinkedBlockingDeque<>(taskQueueSize);
        this.workers = Collections.synchronizedList(new ArrayList<>());
        //初始化就开始创建线程
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker(this);
            worker.start();
            this.workers.add(worker);
        }
    }

    public boolean submit(Runnable runnable) {
        //此处使用offer而不是take,当队列满了之后会有返回值提醒
       return this.taskQueue.offer(runnable);
    }

    //可以阻塞一段时间的方法


    private static class Worker extends Thread{
        private FixedSizeThredPool pool;
        public Worker(FixedSizeThredPool pool) {
            super();
            this.pool = pool;
        }

        public void run() {
            int taskCount = 0;
            while (true) {
                Runnable task = null;
                try {
                    task = this.pool.taskQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task != null) {
                    task.run();
                    System.out.println("run the task" + (++taskCount) + "个任务");
                }
            }
        }
    }

    public static void main(String[] args) {
        FixedSizeThredPool fixedSizeThredPool = new FixedSizeThredPool(3, 6);
        for (int i = 0; i < 6; i++) {
            fixedSizeThredPool.submit(() -> {
                System.out.println("任务开始执行");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
