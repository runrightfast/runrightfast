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
import com.google.gson.JsonObject;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.qi4j.api.service.ServiceReference;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;
import reactor.Environment;
import static reactor.Environment.DISPATCHER_GROUP;
import static reactor.Environment.MPSC;
import static reactor.Environment.SHARED;
import static reactor.Environment.THREAD_POOL;
import static reactor.Environment.WORK_QUEUE;
import reactor.core.Dispatcher;
import reactor.rx.Streams;

/**
 *
 * @author alfio
 */
@Log
public class EnvironmentProviderTest extends AbstractQi4jTest {

    final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testEnvironment() throws InterruptedException {
        final ServiceReference<ReactorEnvironmentProvider> serviceRef = module.findService(ReactorEnvironmentProvider.class);
        assertThat(serviceRef, is(notNullValue()));

        final ReactorEnvironmentProvider service = serviceRef.get();
        assertThat(service, is(notNullValue()));

        final Environment env = service.environment();
        assertThat(env, is(notNullValue()));
        logAllDispatchers(env);

        final Dispatcher defaultDispatcher = env.getDefaultDispatcher();
        assertThat(defaultDispatcher, is(notNullValue()));
        log(defaultDispatcher);

        env.iterator().forEachRemaining(entry -> {
            log.info(String.format("%s -> %s", entry.getKey(), dispatcherInfo(entry.getValue())));
        });

        logDispatchers(env, DISPATCHER_GROUP, MPSC, SHARED, THREAD_POOL, WORK_QUEUE);
        logAllDispatchers(env);

        testDispatching("default", env.getDefaultDispatcher());

        for (int i = 1; i <= 16; i++) {
            testDispatching("cached:" + i, env.getCachedDispatcher());
        }
    }

    private void logAllDispatchers(final Environment env) {
        env.iterator().forEachRemaining(entry -> {
            log.info(String.format("%s -> %s", entry.getKey(), dispatcherInfo(entry.getValue())));
        });
    }

    private void logDispatchers(final Environment env, String... names) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Streams.from(names)
                .dispatchOn(env.getDefaultDispatcher())
                .ignoreError()
                .consume(
                        name -> {
                            try {
                                final Dispatcher dispatcher = env.getDispatcher(name);
                                log(name, dispatcher);
                                testDispatching(name, dispatcher);
                            } catch (final IllegalArgumentException e) {
                                // thrown if the dispatcher is not registered with the environment
                                log.logp(Level.SEVERE, getClass().getName(), "logDispatchers", name, e);
                            } catch (final Exception e) {
                                log.logp(Level.SEVERE, getClass().getName(), "logDispatchers", name, e);
                            }

                        },
                        exception -> {
                            log.logp(Level.SEVERE, getClass().getName(), "logDispatchers", "failed to log dispatcher info", exception);
                            latch.countDown();
                        },
                        v -> {
                            log.info("dispatcherNames stream is done");
                            latch.countDown();
                        }
                );

        latch.await();
    }

    @Override
    public void assemble(final ModuleAssembly assembly) throws AssemblyException {
        assembleDefaultReactorEnvironment(assembly);
    }

    private void log(final Dispatcher dispatcher) {
        log.info(gson.toJson(dispatcherInfo(dispatcher)));
    }

    private void log(final String name, final Dispatcher dispatcher) {
        final JsonObject json = dispatcherInfo(dispatcher);
        json.addProperty("name", name);
        log.info(gson.toJson(json));
    }

    private JsonObject dispatcherInfo(final Dispatcher dispatcher) {
        final JsonObject json = new JsonObject();
        if (dispatcher == null) {
            return json;
        }
        json.addProperty("class", dispatcher.getClass().getName());
        json.addProperty("alive", dispatcher.alive());
        json.addProperty("backlogSize", dispatcher.backlogSize());
        json.addProperty("remainingSlots", dispatcher.remainingSlots());
        json.addProperty("inContext", dispatcher.inContext());
        json.addProperty("supportsOrdering", dispatcher.supportsOrdering());
        return json;
    }

    private void testDispatching(final String name, final Dispatcher dispatcher) throws InterruptedException {
        int taskCount = 2;
        final CountDownLatch latch = new CountDownLatch(taskCount);
        final long sleepTime = 100L;
        Streams
                .range(1, 2)
                .dispatchOn(dispatcher)
                .consume(id -> {
                    log.info(String.format("%s: %s: RECEIVED MESSAGE : %d", name, Thread.currentThread().getName(), id));
                    log(name, dispatcher);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    log.info(String.format("%s: %s: PROCESSED MESSAGE : %d", name, Thread.currentThread().getName(), id));
                    latch.countDown();
                });

        latch.await();

    }

}
