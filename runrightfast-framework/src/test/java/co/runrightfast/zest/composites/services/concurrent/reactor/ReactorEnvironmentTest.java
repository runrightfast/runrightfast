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
package co.runrightfast.zest.composites.services.concurrent.reactor;

import static co.runrightfast.zest.assemblers.ReactorAssemblers.assembleDefaultReactorEnvironment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.qi4j.api.service.ServiceReference;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;
import reactor.Environment;
import reactor.core.Dispatcher;
import reactor.core.config.DispatcherType;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.core.dispatch.TailRecurseDispatcher;

/**
 *
 * @author alfio
 */
@Slf4j
public class ReactorEnvironmentTest extends AbstractQi4jTest {

    final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void assemble(final ModuleAssembly assembly) throws AssemblyException {
        assembleDefaultReactorEnvironment(assembly);
    }

    @Test
    public void testEnvironment() throws InterruptedException {
        final ServiceReference<ReactorEnvironment> serviceRef = module.findService(ReactorEnvironment.class);
        final ReactorEnvironment service = serviceRef.get();
        final Environment env = service.environment();

        assertThat(testDispatcher(2, Environment.cachedDispatcher()).size(), is(1));
        assertThat(testDispatcher(Environment.PROCESSORS, Environment.sharedDispatcher()).size(), is(1));
        assertThat(testDispatcher(Environment.PROCESSORS * 2, Environment.workDispatcher()).size() >= Environment.PROCESSORS, is(true));
        assertThat(testDispatcher(2, Environment.newDispatcher(16, 16, DispatcherType.THREAD_POOL_EXECUTOR)).size(), is(2));
        assertThat(testDispatcher(2, SynchronousDispatcher.INSTANCE).size(), is(1));
        assertThat(testDispatcher(2, TailRecurseDispatcher.INSTANCE).size(), is(1));

        final Dispatcher ringBuffer = module.findService(RingBufferDispatcherProvider.class).get().ringBufferDispatcher();
        assertThat(ringBuffer.supportsOrdering(), is(true));
        assertThat(testDispatcher(Environment.PROCESSORS, ringBuffer).size(), is(1));

        final Dispatcher workQueue = module.findService(WorkQueueDispatcherProvider.class).get().workQueueDispatcher();
        assertThat(workQueue.supportsOrdering(), is(false));
        // the pool size should
        assertThat(testDispatcher(Environment.PROCESSORS * 2, workQueue).size() >= Environment.PROCESSORS, is(true));
    }

    private Set<String> testDispatcher(final int taskCount, final Dispatcher dispatcher) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(taskCount);
        final Set<String> threadNames = new HashSet<>();
        for (int i = 0; i < taskCount; i++) {
            dispatcher.dispatch(i,
                    msg -> {
                        threadNames.add(Thread.currentThread().getName());
                        log.info("{} : RECEIVED {}", Thread.currentThread().getName(), msg);
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException ex) {
                            // ignore
                        }
                        log.info("{} : DONE {}", Thread.currentThread().getName(), msg);
                        latch.countDown();
                    },
                    exception -> {
                        log.error("error", exception);
                        latch.countDown();
                    }
            );
        }

        latch.await();
        return threadNames;
    }

}
