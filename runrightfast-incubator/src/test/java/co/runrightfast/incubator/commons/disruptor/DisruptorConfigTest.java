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
package co.runrightfast.incubator.commons.disruptor;

import co.runrightfast.incubator.commons.disruptor.DisruptorConfig;
import co.runrightfast.incubator.commons.disruptor.RingBufferReference;
import static co.runrightfast.incubator.commons.disruptor.DisruptorConfig.DEFAULT_WaitStrategy;
import com.google.common.util.concurrent.MoreExecutors;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class DisruptorConfigTest {

    @Test
    public void test_getDefaultConfig() {
        final DisruptorConfig config = DisruptorConfig.getDefaultConfig();

        assertThat(config.getRingBufferSize(), is(1024));
        assertThat(config.getExecutor(), is(ForkJoinPool.commonPool()));
        assertThat(config.getProducerType(), is(ProducerType.MULTI));
        assertThat(config.getWaitStrategy(), is(DEFAULT_WaitStrategy));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_negativeRingBufferSize() {
        DisruptorConfig.builder().ringBufferSize(-1).build();
    }

    @Test
    public void test_withRingBufferSize() {
        final DisruptorConfig config = DisruptorConfig.builder()
                .ringBufferSize(100)
                .build();

        assertThat(config.getRingBufferSize(), is(128));
        assertThat(config.getExecutor(), is(ForkJoinPool.commonPool()));
        assertThat(config.getProducerType(), is(ProducerType.MULTI));
        assertThat(config.getWaitStrategy(), is(DEFAULT_WaitStrategy));
    }

    @Test
    public void test_withRingBufferSize_withExecutor() {
        final Executor executor = MoreExecutors.directExecutor();
        final DisruptorConfig config = DisruptorConfig.builder()
                .ringBufferSize(100)
                .executor(executor)
                .build();

        assertThat(config.getRingBufferSize(), is(128));
        assertThat(config.getExecutor(), is(executor));
        assertThat(config.getProducerType(), is(ProducerType.MULTI));
        assertThat(config.getWaitStrategy(), is(DEFAULT_WaitStrategy));
    }

    @Test
    public void test_withRingBufferSize_withExecutor_withProducerType() {
        final Executor executor = MoreExecutors.directExecutor();
        final DisruptorConfig config = DisruptorConfig.builder()
                .ringBufferSize(100)
                .executor(executor)
                .producerType(ProducerType.SINGLE)
                .build();

        assertThat(config.getRingBufferSize(), is(128));
        assertThat(config.getExecutor(), is(executor));
        assertThat(config.getProducerType(), is(ProducerType.SINGLE));
        assertThat(config.getWaitStrategy(), is(DEFAULT_WaitStrategy));
    }

    @Test
    public void test_withRingBufferSize_withExecutor_withProducerType_withWaitStrategy() {
        final Executor executor = MoreExecutors.directExecutor();
        final WaitStrategy waitStrategy = new YieldingWaitStrategy();
        final DisruptorConfig config = DisruptorConfig.builder()
                .ringBufferSize(100)
                .executor(executor)
                .producerType(ProducerType.SINGLE)
                .waitStrategy(waitStrategy)
                .build();

        assertThat(config.getRingBufferSize(), is(128));
        assertThat(config.getExecutor(), is(executor));
        assertThat(config.getProducerType(), is(ProducerType.SINGLE));
        assertThat(config.getWaitStrategy(), is(waitStrategy));
    }

    @Test
    public void test_newDisruptor() throws InterruptedException {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final WaitStrategy waitStrategy = new YieldingWaitStrategy();
        final DisruptorConfig config = DisruptorConfig.builder()
                .ringBufferSize(100)
                .executor(executor)
                .producerType(ProducerType.SINGLE)
                .waitStrategy(waitStrategy)
                .build();

        final Disruptor<RingBufferReference<Integer>> disruptor = config.newDisruptor(Integer.class);
        final int msgCount = 1000;

        final AtomicInteger msgReceivedCount = new AtomicInteger();
        disruptor.handleEventsWith((final RingBufferReference<Integer> event, final long sequence, final boolean endOfBatch) -> {
            msgReceivedCount.incrementAndGet();
            log.info(String.format("event: %d", event.data));
        });
        final RingBuffer<RingBufferReference<Integer>> ringBuffer = disruptor.start();
        for (int i = 0; i < msgCount; i++) {
            ringBuffer.publishEvent(this::setEvent, i);
        }
        // waits until all ring buffer events have been processed
        disruptor.shutdown();
        // waits until all tasks queued for the thread are done
        executor.shutdown();
        assertThat(msgReceivedCount.get(), is(msgCount));
    }

    private void setEvent(final RingBufferReference<Integer> event, final long sequence, final Integer data) {
        event.data = data;
    }

}
