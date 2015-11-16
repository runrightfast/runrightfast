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
package co.runrightfast.exceptions;

import lombok.Getter;
import lombok.NonNull;

/**
 * Tracks metrics.
 *
 * @author alfio
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    protected final ApplicationExceptionSeverity severity;

    public ApplicationException(@NonNull final ApplicationExceptionSeverity severity) {
        this.severity = severity;
    }

    public ApplicationException(@NonNull final ApplicationExceptionSeverity severity, @NonNull final String message) {
        super(message);
        this.severity = severity;
    }

    public ApplicationException(@NonNull final ApplicationExceptionSeverity severity, @NonNull final String message, @NonNull final Throwable cause) {
        super(message, cause);
        this.severity = severity;
    }

    public ApplicationException(@NonNull final ApplicationExceptionSeverity severity, @NonNull final Throwable cause) {
        super(cause);
        this.severity = severity;
    }

}
