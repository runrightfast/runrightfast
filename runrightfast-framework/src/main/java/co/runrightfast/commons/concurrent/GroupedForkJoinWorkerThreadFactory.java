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

import static co.runrightfast.commons.utils.PreconditionUtils.notBlank;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * The thread naming pattern is {group}/{sequence}/{createdOnTimestamp}
 *
 * @author alfio
 */
@RequiredArgsConstructor
public final class GroupedForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {

    @NonNull
    private final ThreadGroup group;

    @NonNull
    private final Optional<ThreadFactoryConfig> threadFactoryConfig;

    public GroupedForkJoinWorkerThreadFactory(@NonNull final ThreadGroup group) {
        this(group, Optional.empty());
    }

    public GroupedForkJoinWorkerThreadFactory(@NonNull final ThreadGroup group, @NonNull final ThreadFactoryConfig threadFactoryConfig) {
        this(group, Optional.of(threadFactoryConfig));
    }

    public GroupedForkJoinWorkerThreadFactory(final String group) {
        notBlank(group, "group");
        this.group = new ThreadGroup(group.trim());
        this.threadFactoryConfig = Optional.empty();
    }

    private String newThreadName(long threadId) {
        return new StringBuilder()
                .append(group.getName())
                .append('/')
                .append(threadId)
                .append('/')
                .append(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
                .toString();
    }

    @Override
    public ForkJoinWorkerThread newThread(final ForkJoinPool pool) {
        final ForkJoinWorkerThread t = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
        t.setName(newThreadName(t.getId()));
        return threadFactoryConfig.map(config -> (ForkJoinWorkerThread) config.configure(t)).orElse(t);
    }

}
