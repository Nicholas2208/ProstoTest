package com.nwhite;

import java.util.Queue;

/**
 * A consumer that retrieves messages from a queue at a specified rate.
 */
public class Consumer implements Runnable {
    private final Queue<Integer> queue;
    private final int messagesPerMinute;

    /**
     * Constructs a new Consumer with the specified queue and messages per minute rate.
     *
     * @param queue             the queue to retrieve messages from
     * @param messagesPerMinute the rate at which messages should be consumed
     */
    public Consumer(Queue<Integer> queue, int messagesPerMinute) {
        this.messagesPerMinute = messagesPerMinute;
        this.queue = queue;
    }

    /**
     * Retrieves messages from the queue at the specified rate.
     */
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
