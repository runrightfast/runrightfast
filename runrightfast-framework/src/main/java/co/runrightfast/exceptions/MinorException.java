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

import static co.runrightfast.exceptions.ApplicationExceptionSeverity.MINOR;

/**
 * Indicates that the exception is minor and application should be able to recover.
 *
 * @author alfio
 */
public class MinorException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public MinorException() {
        super(MINOR);
    }

    public MinorException(final String message) {
        super(MINOR, message);
    }

    public MinorException(final String message, final Throwable cause) {
        super(MINOR, message, cause);
    }

    public MinorException(final Throwable cause) {
        super(MINOR, cause);
    }

}
