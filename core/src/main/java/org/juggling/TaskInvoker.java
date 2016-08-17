package org.juggling;

public interface TaskInvoker<T> {

    void invoke(T task);

}
