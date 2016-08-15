package org.juggling;


public interface TaskQueue<T> {

    void addTask(String queueName, T task);

}
