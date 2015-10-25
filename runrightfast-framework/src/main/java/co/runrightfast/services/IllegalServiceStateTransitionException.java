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
package co.runrightfast.services;

import co.runrightfast.exceptions.MajorException;
import com.google.common.util.concurrent.Service;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author alfio
 */
@RequiredArgsConstructor
public class IllegalServiceStateTransitionException extends MajorException {

    private static final long serialVersionUID = 1L;

    @NonNull
    private final Service.State from;

    @NonNull
    private final Service.State to;

    @Override
    public String getMessage() {
        return String.format("service transition is not allowed: %s -> %s", from, to);
    }

}
