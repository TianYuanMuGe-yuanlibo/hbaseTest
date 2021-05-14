package com.huawei.threadpool;


import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

public class TestBiasedLock {
    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        Object o = new Object();
        String s = ClassLayout.parseInstance(o).toPrintable();
        System.out.println(s);

    }
}
