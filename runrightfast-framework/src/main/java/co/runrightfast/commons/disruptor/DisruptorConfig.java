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
package co.runrightfast.commons.disruptor;

import static co.runrightfast.commons.utils.PreconditionUtils.greaterThanOrEqualZero;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import lombok.Builder;
import lombok.Value;

/**
 *
 * @author alfio
 */
@Builder
@Value
public class DisruptorConfig<A> {

    int ringBufferSize;
    ProducerType producerType;
    WaitStrategy waitStrategy;
    Executor executor;

    public static final WaitStrategy DEFAULT_WaitStrategy = new SleepingWaitStrategy(5);

    private static final DisruptorConfig DEFAULT_ObservableRingBufferConfig = new DisruptorConfigBuilder<>().build();

    public DisruptorConfig(
            final int ringBufferSize,
            final ProducerType producerType,
            final WaitStrategy waitStrategy,
            final Executor executor) {
        greaterThanOrEqualZero(ringBufferSize, "rinfBufferSize");
        this.ringBufferSize = Util.ceilingNextPowerOfTwo(ringBufferSize > 0 ? ringBufferSize : 1024);
        this.producerType = producerType != null ? producerType : ProducerType.MULTI;
        this.waitStrategy = waitStrategy != null ? waitStrategy : DEFAULT_WaitStrategy;
        this.executor = executor != null ? executor : ForkJoinPool.commonPool();
    }

    /**
     * <ul>
     * <li>ringBufferSize = 1024
     * <li>producerType = ProducerType.MULTI
     * <li>waitStrategy = new SleepingWaitStrategy(5) - The SleepingWaitStrategy it attempts to be conservative with CPU usage, by using a simple busy wait
     * loop, but uses a call to LockSupport.parkNanos(1) in the middle of the loop. On a typical Linux system this will pause the thread for around 60µs.
     * However it has the benefit that the producing thread does not need to take any action other increment the appropriate counter and does not require the
     * cost of signalling a condition variable. However, the mean latency of moving the event between the producer and consumer threads will be higher. It works
     * best in situations where low latency is not required, but a low impact on the producing thread is desired. A common use case is for asynchronous logging.
     * <li>executor = ForkJoinPool.commonPool()
     * </ul>
     *
     * @param <A> ring buffer item type
     * @return default config
     */
    public static <A> DisruptorConfig<A> getDefaultConfig() {
        return DEFAULT_ObservableRingBufferConfig;
    }

    public <A> Disruptor<RingBufferReference<A>> newDisruptor(final Class<A> ringBufferType) {
        return RingBufferReference.disruptor(
                ringBufferType,
                ringBufferSize,
                producerType,
                waitStrategy,
                executor);
    }

}
