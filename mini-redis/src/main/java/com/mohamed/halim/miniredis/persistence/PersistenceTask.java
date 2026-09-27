package com.mohamed.halim.miniredis.persistence;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PersistenceTask {
    public static void startPersistenceTask() {
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        Persistence persistence = Persistence.getInstance();
        Runnable task = () -> {
            try {
                persistence.fsync();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        scheduler.scheduleAtFixedRate(
                task,
                0, // initial delay
                2, // period
                TimeUnit.SECONDS
        );

    }
}
