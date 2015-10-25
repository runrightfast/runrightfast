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

import java.util.Optional;

/**
 * Event names should be unique within the component's scope.
 *
 * Enums should be created for all events. The enums would implement this interface.
 *
 *
 * @author alfio
 * @param <DATA> event data type. The data should be easily convertible to JSON using Gson.
 */
public interface Event<DATA> {

    String name();

    EventLevel eventLevel();

    /**
     * Not all events may have data.
     *
     * @return by default
     */
    default Optional<Class<DATA>> eventDataType() {
        return Optional.empty();
    }
}
