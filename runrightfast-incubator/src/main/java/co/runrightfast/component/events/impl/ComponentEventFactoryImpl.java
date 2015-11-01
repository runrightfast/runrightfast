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
import co.runrightfast.component.events.ComponentEvent;
import co.runrightfast.component.events.ComponentEventFactory;
import co.runrightfast.component.events.Event;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author alfio
 */
@RequiredArgsConstructor
public class ComponentEventFactoryImpl implements ComponentEventFactory {

    @NonNull
    @Getter
    private final ComponentId componentId;

    @Override
    public ComponentEvent componentEvent(@NonNull final Event event, final String... tags) {
        final ComponentEventImpl.ComponentEventImplBuilder builder = ComponentEventImpl.builder()
                .componentId(componentId)
                .event(event);

        if (ArrayUtils.isNotEmpty(tags)) {
            return builder.tags(Optional.of(ImmutableSet.copyOf(tags))).build();
        }

        return builder.build();
    }

    @Override
    public ComponentEvent componentEvent(@NonNull final Event event, @NonNull final Throwable exception, final String... tags) {
        final ComponentEventImpl.ComponentEventImplBuilder builder = ComponentEventImpl.builder()
                .componentId(componentId)
                .event(event)
                .exception(Optional.of(exception));

        if (ArrayUtils.isNotEmpty(tags)) {
            return builder.tags(Optional.of(ImmutableSet.copyOf(tags))).build();
        }

        return builder.build();
    }

    @Override
    public <DATA> ComponentEvent<DATA> componentEvent(@NonNull final Event<DATA> event, @NonNull final Class<DATA> eventDataType, @NonNull final DATA data, @NonNull final String... tags) {
        final ComponentEventImpl.ComponentEventImplBuilder<DATA> builder = ComponentEventImpl.<DATA>builder()
                .componentId(componentId)
                .event(event)
                .data(Optional.of(data));

        if (ArrayUtils.isNotEmpty(tags)) {
            return builder.tags(Optional.of(ImmutableSet.copyOf(tags))).build();
        }

        return builder.build();
    }

    @Override
    public <DATA> ComponentEvent<DATA> componentEvent(@NonNull final Event<DATA> event, @NonNull final Class<DATA> eventDataType, @NonNull final DATA data, @NonNull final Throwable exception, String... tags) {
        final ComponentEventImpl.ComponentEventImplBuilder<DATA> builder = ComponentEventImpl.<DATA>builder()
                .componentId(componentId)
                .event(event)
                .data(Optional.of(data))
                .exception(Optional.of(exception));

        if (ArrayUtils.isNotEmpty(tags)) {
            return builder.tags(Optional.of(ImmutableSet.copyOf(tags))).build();
        }

        return builder.build();
    }

}
