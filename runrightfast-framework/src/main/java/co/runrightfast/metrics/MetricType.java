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
package co.runrightfast.metrics;

import static com.google.common.base.Preconditions.checkArgument;
import java.util.Arrays;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.Validate.notEmpty;

/**
 *
 * @author alfio
 */
public enum MetricType {

    COUNTER,
    GAUGE,
    METER,
    HISTOGRAM,
    TIMER;

    public static String metricName(@NonNull final MetricType metricType, final String... names) {
        notEmpty(names);
        checkArgument(!Arrays.stream(names).filter(StringUtils::isBlank).findFirst().isPresent(), "any of the names cannot be blank");
        final StringBuilder sb = new StringBuilder(64);
        sb.append(metricType.name());
        Arrays.stream(names).forEach(n -> sb.append('.').append(n));
        return sb.toString();
    }

    public String metricName(final String... names) {
        return metricName(this, names);
    }

}
