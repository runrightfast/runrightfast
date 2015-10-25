/* Copyright (C) RunRightFast.co - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Alfio Zappala azappala@azaptree.com, March 2014
 */
package co.runrightfast.incubator.commons.disruptor;

import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author alfio
 * @param <DATA> reference to RingBuffer
 */
public final class RingBufferReference<DATA> {

    public DATA data;

    @Override
    public String toString() {
        if (data != null) {
            return data.toString();
        } else {
            return "NO DATA";
        }
    }

    /**
     * Uses ForkJoinPool.commonPool() as the Executor
     *
     * @param <A> the type of data stored in the RingBuffer
     * @param dataClass used to specify the type parameter
     * @param ringBufferSize ring buffer size
     * @return Disruptor
     */
    public static <A> Disruptor<RingBufferReference<A>> disruptor(final Class<A> dataClass, final int ringBufferSize) {
        return new Disruptor<>(RingBufferReference<A>::new, ringBufferSize, ForkJoinPool.commonPool());
    }

    /**
     * Uses ForkJoinPool.commonPool() as the Executor
     *
     * @param <A> the type of data stored in the RingBuffer
     * @param dataClass used to specify the type parameter
     * @param ringBufferSize ring buffer size
     * @param producerType producer type
     * @param waitStrategy wait strategy
     * @return Disruptor
     */
    public static <A> Disruptor<RingBufferReference<A>> disruptor(
            final Class<A> dataClass,
            final int ringBufferSize,
            final ProducerType producerType,
            final WaitStrategy waitStrategy
    ) {
        return new Disruptor<>(RingBufferReference<A>::new, ringBufferSize, ForkJoinPool.commonPool(), producerType, waitStrategy);
    }

    public static <A> Disruptor<RingBufferReference<A>> disruptor(
            final Class<A> dataClass,
            final int ringBufferSize,
            final ProducerType producerType,
            final WaitStrategy waitStrategy,
            final Executor executor
    ) {
        return new Disruptor<>(RingBufferReference<A>::new, ringBufferSize, executor, producerType, waitStrategy);
    }

}
