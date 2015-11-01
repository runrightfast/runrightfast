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
package co.runrightfast.zest.commons.concurrent;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class CompletableFutureExecutorServiceTest {

    @Test
    public void testSubmit_Supplier() throws Exception {
        CompletableFutureExecutorService instance = new CompletableFutureExecutorServiceImpl();
        CompletableFuture<Integer> result = instance.submit(() -> 1);
        assertThat(result.get(), is(1));
    }

    @Test
    public void testSubmit_Function_GenericType() throws Exception {
        CompletableFutureExecutorService instance = new CompletableFutureExecutorServiceImpl();
        CompletableFuture<Integer> result = instance.submit(1, (Integer x) -> {
            return 2 * x;
        });
        assertThat(result.get(), is(2));
    }

    @Test
    public void testSubmit_Runnable() {
        CompletableFutureExecutorService instance = new CompletableFutureExecutorServiceImpl();
        CompletableFuture<CompletableFutureExecutorService.Nothing> result = instance.submit(() -> log.info("testSubmit_Runnable()"));
        assertThat(result.isDone(), is(true));

    }

    @Test
    public void testSubmit_Consumer_GenericType() {
        CompletableFutureExecutorService instance = new CompletableFutureExecutorServiceImpl();
        CompletableFuture<CompletableFutureExecutorService.Nothing> result = instance.submit(Instant.now(), (Instant input) -> {
            log.log(Level.INFO, "input: {0}", input);
        });
    }

    public class CompletableFutureExecutorServiceImpl implements CompletableFutureExecutorService {

        @Override
        public <OUTPUT> CompletableFuture<OUTPUT> submit(Supplier<OUTPUT> task) throws TaskRejectedException {
            CompletableFuture<OUTPUT> future = new CompletableFuture<>();
            future.complete(task.get());
            return future;
        }

        @Override
        public <INPUT, OUTPUT> CompletableFuture<OUTPUT> submit(INPUT input, Function<INPUT, OUTPUT> task) throws TaskRejectedException {
            CompletableFuture<OUTPUT> future = new CompletableFuture<>();
            future.complete(task.apply(input));
            return future;
        }

        @Override
        public CompletableFuture<Nothing> submit(Runnable task) throws TaskRejectedException {
            CompletableFuture<Nothing> future = new CompletableFuture<>();
            future.complete(Nothing.NOTHING);
            task.run();
            return future;
        }

        @Override
        public <INPUT> CompletableFuture<Nothing> submit(INPUT input, Consumer<INPUT> task) throws TaskRejectedException {
            CompletableFuture<Nothing> future = new CompletableFuture<>();
            future.complete(Nothing.NOTHING);
            task.accept(input);
            return future;
        }

    }

}
