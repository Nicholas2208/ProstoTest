package com.nwhite;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * A producer that generates messages and adds them to a queue at a specified rate.
 */
public class Producer implements Runnable {
    private final Semaphore rateLimiter;
    private final Queue<Integer> queue;
    private final int messagesPerMinute;

    /**
     * Constructs a new Producer with the specified queue and messages per minute rate.
     *
     * @param queue             the queue to add messages to
     * @param messagesPerMinute the rate at which messages should be produced
     */
    public Producer(Queue<Integer> queue, int messagesPerMinute) {
        this.messagesPerMinute = messagesPerMinute;
        this.queue = queue;
        this.rateLimiter = new Semaphore(messagesPerMinute);
    }

    /**
     * Generates messages and adds them to the queue at the specified rate.
     */
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
