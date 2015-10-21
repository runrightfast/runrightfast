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
import co.runrightfast.commons.disruptor.RingBufferReference;
import co.runrightfast.logging.JsonLog;
import co.runrightfast.rx.ObservableRingBuffer;
import com.google.common.util.concurrent.AbstractIdleService;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.java.Log;
import org.apache.commons.collections4.CollectionUtils;
import rx.Observable;
import rx.Subscriber;

/**
 *
 * @author alfio
 * @param <A> ring buffer item type
 */
@RequiredArgsConstructor
@Log
public class ObservableRingBufferImpl<A> extends AbstractIdleService implements ObservableRingBuffer<A> {

    private static final String CLAZZ = ObservableRingBufferImpl.class.getName();

    @NonNull
    private final DisruptorConfig<A> disruptorConfig;
    @NonNull
    private final Class<A> ringBufferDataType;

    private Disruptor<RingBufferReference<A>> disruptor;
    private RingBuffer<RingBufferReference<A>> ringBuffer;

    @Getter
    private Observable<A> observable;

    private final List<Subscriber<? super A>> subscribers = new CopyOnWriteArrayList<>();

    private static final JsonLog warning = JsonLog.newWarningLog(log, CLAZZ);

    @Value
    @Builder
    private static class LogMessage {

        String message;

        String type;

        long sequence;

    }

    @Override
    protected void startUp() {
        this.disruptor = disruptorConfig.newDisruptor(ringBufferDataType);
        this.observable = Observable.<A>create(this::observableOnSubscribe);
        this.disruptor.handleEventsWith(this::onEvent);
        this.ringBuffer = this.disruptor.start();
    }

    @Override
    protected void shutDown() {
        if (this.disruptor != null) {
            for (int i = 1; true; i++) {
                final int waitTimeSecs = 10;
                try {
                    this.disruptor.shutdown(waitTimeSecs, TimeUnit.SECONDS);
                    break;
                } catch (final TimeoutException ex) {
                    log.logp(Level.WARNING, CLAZZ, "shutDown", "waiting for disruptor to shutdown : {0} secs", i * waitTimeSecs);
                }
            }
        }

        if (this.subscribers != null) {
            this.subscribers.stream()
                    .filter(this::isSubscribed)
                    .forEach(subscriber -> subscriber.onCompleted());
        }

        this.disruptor = null;
        this.ringBuffer = null;
        this.subscribers.clear();
    }

    @Override
    public boolean publish(@NonNull final A msg) {
        return this.ringBuffer.tryPublishEvent(this::setRingBufferReferenceData, msg);
    }

    private void setRingBufferReferenceData(final RingBufferReference<A> msg, final long sequence, final A data) {
        msg.data = data;
    }

    private boolean isSubscribed(final Subscriber<? super A> subscriber) {
        return !subscriber.isUnsubscribed();
    }

    private void observableOnSubscribe(final Subscriber<? super A> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            this.subscribers.add(subscriber);
        }
    }

    // TODO: collect metrics - how many event were dropped, delivered, etc
    private void onEvent(final RingBufferReference<A> msg, final long sequence, final boolean endOfBatch) {
        if (CollectionUtils.isEmpty(subscribers)) {
            warning.log("onEvent", new LogMessage("message dropped because there are no observers", getClass().getName(), sequence));
            return;
        }

        for (final Subscriber<? super A> subscriber : subscribers) {
            if (subscriber.isUnsubscribed()) {
                this.subscribers.remove(subscriber);
                continue;
            }
            try {
                subscriber.onNext(msg.data);
            } catch (final Throwable t) {
                subscriber.onError(t);
                this.subscribers.remove(subscriber);
            }
        }

    }

    @Override
    public long remainingCapacity() {
        return ringBuffer.getBufferSize();
    }

    @Override
    public int observerCount() {
        return subscribers.size();
    }

    @Override
    public int bufferSize() {
        return ringBuffer.getBufferSize();
    }

    @Override
    public long cursor() {
        return ringBuffer.getCursor();
    }

    @Override
    public String toString() {
        if (!isRunning()) {
            return "{}";
        }
        return new JsonObject()
                .put("remainingCapacity", remainingCapacity())
                .put("observerCount", observerCount())
                .put("bufferSize", bufferSize())
                .put("cursor", cursor())
                .put("ringBufferDataType", ringBufferDataType.getName())
                .encodePrettily();
    }

}
