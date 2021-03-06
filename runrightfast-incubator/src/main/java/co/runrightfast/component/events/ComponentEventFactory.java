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
package co.runrightfast.component.events;

import co.runrightfast.app.ComponentId;

/**
 * All events generated by this event factory are for a specific component.
 *
 * @author alfio
 */
public interface ComponentEventFactory {

    /**
     *
     * @return ComponentId used for all {@link ComponentEvent} instances created using this factory.
     */
    ComponentId getComponentId();

    ComponentEvent componentEvent(Event event, String... tags);

    ComponentEvent componentEvent(Event event, Throwable exception, String... tags);

    <DATA> ComponentEvent<DATA> componentEvent(Event<DATA> event, Class<DATA> eventDataType, DATA data, String... tags);

    <DATA> ComponentEvent<DATA> componentEvent(Event<DATA> event, Class<DATA> eventDataType, DATA data, Throwable exception, String... tags);

}
