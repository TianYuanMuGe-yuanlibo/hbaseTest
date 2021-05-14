package com.huawei.threadpool;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class TestHeapOOM {
   /* ReentrantLock reentrantLock = new ReentrantLock(false);
    CountDownLatch countDownLatch = new CountDownLatch(5);
    public void Test(){
        reentrantLock.lock();
        try {

        }catch () {

        }finally {
            reentrantLock.unlock();
        }
    }
    static class OOMObject{
    }
    public static void main(String[] args) {
        ArrayList<OOMObject> oomObjects = new ArrayList<>();
        while (true){
            oomObjects.add(new OOMObject());
        }

    }*/



    public static void main(String[] args) {
        new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return null;
            }
        };
        CountDownLatch countDownLatch =  new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            final int num= i;
            new Thread(()-> {
                System.out.println(Thread.currentThread().getName() + "执行了第" + num + "次");
                countDownLatch.countDown();
            },String.valueOf(num)).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "执行");

    }
}
