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
package co.runrightfast.rx;

import co.runrightfast.commons.disruptor.DisruptorConfig;
import co.runrightfast.commons.disruptor.RingBufferReference;
import com.google.common.util.concurrent.AbstractIdleService;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author alfio
 */
@RequiredArgsConstructor
public class ObservableRingBuffer<A> extends AbstractIdleService {

    private Disruptor<RingBufferReference<A>> disruptor;
    private RingBuffer<RingBufferReference<A>> ringBuffer;

    @NonNull
    private final DisruptorConfig<A> disruptorConfig;
    private final Class<A> clazz;

    @Override
    protected void startUp() {
        this.disruptor = disruptorConfig.newDisruptor(clazz);
    }

    @Override
    protected void shutDown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void publish(@NonNull final A msg) {

    }

}
