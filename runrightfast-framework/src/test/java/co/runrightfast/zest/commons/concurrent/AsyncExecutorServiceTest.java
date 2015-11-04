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

import co.runrightfast.zest.composites.services.concurrent.TaskRejectedException;
import co.runrightfast.zest.composites.services.concurrent.AsyncExecutorService;
import java.util.function.Consumer;
import lombok.extern.java.Log;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class AsyncExecutorServiceTest {

    @Test
    public void testSubmit_Runnable() {
        AsyncExecutorService instance = new AsyncExecutorServiceImpl();
        instance.submit(() -> log.info("testSubmit_Runnable()"));
    }

    @Test
    public void testSubmit_GenericType_Consumer() {
        AsyncExecutorService instance = new AsyncExecutorServiceImpl();
        instance.submit(1, input -> log.info(String.format("input: %s", input)));
    }

    public class AsyncExecutorServiceImpl implements AsyncExecutorService {

        @Override
        public void submit(Runnable task) throws TaskRejectedException {
            task.run();
        }

        @Override
        public <INPUT> void submit(INPUT input, Consumer<INPUT> task) throws TaskRejectedException {
            task.accept(input);
        }

    }

}
