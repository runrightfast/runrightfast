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
package co.runrightfast.commons.concurrent;

import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 *
 * @author alfio
 */
@Value
@Builder
public class ThreadFactoryConfig {

    Optional<Boolean> daemon;

    Optional<Integer> priority;

    Optional<Thread.UncaughtExceptionHandler> uncaughtExceptionHandler;

    public ThreadFactoryConfig(final Optional<Boolean> daemon, final Optional<Integer> priority, final Optional<Thread.UncaughtExceptionHandler> uncaughtExceptionHandler) {
        this.daemon = daemon != null ? daemon : Optional.empty();
        this.priority = priority != null ? priority : Optional.empty();
        this.uncaughtExceptionHandler = uncaughtExceptionHandler != null ? uncaughtExceptionHandler : Optional.empty();
    }

    public Thread configure(@NonNull final Thread t) {
        daemon.ifPresent(t::setDaemon);
        priority.ifPresent(t::setPriority);
        uncaughtExceptionHandler.ifPresent(t::setUncaughtExceptionHandler);
        return t;
    }

}
