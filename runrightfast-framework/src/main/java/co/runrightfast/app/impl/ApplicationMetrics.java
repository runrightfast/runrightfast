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
package co.runrightfast.app.impl;

import co.runrightfast.exceptions.ApplicationException;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author alfio
 */
public final class ApplicationMetrics {

    @Getter
    private final MetricRegistry exceptionsMetricRegistry;

    public ApplicationMetrics() {
        exceptionsMetricRegistry = SharedMetricRegistries.getOrCreate("ApplicationMetrics.EXCEPTION_METRICS");
    }

    public void reportException(@NonNull final Throwable t) {
        recordExceptionOccurrence(t);
    }

    public void reportException(@NonNull final ApplicationException t) {
        recordExceptionOccurrence(t);
        final Meter meter = exceptionsMetricRegistry.meter(t.getSeverity().name());
        meter.mark();
    }

    private void recordExceptionOccurrence(final Throwable t) {
        final Meter meter = exceptionsMetricRegistry.meter(t.getClass().getName());
        meter.mark();
    }
}
