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
package co.runrightfast.rx.impl;

import co.runrightfast.commons.disruptor.DisruptorConfig;
import co.runrightfast.commons.utils.ServiceUtils;
import co.runrightfast.rx.ObservableRingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.logging.Level.SEVERE;
import lombok.extern.java.Log;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class ObservableRingBufferImplTest {

    @Test
    public void testStartUp_shutDown() {
        final DisruptorConfig<String> disruptorConfig = DisruptorConfig.<String>builder()
                .ringBufferSize(100)
                .executor(Executors.newSingleThreadExecutor())
                .producerType(ProducerType.SINGLE)
                .waitStrategy(new YieldingWaitStrategy())
                .build();
        final ObservableRingBuffer<String> observableRingBuffer = new ObservableRingBufferImpl<>(disruptorConfig, String.class);
        try {
            ServiceUtils.start(observableRingBuffer);
            log.info(String.format("after started: %s", observableRingBuffer));
        } finally {
            ServiceUtils.stop(observableRingBuffer);
            log.info(String.format("after shutdown: %s", observableRingBuffer));
        }
    }

    /**
     * Test of publish method, of class ObservableRingBufferImpl.
     */
    @Test
    public void testPublish() {
        final DisruptorConfig<String> disruptorConfig = DisruptorConfig.<String>builder()
                .ringBufferSize(100)
                .executor(Executors.newSingleThreadExecutor())
                .producerType(ProducerType.SINGLE)
                .waitStrategy(new YieldingWaitStrategy())
                .build();
        final ObservableRingBuffer<String> observableRingBuffer = new ObservableRingBufferImpl<>(disruptorConfig, String.class);
        try {
            ServiceUtils.start(observableRingBuffer);
            log.info(String.format("after started: %s", observableRingBuffer));
            for (int i = 1; i <= 10; i++) {
                observableRingBuffer.publish(String.format("%s : msg #%d", Thread.currentThread().getName(), i));
            }
        } finally {
            ServiceUtils.stop(observableRingBuffer);
            log.info(String.format("after shutdown: %s", observableRingBuffer));
        }

    }

    /**
     * Test of publish method, of class ObservableRingBufferImpl.
     */
    @Test
    public void testPublish_usingForkJoinPool() {
        final DisruptorConfig<String> disruptorConfig = DisruptorConfig.<String>builder()
                .ringBufferSize(100)
                .executor(ForkJoinPool.commonPool())
                .producerType(ProducerType.SINGLE)
                .waitStrategy(new YieldingWaitStrategy())
                .build();
        final ObservableRingBuffer<String> observableRingBuffer = new ObservableRingBufferImpl<>(disruptorConfig, String.class);
        try {
            ServiceUtils.start(observableRingBuffer);
            final AtomicInteger counter = new AtomicInteger();
            observableRingBuffer.getObservable().subscribe(
                    msg -> log.info(String.format("%s : msg #%d", Thread.currentThread().getName(), counter.incrementAndGet())),
                    error -> log.log(SEVERE, "Observable error", error),
                    () -> log.info("completed")
            );
            log.info(String.format("after started: %s", observableRingBuffer));
            for (int i = 1; i <= 100; i++) {
                observableRingBuffer.publish("msg #" + i);
            }
            log.info(String.format("after publishing messages : %s", observableRingBuffer));
        } finally {
            ServiceUtils.stop(observableRingBuffer);
            log.info(String.format("after shutdown: %s", observableRingBuffer));
        }

    }

}
