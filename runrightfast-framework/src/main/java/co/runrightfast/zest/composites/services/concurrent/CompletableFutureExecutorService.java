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
package co.runrightfast.zest.composites.services.concurrent;

import static co.runrightfast.zest.composites.services.concurrent.CompletableFutureExecutorService.Nothing.NOTHING;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;

/**
 *
 * @author alfio
 */
public interface CompletableFutureExecutorService {

    public static final class Nothing {

        private Nothing() {
        }

        public static Nothing NOTHING = new Nothing();

    }

    <OUTPUT> CompletableFuture<OUTPUT> submit(Supplier<OUTPUT> task) throws TaskRejectedException;

    default <INPUT, OUTPUT> CompletableFuture<OUTPUT> submit(@NonNull INPUT input, @NonNull Function<INPUT, OUTPUT> task) throws TaskRejectedException {
        return submit(() -> task.apply(input));
    }

    default CompletableFuture<Nothing> submit(final Runnable task) throws TaskRejectedException {
        return submit(() -> {
            task.run();
            return NOTHING;
        });
    }

    default <INPUT> CompletableFuture<Nothing> submit(@NonNull final INPUT input, @NonNull final Consumer<INPUT> task) throws TaskRejectedException {
        return submit(() -> task.accept(input));
    }

}
