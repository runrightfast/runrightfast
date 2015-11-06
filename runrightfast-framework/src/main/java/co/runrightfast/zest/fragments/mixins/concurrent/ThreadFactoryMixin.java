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
package co.runrightfast.zest.fragments.mixins.concurrent;

import co.runrightfast.zest.composites.services.concurrent.ThreadFactoryService;
import co.runrightfast.zest.composites.services.concurrent.ThreadGroupService;
import co.runrightfast.zest.composites.services.concurrent.config.ThreadFactoryConfig;
import co.runrightfast.zest.composites.values.ApplicationModule;
import co.runrightfast.zest.composites.values.ApplicationModuleFactory;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import lombok.extern.java.Log;
import org.qi4j.api.configuration.Configuration;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.structure.Module;

/**
 *
 * TODO: collect metrics - per thread group
 * <pre>
 *  - counters
 *      - how many threads have been created
 *      - how many uncaught exceptions - per exception type
 *  - gauge - how many threads are running
 *  - gauge - the last time a thread was created
 *  - gauge - the last time a thread started running
 *  - meter - how often are thread run() methods called
 *  - meter - how often are uncaught exceptions occurring
 *  - timer - how long are the run() methods running
 * </pre>
 *
 * TODO: add tracing for when a thread is created, when it starts running, and when it is done
 *
 * @author alfio
 */
@Log
public class ThreadFactoryMixin implements ThreadFactoryService {

    private final ApplicationModule applicationModule;

    private final ThreadGroup group;

    @This
    private Configuration<ThreadFactoryConfig> threadFactoryConfig;

    public ThreadFactoryMixin(@Structure final Module module, @Service final ThreadGroupService threadGroupService) {
        this.applicationModule = module.newValue(ApplicationModuleFactory.class).applicationModule();
        this.group = threadGroupService.getThreadGroup(applicationModule);
    }

    @Override
    public Thread newThread(final Runnable r) {
        final Thread t = new Thread(group, r);
        t.setName(newThreadName(t.getId()));
        configure(t);
        return t;
    }

    @Override
    public ForkJoinWorkerThread newThread(final ForkJoinPool pool) {
        final ForkJoinWorkerThread t = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
        t.setName(newThreadName(t.getId()));
        configure(t);
        return t;
    }

    private void configure(final Thread t) {
        final ThreadFactoryConfig config = threadFactoryConfig.get();
        if (config.daemon().get() != null) {
            t.setDaemon(config.daemon().get());
        }

        if (config.threadPriority().get() != null) {
            t.setPriority(config.threadPriority().get());
        }
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
