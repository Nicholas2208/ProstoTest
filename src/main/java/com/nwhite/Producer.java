package com.nwhite;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Producer implements Runnable {
    private final Semaphore rateLimiter;
    private final Queue<Integer> queue;
    private final int messagesPerMinute;

    public Producer(Queue<Integer> queue, int messagesPerMinute) {
        this.messagesPerMinute = messagesPerMinute;
        this.queue = queue;
        this.rateLimiter = new Semaphore(messagesPerMinute);
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            try {
                int message = random.nextInt(Integer.MAX_VALUE);
                rateLimiter.acquire();
               if (queue.offer(message)) {
                   System.out.println("Produced " + message);
                }
                Thread.sleep(60000 / messagesPerMinute);
                rateLimiter.release();
            } catch (InterruptedException e) {
                System.err.println("Поток был прерван.");
                // TODO Реализовать логику на случай прерывания потока.
            }
        }
    }
}
