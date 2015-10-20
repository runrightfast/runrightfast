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
package co.runrightfast.logging;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.logging.Level;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import lombok.NonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 *
 * @author alfio
 */
public final class JsonLog {

    private static final Gson gson = new GsonBuilder().create();

    public static JsonLog newInfoLog(@NonNull final Logger logger, final String className) {
        return new JsonLog(logger, INFO, className);
    }

    public static JsonLog newWarningLog(@NonNull final Logger logger, final String className) {
        return new JsonLog(logger, WARNING, className);
    }

    public static JsonLog newErrorLog(@NonNull final Logger logger, final String className) {
        return new JsonLog(logger, SEVERE, className);
    }

    private final Logger logger;

    private final Level level;

    private final String className;

    public JsonLog(@NonNull final Logger logger, @NonNull final Level level, final String className) {
        checkArgument(isNotBlank(className));
        this.logger = logger;
        this.level = level;
        this.className = className;
    }

    public void log(final String method, @NonNull final Object message) {
        logger.logp(level, className, method, () -> gson.toJson(message));
    }

    public void log(final String method, @NonNull final Object message, @NonNull final Throwable exception) {
        logger.logp(level, className, method, exception, () -> gson.toJson(message));
    }
}
