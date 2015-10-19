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

/**
 *
 * @author alfio
 */
public enum ApplicationExceptionSeverity {

    /**
     * indicates that the exception requires immediate attention.
     */
    CRITICAL,
    /**
     * indicates that the exception requires attention, but is less critical
     */
    MAJOR,
    /**
     * indicates that the exception should be investigated, but is lower priority
     */
    MINOR

}
