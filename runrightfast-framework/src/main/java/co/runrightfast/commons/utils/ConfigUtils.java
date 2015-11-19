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

import com.google.gson.JsonObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigRenderOptions;
import lombok.NonNull;

/**
 *
 * @author alfio
 */
public final class ConfigUtils {

    private ConfigUtils() {
    }

    public static String renderConfig(@NonNull final Config config, final boolean comments, final boolean orginComments, final boolean json) {
        final ConfigRenderOptions options = ConfigRenderOptions.defaults()
                .setComments(comments)
                .setOriginComments(orginComments)
                .setJson(json)
                .setFormatted(true);
        return config.root().render(options);
    }

    public static JsonObject toJsonObject(@NonNull final Config config) {
        return JsonUtils.gson.fromJson(renderConfigAsJson(config, false), JsonObject.class);
    }

    public static String renderConfigAsJson(@NonNull final Config config, final boolean formatted) {
        final ConfigRenderOptions options = ConfigRenderOptions.defaults()
                .setComments(false)
                .setOriginComments(false)
                .setJson(true)
                .setFormatted(formatted);
        return config.root().render(options);
    }

}
