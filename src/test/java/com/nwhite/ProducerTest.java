package com.nwhite;

import org.junit.Test;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProducerTest {

    @Test
    public void testProducer() throws InterruptedException {
        Queue<Integer> queue = new ConcurrentLinkedQueue<>();
        int messagesPerMinute = 10;
        Producer producer = new Producer(queue, messagesPerMinute);

        Thread producerThread = new Thread(producer);
        producerThread.start();

        Thread.sleep(3000);

        assertEquals(messagesPerMinute / 6, queue.size());

        producerThread.interrupt();
        producerThread.join();
    }
}
