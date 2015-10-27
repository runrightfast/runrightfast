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
package co.runrightfast.component.events.impl;

import co.runrightfast.app.ComponentId;
import co.runrightfast.commons.utils.AppUtils;
import co.runrightfast.component.events.ComponentEvent;
import co.runrightfast.component.events.Event;
import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.collect.ImmutableSet;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

/**
 *
 * @author alfio
 * @param <DATA> event data type
 */
@Builder
@Value
public final class ComponentEventImpl<DATA> implements ComponentEvent<DATA> {

    ComponentId componentId;

    UUID id;

    Instant eventTimestamp;

    String jvmId;

    @NonNull
    Event<DATA> event;

    Optional<DATA> data;

    Optional<Throwable> exception;

    Optional<Set<String>> tags;

    /**
     *
     * @param componentId required
     * @param id optional - new id generated if not supplied
     * @param eventTimestamp optional - uses now if not specified
     * @param jvmId optional - uses {@link AppUtils#JVM_ID} if not specified
     * @param event required
     * @param data conditionally required based on event
     * @param exception optional
     * @param tags optional
     */
    public ComponentEventImpl(
            @NonNull final ComponentId componentId,
            final UUID id,
            final Instant eventTimestamp,
            final String jvmId,
            @NonNull final Event<DATA> event,
            final Optional<DATA> data,
            final Optional<Throwable> exception,
            final Optional<Set<String>> tags) {

        if (event.eventDataType().isPresent()) {
            checkArgument(data != null && data.isPresent(), "event is expecting data : event=%s", event.name());
            checkArgument(
                    TypeUtils.isInstance(data.get(), event.eventDataType().get()),
                    "event data type does not match : event=%s, expected=%s, actual=%s", event.name(), event.eventDataType().get(), data.get().getClass()
            );
        } else {
            checkArgument(data == null || !data.isPresent(), "event is not expecting data : event=%s", event.name());
        }

        this.componentId = componentId;
        this.id = id != null ? id : UUID.randomUUID();
        this.eventTimestamp = eventTimestamp != null ? eventTimestamp : Instant.now();
        this.jvmId = StringUtils.isNotBlank(jvmId) ? jvmId : AppUtils.JVM_ID;
        this.event = event;
        this.data = data != null ? data : Optional.empty();
        this.exception = exception != null ? exception : Optional.empty();
        this.tags = tags != null ? tags : Optional.empty();
    }

    public ComponentEventImpl(
            @NonNull final ComponentId componentId,
            @NonNull final Event<DATA> event) {
        this(componentId, UUID.randomUUID(), Instant.now(), AppUtils.JVM_ID, event, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public ComponentEventImpl(
            @NonNull final ComponentId componentId,
            @NonNull final Event<DATA> event,
            @NonNull final DATA data) {
        this(componentId, UUID.randomUUID(), Instant.now(), AppUtils.JVM_ID, event, Optional.of(data), Optional.empty(), Optional.empty());
    }

    public ComponentEventImpl(
            @NonNull final ComponentId componentId,
            @NonNull final Event<DATA> event,
            @NonNull final DATA data,
            @NonNull final Throwable exception) {
        this(componentId, UUID.randomUUID(), Instant.now(), AppUtils.JVM_ID, event, Optional.of(data), Optional.of(exception), Optional.empty());
    }

    public ComponentEventImpl(
            @NonNull final ComponentId componentId,
            @NonNull final Event<DATA> event,
            @NonNull final DATA data,
            @NonNull final Throwable exception,
            String... tags) {
        this(
                componentId,
                UUID.randomUUID(),
                Instant.now(),
                AppUtils.JVM_ID,
                event,
                Optional.of(data),
                Optional.of(exception),
                ArrayUtils.isNotEmpty(tags) ? Optional.of(ImmutableSet.copyOf(tags)) : Optional.empty()
        );
    }

}
