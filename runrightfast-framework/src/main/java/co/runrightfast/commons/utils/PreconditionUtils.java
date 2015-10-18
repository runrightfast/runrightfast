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

import static com.google.common.base.Preconditions.checkArgument;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author alfio
 */
public interface PreconditionUtils {

    static void greaterThanOrEqualZero(final int n, final String argName) {
        checkArgument(n >= 0, "%s must be >= 0");
    }

    static void greaterThanZero(final int n, final String argName) {
        checkArgument(n > 0, "%s must be > 0");
    }

    static void notBlank(final String val) {
        checkArgument(StringUtils.isNotBlank(val));
    }

    static void notEmpty(final Collection<?> coll) {
        checkArgument(CollectionUtils.isNotEmpty(coll));
    }

}
