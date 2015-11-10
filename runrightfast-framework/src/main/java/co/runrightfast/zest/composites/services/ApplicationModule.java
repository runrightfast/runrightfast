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
package co.runrightfast.zest.composites.services;

import lombok.NonNull;
import org.qi4j.api.property.Property;

/**
 *
 * @author alfio
 */
public interface ApplicationModule {

    Property<String> applicationName();

    Property<String> applicationVersion();

    Property<String> layerName();

    Property<String> moduleName();

    static String uri(@NonNull final ApplicationModule appModule) {
        final String appName = appModule.applicationName().get();
        final String appVersion = appModule.applicationVersion().get();
        final String layer = appModule.layerName().get();
        final String module = appModule.moduleName().get();
        final int length = appName.length() + appVersion.length() + layer.length() + module.length();
        return new StringBuilder(length + 4)
                .append('/').append(appName)
                .append('/').append(appVersion)
                .append('/').append(layer)
                .append('/').append(module)
                .toString();
    }

}
