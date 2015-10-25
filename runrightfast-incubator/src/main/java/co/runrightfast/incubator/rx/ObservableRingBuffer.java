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
package co.runrightfast.incubator.rx;

import com.google.common.util.concurrent.Service;
import rx.Observable;

/**
 *
 * @author alfio
 * @param <A> Ring buffer item type
 */
public interface ObservableRingBuffer<A> extends Service {

    Observable<A> getObservable();

    /**
     *
     * @param msg message
     * @return true if the value was published, false if there was insufficient capacity.
     */
    boolean publish(A msg);

    long remainingCapacity();

    int bufferSize();

    /**
     * Get the current cursor value for the ring buffer. The cursor value is the last value that was published, or the highest available sequence that can be
     * consumed.
     *
     * @return current cursor value
     */
    long cursor();

    int observerCount();

}
