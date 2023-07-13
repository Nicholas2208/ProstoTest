package com.nwhite;

import java.util.Queue;

public class Consumer implements Runnable {
    private final Queue<Integer> queue;
    private final int messagesPerMinute;

    public Consumer(Queue<Integer> queue, int messagesPerMinute) {
        this.messagesPerMinute = messagesPerMinute;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Integer value = queue.poll();
                if (value != null) {
                    System.out.println("Consumed: " + value);
                }
                Thread.sleep(60000 / messagesPerMinute);
            } catch (InterruptedException e) {
                System.err.println("Поток был прерван.");
                // TODO Реализовать логику на случай прерывания потока.
            }
        }
    }
}
