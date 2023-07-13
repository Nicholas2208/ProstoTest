package com.nwhite;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;

public class Main {
    public static final int INITIAL_DELAY = 5;
    public static final int PERIOD = 1; // каждую секунду

    public static void main(String[] args) {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.yml";
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int producePerMinute = Integer.parseInt(appProps.getProperty("produce-messages-per-minute"));
        float consumerLinger = Float.parseFloat(appProps.getProperty("consumer-linger"));
        float consumePerMinute = producePerMinute * consumerLinger;

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        ScheduledExecutorService producerExecutor = Executors.newSingleThreadScheduledExecutor();
        ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();
        Producer producer = new Producer(queue, producePerMinute);
        Consumer consumer = new Consumer(queue, (int) consumePerMinute);
        producerExecutor.scheduleAtFixedRate(producer, INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
        consumerExecutor.submit(consumer);

    }
}
