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
package co.runrightfast.commons.utils;

import java.util.Collection;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author alfio
 */
public interface ValidationUtils {

    static void greaterThanOrEqualZero(final int n) {
        Validate.isTrue(n >= 0);
    }

    static void greaterThanOrEqualZero(final int n, final String argName) {
        Validate.isTrue(n >= 0, "%s must be >= 0", argName);
    }

    static void greaterThanZero(final int n) {
        Validate.isTrue(n > 0);
    }

    static void greaterThanZero(final int n, final String argName) {
        Validate.isTrue(n > 0, "%s must be > 0", argName);
    }

    static void notBlank(final String val) {
        Validate.notBlank(val);
    }

    static void notBlank(final String val, @NonNull final String argName) {
        Validate.notBlank(val, "%s cannot be blank", argName);
    }

    static void notEmpty(final Collection<?> coll) {
        Validate.notEmpty(coll);
    }

    static void notEmpty(final Collection<?> coll, final String argName) {
        Validate.notEmpty(coll, "%s cannot be empty", argName);
    }

    static void noNullElements(final Object[] values) {
        Validate.noNullElements(values);
    }

}
