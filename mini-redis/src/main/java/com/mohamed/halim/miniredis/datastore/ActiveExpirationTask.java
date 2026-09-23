package com.mohamed.halim.miniredis.datastore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ActiveExpirationTask {
    public static void startActiveMonitoring() {
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            var store = DataStore.getInstance();
            store.expireActiveCycle();
        };

        scheduler.scheduleAtFixedRate(
                task,
                0, // initial delay
                1, // period
                TimeUnit.SECONDS
        );

    }
}
