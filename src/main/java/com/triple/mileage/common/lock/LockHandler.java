package com.triple.mileage.common.lock;

import java.util.UUID;

@FunctionalInterface
public interface LockHandler {
    void runWithLock(Runnable runnable, UUID key);
}
