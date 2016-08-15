package org.juggling;

public interface TaskConsumer<T> {

    T reserveTask(String queueName);

    void releaseTask(T task);

    void deleteTask(T task);

}
