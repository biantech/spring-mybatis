package com.biantech.ssmd.test.threadtest;

public class ThreadLocalDemo {
    private static Integer num = 0;
    public static void main(String[] args) {
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5;i++) {
            threads[i] = new Thread(() -> {
                num += 5;
                System.out.println(Thread.currentThread().getName() +" : " + num);
            },"Thread-" +i);
        }
        for(Thread thread:threads) {
            thread.start();
        }
    }
}

