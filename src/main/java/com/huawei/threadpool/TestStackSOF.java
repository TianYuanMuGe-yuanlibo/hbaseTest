package com.huawei.threadpool;

public class TestStackSOF {
    private int stackLength = 1;
    private void stackLeak(){
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        TestStackSOF testStackSOF = new TestStackSOF();
        try {
            testStackSOF.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length" + testStackSOF.stackLength);
            throw e;
        }

    }
}
