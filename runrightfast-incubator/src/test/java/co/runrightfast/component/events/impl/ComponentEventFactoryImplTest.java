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
import static co.runrightfast.app.impl.FactoryImpl.factory;
import co.runrightfast.commons.utils.AppUtils;
import static co.runrightfast.commons.utils.AppUtils.uri;
import co.runrightfast.component.events.ComponentEvent;
import co.runrightfast.component.events.ComponentEventFactory;
import co.runrightfast.component.events.Event;
import co.runrightfast.component.events.EventLevel;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.Value;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class ComponentEventFactoryImplTest {

    @Value
    public static class EventData {

        String foo;
    }

    static enum TestEvent implements Event<EventData> {

        EVENT_1(EventLevel.ERROR);

        private final EventLevel level;

        private TestEvent(final EventLevel level) {
            this.level = level;
        }

        @Override
        public EventLevel eventLevel() {
            return level;
        }

    }

    @Test
    public void testComponentEvent() {
        final Event event = TestEvent.EVENT_1;
        String[] tags = {"FOO", "BAR"};

        final ComponentId componentId = factory.componentId(uri("http://www.runrightfast.co"), "  event-service   ", factory.version(1, 0, 0));
        final ComponentEventFactory factory = new ComponentEventFactoryImpl(componentId);

        final ComponentEvent compEvent1 = factory.componentEvent(event, tags);
        assertThat(compEvent1.getComponentId(), is(componentId));
        assertThat(compEvent1.getTags().isPresent(), is(true));

        assertThat(compEvent1.getTags().get(), is(ImmutableSet.copyOf(tags)));
        final Set<String> eventTags = (Set<String>) compEvent1.getTags().get();
        assertThat(eventTags, is(ImmutableSet.copyOf(tags)));
        assertThat(compEvent1.getJvmId(), is(AppUtils.JVM_ID));
        assertThat(compEvent1.getEventTimestamp(), is(notNullValue()));
        assertThat(compEvent1.getId(), is(notNullValue()));

        // TODO - more tests
    }

    @Test(expected = NullPointerException.class)
    public void testComponentEvent_withNullComponentId() {
        new ComponentEventFactoryImpl(null);
    }

}
