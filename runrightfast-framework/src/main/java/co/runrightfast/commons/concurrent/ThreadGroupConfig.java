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
package co.runrightfast.commons.concurrent;

import static co.runrightfast.commons.utils.PreconditionUtils.notBlank;
import java.util.Optional;
import lombok.Builder;
import lombok.Value;

/**
 *
 * @author alfio
 */
@Value
@Builder
public class ThreadGroupConfig {

    String name;

    boolean daemon;

    Optional<Integer> maxPriority;

    Optional<String> parentThreadGroup;

    public ThreadGroupConfig(final String name, final boolean daemon, final Optional<Integer> maxPriority, final Optional<String> parent) {
        notBlank(name, "name");
        this.name = name;
        this.daemon = daemon;
        this.maxPriority = maxPriority != null ? maxPriority : Optional.empty();
        this.parentThreadGroup = parent != null ? parent : Optional.empty();
    }

}
