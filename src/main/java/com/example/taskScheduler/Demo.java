package com.example.taskScheduler;

public class Demo {
    public static void main(String[] args) {
        InMemoryScheduler scheduler = new InMemoryScheduler(3);
        scheduler.start();

        scheduler.scheduleTask(() -> {
            System.out.println("Task 1 executed at " + System.currentTimeMillis());
        }, System.currentTimeMillis() + 2000);

        scheduler.scheduleTask(() -> {
            System.out.println("Task 2 executed at " + System.currentTimeMillis());
        }, System.currentTimeMillis() + 4000);

        scheduler.scheduleTask(() -> {
            System.out.println("Task 3 executed at " + System.currentTimeMillis());
        }, System.currentTimeMillis() + 6000);

        scheduler.scheduleTaskAtFixedInterval(() -> {
            System.out.println("Interval Task executed at " + System.currentTimeMillis());
        }, 3);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scheduler.shutDown();
    }
}
