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

import static java.lang.Boolean.TRUE;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class GroupBasedThreadFactoryTest {

    @Test
    public void testNewThread() {
        final GroupBasedThreadFactory threadFactory = new GroupBasedThreadFactory(" app ");
        final Thread thread1 = threadFactory.newThread(() -> log.info(Thread.currentThread().getName()));
        log.info(String.format("thread1 name : %s", thread1.getName()));

        String expectedThreadNamePrefix = "app/" + thread1.getId() + '/';
        assertThat(thread1.getName().startsWith(expectedThreadNamePrefix), is(true));
        final Instant threadCreationTime1 = Instant.parse(thread1.getName().substring(expectedThreadNamePrefix.length()));
        assertThat(thread1.getName(), is(expectedThreadNamePrefix + DateTimeFormatter.ISO_INSTANT.format(threadCreationTime1)));

        final Thread thread2 = threadFactory.newThread(() -> log.info(Thread.currentThread().getName()));
        log.info(String.format("thread2 name : %s", thread2.getName()));

        expectedThreadNamePrefix = "app/" + thread2.getId() + '/';
        assertThat(thread2.getName().startsWith(expectedThreadNamePrefix), is(true));
        final Instant threadCreationTime2 = Instant.parse(thread2.getName().substring(expectedThreadNamePrefix.length()));
        assertThat(thread2.getName(), is(expectedThreadNamePrefix + DateTimeFormatter.ISO_INSTANT.format(threadCreationTime2)));
    }

    @Test
    public void testNewThreadWithThreadFactoryConfig() {
        final ThreadFactoryConfig threadFactoryConfig = ThreadFactoryConfig.builder()
                .daemon(Optional.of(TRUE))
                .priority(Optional.of(8))
                .uncaughtExceptionHandler(Optional.of((t, e) -> log.info(String.format("%s : %s", t.getName(), e))))
                .build();

        final GroupBasedThreadFactory threadFactory = new GroupBasedThreadFactory(" app ", threadFactoryConfig);
        final Thread thread1 = threadFactory.newThread(() -> log.info(Thread.currentThread().getName()));
        log.info(String.format("thread1 name : %s", thread1.getName()));
        assertThat(thread1.isDaemon(), is(threadFactoryConfig.getDaemon().get()));
        assertThat(thread1.getPriority(), is(threadFactoryConfig.getPriority().get()));
        assertThat(thread1.getUncaughtExceptionHandler(), is(threadFactoryConfig.getUncaughtExceptionHandler().get()));

        String expectedThreadNamePrefix = "app/" + thread1.getId() + '/';
        assertThat(thread1.getName().startsWith(expectedThreadNamePrefix), is(true));
        final Instant threadCreationTime1 = Instant.parse(thread1.getName().substring(expectedThreadNamePrefix.length()));
        assertThat(thread1.getName(), is(expectedThreadNamePrefix + DateTimeFormatter.ISO_INSTANT.format(threadCreationTime1)));

        final Thread thread2 = threadFactory.newThread(() -> log.info(Thread.currentThread().getName()));
        log.info(String.format("thread2 name : %s", thread2.getName()));

        expectedThreadNamePrefix = "app/" + thread2.getId() + '/';
        assertThat(thread2.getName().startsWith(expectedThreadNamePrefix), is(true));
        final Instant threadCreationTime2 = Instant.parse(thread2.getName().substring(expectedThreadNamePrefix.length()));
        assertThat(thread2.getName(), is(expectedThreadNamePrefix + DateTimeFormatter.ISO_INSTANT.format(threadCreationTime2)));
    }

    @Test
    public void testNewThreadUsingPrexistingThreadGroup() {
        final GroupBasedThreadFactory threadFactory = new GroupBasedThreadFactory(new ThreadGroup("app"));
        final Thread thread1 = threadFactory.newThread(() -> log.info(Thread.currentThread().getName()));
        log.info(String.format("thread1 name : %s", thread1.getName()));

        String expectedThreadNamePrefix = "app/" + thread1.getId() + '/';
        assertThat(thread1.getName().startsWith(expectedThreadNamePrefix), is(true));
        final Instant threadCreationTime1 = Instant.parse(thread1.getName().substring(expectedThreadNamePrefix.length()));
        assertThat(thread1.getName(), is(expectedThreadNamePrefix + DateTimeFormatter.ISO_INSTANT.format(threadCreationTime1)));

        final Thread thread2 = threadFactory.newThread(() -> log.info(Thread.currentThread().getName()));
        log.info(String.format("thread2 name : %s", thread2.getName()));

        expectedThreadNamePrefix = "app/" + thread2.getId() + '/';
        assertThat(thread2.getName().startsWith(expectedThreadNamePrefix), is(true));
        final Instant threadCreationTime2 = Instant.parse(thread2.getName().substring(expectedThreadNamePrefix.length()));
        assertThat(thread2.getName(), is(expectedThreadNamePrefix + DateTimeFormatter.ISO_INSTANT.format(threadCreationTime2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsingNullGroupName() {
        final GroupBasedThreadFactory threadFactory = new GroupBasedThreadFactory((String) null);
        final Thread thread1 = threadFactory.newThread(() -> log.info(Thread.currentThread().getName()));
        log.info(String.format("thread1 name : %s", thread1.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsingBlankGroupName() {
        final GroupBasedThreadFactory threadFactory = new GroupBasedThreadFactory("  ");
        final Thread thread1 = threadFactory.newThread(() -> log.info(Thread.currentThread().getName()));
        log.info(String.format("thread1 name : %s", thread1.getName()));
    }

}
