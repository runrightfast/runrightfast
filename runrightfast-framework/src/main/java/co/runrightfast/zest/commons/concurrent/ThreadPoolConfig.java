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

import org.qi4j.api.common.Optional;
import org.qi4j.api.configuration.ConfigurationComposite;
import org.qi4j.api.property.Property;
import org.qi4j.library.constraints.annotation.GreaterThan;
import org.qi4j.library.constraints.annotation.NotEmpty;
import org.qi4j.library.constraints.annotation.Range;

/**
 * A thread pool maps to a ThreadGroup
 *
 * @author alfio
 */
public interface ThreadPoolConfig extends ConfigurationComposite {

    /**
     * The pool name corresponds to the name of the ThreadGroup that the Thread belongs to.
     *
     * @return pool name
     */
    @NotEmpty
    Property<String> threadPoolName();

    @GreaterThan(0)
    Property<Integer> corePoolSize();

    @GreaterThan(0)
    Property<Integer> maxPoolSize();

    /**
     * Sets the time limit for which threads may remain idle before being terminated.
     *
     * @return
     */
    Property<DurationTime> keepAliveTime();

    @Optional
    @NotEmpty
    Property<String> parentThreadPoolName();

    /**
     *
     * @return if null, then daemon = false
     */
    @Optional
    Property<Boolean> daemon();

    @Optional
    @Range(min = Thread.MIN_PRIORITY, max = Thread.MAX_PRIORITY)
    Property<Integer> threadPriority();

    /**
     *
     * @return if null, then default value is false
     */
    @Optional
    Property<Boolean> allowCoreThreadTimeOut();

}
