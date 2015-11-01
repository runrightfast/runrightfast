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

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    <INPUT, OUTPUT> CompletableFuture<OUTPUT> submit(INPUT input, Function<INPUT, OUTPUT> task) throws TaskRejectedException;

    CompletableFuture<Nothing> submit(Runnable task) throws TaskRejectedException;

    <INPUT> CompletableFuture<Nothing> submit(INPUT input, Consumer<INPUT> task) throws TaskRejectedException;

}
