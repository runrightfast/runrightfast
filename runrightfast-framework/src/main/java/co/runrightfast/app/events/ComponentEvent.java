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
package co.runrightfast.app.events;

import co.runrightfast.app.ComponentId;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author alfio
 * @param <DATA> event data type
 */
public interface ComponentEvent<DATA> {

    ComponentId getComponentId();

    Event<DATA> getEvent();

    /**
     * Used to lookup a specific
     *
     * @return UUID
     */
    UUID getId();

    Instant getEventTimestamp();

    String getJvmId();

    /**
     * Can be used to group events into categories or relate events.
     *
     * For example, a user session id can be added as a tag.
     *
     * @return optional set of tags
     */
    Optional<Set<String>> getTags();

    Optional<Throwable> getException();

    Optional<DATA> getData();

}
