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
import java.util.concurrent.ThreadFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * The thread naming pattern is {group}/{sequence}/{createdOnTimestamp}
 *
 * @author alfio
 */
@RequiredArgsConstructor
public final class GroupBasedThreadFactory implements ThreadFactory {

    @Getter
    @NonNull
    private final ThreadGroup group;

    @NonNull
    private final Optional<ThreadFactoryConfig> threadFactoryConfig;

    public GroupBasedThreadFactory(@NonNull final ThreadGroup group) {
        this(group, Optional.empty());
    }

    public GroupBasedThreadFactory(@NonNull final ThreadGroup group, @NonNull final ThreadFactoryConfig threadFactoryConfig) {
        this(group, Optional.of(threadFactoryConfig));
    }

    public GroupBasedThreadFactory(final String group) {
        notBlank(group, "group");
        this.group = new ThreadGroup(group.trim());
        this.threadFactoryConfig = Optional.empty();
    }

    public GroupBasedThreadFactory(final String group, @NonNull final ThreadFactoryConfig threadFactoryConfig) {
        notBlank(group, "group");
        this.group = new ThreadGroup(group.trim());
        this.threadFactoryConfig = Optional.of(threadFactoryConfig);
    }

    @Override
    public Thread newThread(final Runnable r) {
        final Thread t = new Thread(group, r);
        t.setName(newThreadName(t.getId()));
        return threadFactoryConfig.map(config -> config.configure(t)).orElse(t);
    }

    private String newThreadName(final long threadId) {
        return new StringBuilder()
                .append(group.getName())
                .append('/')
                .append(threadId)
                .append('/')
                .append(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
                .toString();
    }

}
