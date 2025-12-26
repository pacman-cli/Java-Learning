package com.pacman.thread;

public class RunnableExample {
    public static void main(String[] args) {
        Runnable task1 = () -> System.out.println("Hello from a Runnable "+ Thread.currentThread().getName());

        Thread t1 = new Thread(task1);
        t1.start();
    }

}
