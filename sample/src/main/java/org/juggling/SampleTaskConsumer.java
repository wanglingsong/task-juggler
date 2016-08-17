package org.juggling;

import java.util.Queue;

public class SampleTaskConsumer implements TaskConsumer<HttpTask> {

    private Queue<HttpTask> queue;

    public SampleTaskConsumer(Queue<HttpTask> queue) {
        this.queue = queue;
    }

    @Override
    public HttpTask reserveTask(String queueName) {
        return queue.poll();
    }

    @Override
    public void releaseTask(HttpTask task) {
        // TODO
    }

    @Override
    public void deleteTask(HttpTask task) {
        // TODO
    }

}
