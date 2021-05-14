package com.huawei.threadpool;

import java.util.Arrays;
/*
* 数组实现栈
* @Param <T>
*
* */
public class MyStack<T> {
    private Object[] stack;
    private int size;

    public MyStack(Object[] stack) {
        this.stack = new Object[10];//默认大小为10
    }

    //判断是否为空
    public boolean isEmpty(){
        return size == 0;
    }

    //返回栈顶元素，但是不出栈
    public T Peek(){
        T t = null;
        if (size > 0) {
            t = (T)stack[size - 1];
            return t;
        }
        return null;
    }

    //插入数据
    public void push(T t){
        expandCapacity(size + 1);
        stack[size] = t;
        size++;
    }

    //出栈
    public T pop(){
        T t = Peek();
        if (size > 0) {
            stack[size - 1] = null;
            size--;
        }
        return t;
    }

    //扩容方法
    private void expandCapacity(int size){
        int len = stack.length;
        if (size > len) {
            size = size * 3 / 2 + 1;
            stack = Arrays.copyOf(stack, size);
        }
    }


    public static void main(String[] args) {
        Object[] objects = new Object[10];
        MyStack<String> myStack = new MyStack<>(objects);

        System.out.println(myStack.isEmpty());
        myStack.push("hello");
        myStack.push("java");
        myStack.push("spark");
        myStack.push("scala");
        System.out.println(myStack.Peek());
        System.out.println(myStack.pop());
        System.out.println(myStack.Peek());



    }


}
