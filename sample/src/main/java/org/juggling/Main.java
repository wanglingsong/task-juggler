package org.juggling;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] s) {

        TaskJuggler<HttpTask> juggler = new TaskJuggler<>();
        Queue<HttpTask> queue = new ArrayBlockingQueue<>(100);
        SampleTaskConsumer taskConsumer = new SampleTaskConsumer(queue);
        juggler.setTaskConsumer(taskConsumer);
        juggler.setTaskInvoker(new SampleTaskInvoker());
        juggler.setExecutorService(Executors.newScheduledThreadPool(16));
        juggler.setSleepingInterval(100);
        HashMap<String, Integer> config = new HashMap<>();
        config.put("dummy", 2);
        juggler.setConcurrencyConfig(config);
        juggler.kickOff();

        SampleTaskQueue taskQueue = new SampleTaskQueue(queue);
        taskQueue.addTask("dummy", HttpTask.GET().withUrl("/api/test?dummy=true"));

    }

}
