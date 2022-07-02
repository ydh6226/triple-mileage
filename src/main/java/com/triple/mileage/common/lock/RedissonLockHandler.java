package com.triple.mileage.common.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class RedissonLockHandler implements LockHandler {

    private static final int DEFAULT_WAIT_TIME = 200;
    private static final int DEFAULT_LEASE_TIME = 300;

    private final RedissonClient redissonClient;

    public void runWithLock(Runnable runnable, UUID key) {
        String stringKey = String.valueOf(key);

        RLock lock = redissonClient.getLock(stringKey);
        try {
            boolean isLocked = lock.tryLock(DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, TimeUnit.MILLISECONDS);
            if (!isLocked) {
                throwException(stringKey);
            }

            runnable.run();
        } catch (InterruptedException e) {
            log.info("락 획득 중 InterruptedException 예외 발생", e);
            throwException(stringKey);
        } finally {
            lock.unlock();
        }
    }

    private void throwException(String key) {
        throw new LockAcquirementFailException("분산락 획득에 실패했습니다. key: " + key);
    }
}
