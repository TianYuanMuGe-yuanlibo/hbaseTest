package com.huawei.threadpool;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FuctionInterface {
    public static void main(String[] args) {
        /*Supplier supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };*/
        Consumer<String> stringConsumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        /*Predicate<String> prdicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.isEmpty();
            }
        };*/
        /*Function function = new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s;
            }
        };*/
        Function function = (str) -> {return str;};
        Predicate<String> predicate = (s) -> {return s.isEmpty();};
        Consumer<String> consumer = (str) -> {
            System.out.println(str);
        };
        Supplier<String> supplier = () -> {return "Hello";};
        System.out.println(function.apply("hello"));
        System.out.println(predicate.test("hello"));
        consumer.accept("hello");
        System.out.println(supplier.get());
        ArrayList<String> strings = new ArrayList<>();
        strings.forEach((a) -> {
            System.out.println(a);
        });
    }
}
