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
package co.runrightfast.zest.composites.services.concurrent.reactor.config;

import lombok.NonNull;
import org.qi4j.api.configuration.ConfigurationComposite;
import org.qi4j.api.property.Property;
import org.qi4j.library.constraints.annotation.Range;
import reactor.Environment;
import reactor.jarjar.com.lmax.disruptor.util.Util;

/**
 *
 * @author alfio
 */
public interface RingBufferDispatcherConfig extends ConfigurationComposite {

    @Range(min = 2, max = 8096)
    Property<Integer> backlog();

    static int backlog(@NonNull final Property<Integer> backlog) {
        if (backlog.get() == null) {
            return Util.ceilingNextPowerOfTwo(Environment.PROCESSORS * 2);
        }
        return Util.ceilingNextPowerOfTwo(backlog.get());
    }
}
