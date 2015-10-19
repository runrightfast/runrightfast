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

import static co.runrightfast.exceptions.ApplicationExceptionSeverity.MAJOR;

/**
 * If thrown, it indicates that the exception is major and requires attention.
 *
 * @author alfio
 */
public class MajorException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public MajorException() {
        super(MAJOR);
    }

    public MajorException(final String message) {
        super(MAJOR, message);
    }

    public MajorException(final String message, final Throwable cause) {
        super(MAJOR, message, cause);
    }

    public MajorException(final Throwable cause) {
        super(MAJOR, cause);
    }

}
