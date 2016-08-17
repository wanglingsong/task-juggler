package org.juggling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleTaskInvoker implements TaskInvoker<HttpTask> {

    final Logger logger = LoggerFactory.getLogger(SampleTaskInvoker.class);

    @Override
    public void invoke(HttpTask task) {
        logger.info("Invoke task {}", task);
        // TODO invoke remote api using HTTP client here
    }

}
