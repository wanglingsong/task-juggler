package org.juggling;

import java.util.Queue;

public class SampleTaskQueue implements TaskQueue<HttpTask> {

    private Queue<HttpTask> queue;

    public SampleTaskQueue(Queue<HttpTask> queue) {
        this.queue = queue;
    }

    @Override
    public void addTask(String queueName, HttpTask task) {
        queue.add(task);
    }

}
