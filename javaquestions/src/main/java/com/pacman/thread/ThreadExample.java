package com.pacman.thread;
class MyThread extends Thread {
    public void run() {
        System.out.println("MyThread is running "+ Thread.currentThread().getName());
    }
}

public class ThreadExample {
    public static void main(String[] args) {
        MyThread thread =new MyThread();
        thread.start();
    }
}
