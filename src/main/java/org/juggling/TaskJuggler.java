package org.juggling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskJuggler<T> implements Runnable {

    final Logger logger = LoggerFactory.getLogger(TaskJuggler.class);

    private final Object lock = new Object();
    private Map<String, AtomicInteger> concurrencyConfig;
    private long sleepingInterval;
    private TaskInvoker<T> taskInvoker;
    private TaskConsumer<T> taskConsumer;
    private ExecutorService executorService;

    public void kickOff() {
        this.executorService.execute(this);
        logger.info("task worker started!");
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                for (Map.Entry<String, AtomicInteger> entry : concurrencyConfig.entrySet()) {
                    String queueName = entry.getKey();
                    final AtomicInteger allowedConcurrency = entry.getValue();
                    while (allowedConcurrency.get() > 0) {
                        final T task = taskConsumer.reserveTask(queueName);
                        if (task != null) {
                            logger.debug("Consumed one task {}", task);
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        taskInvoker.invoke(task);
                                        taskConsumer.deleteTask(task);
                                    } catch (Exception e) {
                                        logger.error("Failed to invoke task: " + task, e);
                                        taskConsumer.releaseTask(task);
                                    } finally {
                                        allowedConcurrency.incrementAndGet();
                                    }
                                }
                            });
                            int availableConsumer = allowedConcurrency.decrementAndGet();
                            logger.debug("{} available consumers for queue {}", availableConsumer, queueName);
                        } else {
                            break;
                        }
                    }
                }
                synchronized (this.lock) {
                    this.lock.wait(sleepingInterval);
                }
            }
        } catch (InterruptedException e) {
            logger.error("Task queue consumer interrupted", e);
        }
    }

    public long getSleepingInterval() {
        return sleepingInterval;
    }

    public void setSleepingInterval(long sleepingInterval) {
        this.sleepingInterval = sleepingInterval;
    }

    public Map<String, AtomicInteger> getConcurrencyConfig() {
        return concurrencyConfig;
    }

    public void setConcurrencyConfig(Map<String, Integer> concurrencyConfig) {
        HashMap<String, AtomicInteger> newMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : concurrencyConfig.entrySet()) {
            newMap.put(entry.getKey(), new AtomicInteger(entry.getValue()));
        }
        this.concurrencyConfig = newMap;
    }

    public TaskInvoker<T> getTaskInvoker() {
        return taskInvoker;
    }

    public void setTaskInvoker(TaskInvoker<T> taskInvoker) {
        this.taskInvoker = taskInvoker;
    }

    public TaskConsumer<T> getTaskConsumer() {
        return taskConsumer;
    }

    public void setTaskConsumer(TaskConsumer<T> taskConsumer) {
        this.taskConsumer = taskConsumer;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

}
