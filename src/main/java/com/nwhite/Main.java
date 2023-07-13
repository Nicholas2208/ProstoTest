package com.nwhite;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;

public class Main {
    public static final int CAPACITY = 10;
    public static final int INITIAL_DELAY = 5;
    public static final int ACTION_PERIOD = 1;

    public static void main(String[] args) {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.yml";

        Properties appProps = loadAppProperties(appConfigPath);

        int producePerMinute = 0;
        float consumePerMinute = 0;

        try {
            producePerMinute = Integer.parseInt(appProps.getProperty("produce-messages-per-minute"));
            float consumerLinger = Float.parseFloat(appProps.getProperty("consumer-linger"));
            consumePerMinute = producePerMinute * consumerLinger;
        } catch (NumberFormatException e) {
            System.err.println("Ошибка при чтении числового значения из файла конфигурации.");
            // TODO: использовать значение по умолчанию
        }

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(CAPACITY);
        ScheduledExecutorService producerExecutor = Executors.newSingleThreadScheduledExecutor();
        ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();
        Producer producer = new Producer(queue, producePerMinute);
        Consumer consumer = new Consumer(queue, (int) consumePerMinute);
        producerExecutor.scheduleAtFixedRate(producer,
                                             INITIAL_DELAY,
                                             ACTION_PERIOD,
                                             TimeUnit.SECONDS);
        consumerExecutor.submit(consumer);
    }

    private static Properties loadAppProperties(String appConfigPath) {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return appProps;
    }
}
