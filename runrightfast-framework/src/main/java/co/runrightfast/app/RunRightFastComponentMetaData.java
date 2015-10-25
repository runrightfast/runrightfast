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
package co.runrightfast.app;

import co.runrightfast.component.alerts.Alert;
import co.runrightfast.component.events.Event;
import java.util.Set;

/**
 *
 * @author alfio
 */
public interface RunRightFastComponentMetaData {

    /**
     *
     * @return the types of events that the component can emit
     */
    Set<Event> getEvents();

    /**
     * All alerts must reference an event defined by {@link #getEvents() }
     *
     * @return the alerts that can triggered from events
     */
    Set<Alert> getAlerts();
}
