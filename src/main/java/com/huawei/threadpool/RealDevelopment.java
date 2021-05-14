package com.huawei.threadpool;

import java.awt.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RealDevelopment {
    public static void main(String[] args) {
        //并发编程，多线程操作同一资源，将资源类丢入线程,解耦
        Tickets tickets = new Tickets();


        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                //如果是对sale方法加锁的话则执行完sale方法该线程就会释放锁，其他线程才有可能执行sale
                //如果锁的是class则，如果该线程执行sale方法之后释放了锁但是其他线程执行该方法由于锁的是class的所有对象，所以其他线程
                //仍然不能执行
                tickets.sale();
            }
        },"a").start();
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                tickets.sale();
            }
        }, "b").start();
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                tickets.sale();
            }
        }, "c").start();

    }

}

//线程就是一个资源类，没有任何附属操作，属性和方法,OOP编程
class Tickets{
    private int ticketNum = 50;
    //Lock lock = new ReentrantLock();

    //买票的方法
    public  void sale(){
        synchronized (Tickets.class){
            if (ticketNum > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出了" + (ticketNum--));
            }
        }

    }
}


