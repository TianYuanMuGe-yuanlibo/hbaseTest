package com.huawei.threadpool;

import java.util.ArrayList;

public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        //使用list保持常量池引用，避免Full GC 回收常量池行为
        ArrayList<String> strings = new ArrayList<>();
        int i = 0;
        while (true) {
            strings.add(String.valueOf(i++).intern());
        }
    }
}
