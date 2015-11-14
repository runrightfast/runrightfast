/*
 Copyright 2015 Alfio Zappala

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package co.runrightfast.commons.utils;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author alfio
 */
@Slf4j
public final class ConcurrentUtils {

    public static void awaitCountdownLatch(@NonNull final CountDownLatch latch, @NonNull final Duration waitInterval) throws InterruptedException {
        for (int i = 1; !latch.await(waitInterval.toMillis(), TimeUnit.MILLISECONDS); i++) {
            log.warn("Waiting {} msec for count down to complete : {}", i * 1000, latch.getCount());
        }
    }

    /**
     *
     * @param latch CountDownLatch
     * @param waitInterval at each wait interval, a warning message is logged stating that we are still waiting for the count down to complete
     * @param action action that is performed after the count down is complete
     * @throws InterruptedException if the thread is interrupted while awaiting for the CountDownLatch
     */
    public static void awaitCountdownLatch(@NonNull final CountDownLatch latch, @NonNull final Duration waitInterval, @NonNull final Runnable action) throws InterruptedException {
        awaitCountdownLatch(latch, waitInterval,
                waitDuration -> log.warn("Waiting {} msec for count down to complete : {}", waitDuration.toMillis(), latch.getCount()),
                action
        );
    }

    /**
     * Waits for a CountDownLatch to complete, i.e., reach 0 count;
     *
     * @param latch CountDownLatch
     * @param waitInterval how long to wait before notifying the waitIntervalElapsed callback
     * @param waitIntervalElapsed used as a callback to notify the caller that we are still waiting for the CountDownLatch to complete its count down.
     * @param action action that is performed after the count down is complete
     * @throws InterruptedException if the thread is interrupted while awaiting for the CountDownLatch
     */
    public static void awaitCountdownLatch(@NonNull final CountDownLatch latch, @NonNull final Duration waitInterval, @NonNull final Consumer<Duration> waitIntervalElapsed, @NonNull final Runnable action) throws InterruptedException {
        for (int i = 1; !latch.await(waitInterval.toMillis(), TimeUnit.MILLISECONDS); i++) {
            waitIntervalElapsed.accept(Duration.ofMillis(i * waitInterval.toMillis()));
        }
        action.run();
    }

    public static void awaitCountdownLatch(@NonNull final CountDownLatch latch, @NonNull final Duration waitInterval, @NonNull final Consumer<Duration> waitIntervalElapsed) throws InterruptedException {
        for (int i = 1; !latch.await(waitInterval.toMillis(), TimeUnit.MILLISECONDS); i++) {
            waitIntervalElapsed.accept(Duration.ofMillis(i * waitInterval.toMillis()));
        }
    }

    public static void awaitCountdownLatch(@NonNull final CountDownLatch latch, @NonNull final Duration waitInterval, @NonNull final String waitingMessage) throws InterruptedException {
        awaitCountdownLatch(latch, waitInterval, waitDuration -> log.warn("{} : total wait time is {} msec", waitDuration.toMillis(), latch.getCount()));
    }

    public static void awaitCountdownLatchIgnoringInterruptedException(@NonNull final CountDownLatch latch, @NonNull final Duration waitInterval, @NonNull final Consumer<Duration> waitIntervalElapsed) {
        try {
            awaitCountdownLatch(latch, waitInterval, waitIntervalElapsed);
        } catch (InterruptedException ex) {
            logInterruptedException(ex);
        }
    }

    public static void awaitCountdownLatchIgnoringInterruptedException(@NonNull final CountDownLatch latch, @NonNull final Duration waitInterval, @NonNull final String waitingMessage) {
        try {
            awaitCountdownLatch(latch, waitInterval, waitDuration -> log.warn("{} : total wait time is {} msec", waitDuration.toMillis(), latch.getCount()));
        } catch (final InterruptedException ex) {
            logInterruptedException(ex);
        }
    }

    public static void awaitCountdownLatchIgnoringInterruptedException(@NonNull final CountDownLatch latch, @NonNull final Duration waitInterval, @NonNull final Runnable action) {
        try {
            awaitCountdownLatch(latch, waitInterval, action);
        } catch (final InterruptedException ex) {
            logInterruptedException(ex);
        }
    }

    public static void awaitCountdownLatchIgnoringInterruptedException(@NonNull final CountDownLatch latch, @NonNull final Duration waitInterval) {
        try {
            awaitCountdownLatch(latch, waitInterval);
        } catch (final InterruptedException ex) {
            logInterruptedException(ex);
        }
    }

    private static void logInterruptedException(final InterruptedException ex) {
        log.warn("Thread was interrupted while waiting for count down latch", ex);
    }

}
