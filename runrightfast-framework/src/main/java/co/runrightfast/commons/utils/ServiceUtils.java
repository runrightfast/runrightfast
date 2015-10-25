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

import co.runrightfast.exceptions.ApplicationException;
import static co.runrightfast.exceptions.ApplicationExceptionSeverity.MAJOR;
import co.runrightfast.services.IllegalServiceStateTransitionException;
import com.google.common.util.concurrent.Service;
import static com.google.common.util.concurrent.Service.State.NEW;
import static com.google.common.util.concurrent.Service.State.RUNNING;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import lombok.NonNull;
import lombok.extern.java.Log;

/**
 *
 * @author alfio
 */
@Log
public final class ServiceUtils {

    private ServiceUtils() {
    }

    private static final String CLAZZ = ServiceUtils.class.getName();

    public static void start(@NonNull final Service service) {
        switch (service.state()) {
            case NEW:
                service.startAsync();
                awaitRunning(service);
                return;
            case STARTING:
                service.awaitRunning();
                awaitRunning(service);
                return;
            case RUNNING:
                return;
            default:
                throw new IllegalServiceStateTransitionException(service.state(), RUNNING);
        }

    }

    /**
     *
     * @param service if null, then do nothing
     */
    public static void stop(final Service service) {
        if (service != null) {
            switch (service.state()) {
                case STARTING:
                case RUNNING:
                    service.stopAsync();
                    awaitTerminated(service);
                    return;
                case STOPPING:
                    awaitTerminated(service);
                    return;
                case NEW:
                case FAILED:
                case TERMINATED:
                    log.logp(FINE, CLAZZ, "stop",
                            () -> String.format("Service (%s) is not running: %s", service.getClass().getName(), service.state())
                    );
            }
        }
    }

    public static void stopAsync(final Service service) {
        if (service != null) {
            switch (service.state()) {
                case STARTING:
                case RUNNING:
                    service.stopAsync();
                    return;
                case STOPPING:
                    return;
                case NEW:
                case FAILED:
                case TERMINATED:
                    log.logp(FINE, CLAZZ, "stopAsync",
                            () -> String.format("Service (%s) is not running: %s", service.getClass().getName(), service.state())
                    );

            }
        }
    }

    /**
     * Logs a warning every 10 seconds, waiting for the service to stop
     *
     * @param service Service
     */
    public static void awaitTerminated(@NonNull final Service service) {
        for (int i = 1; true; i++) {
            try {
                service.awaitTerminated(10, TimeUnit.SECONDS);
                return;
            } catch (final TimeoutException ex) {
                final int elapsedTimeSeconds = i * 10;
                log.logp(WARNING, CLAZZ, "awaitTerminated",
                        new ApplicationException(MAJOR, ex),
                        () -> String.format("Wating for service to terminate : %s : %d seconds", service.getClass().getName(), elapsedTimeSeconds)
                );
            }
        }
    }

    /**
     * Logs a warning every 10 seconds, waiting for the service to start
     *
     * @param service Service
     */
    public static void awaitRunning(@NonNull final Service service) {
        for (int i = 1; true; i++) {
            try {
                service.awaitRunning(10, TimeUnit.SECONDS);
                return;
            } catch (final TimeoutException ex) {
                final int elapsedTimeSeconds = i * 10;
                log.logp(WARNING, CLAZZ, "awaitRunning",
                        new ApplicationException(MAJOR, ex),
                        () -> String.format("Wating for service to start : %s : %d seconds", service.getClass().getName(), elapsedTimeSeconds)
                );
            }
        }
    }

}
