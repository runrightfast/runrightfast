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

import static co.runrightfast.commons.utils.ValidationUtils.notBlank;
import co.runrightfast.exceptions.ShouldNeverHappenException;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author alfio
 */
public interface AppUtils {

    String JVM_ID = ManagementFactory.getRuntimeMXBean().getName();

    /**
     *
     * @param uri uri
     * @return normalized URI
     */
    static URI uri(final String uri) {
        notBlank(uri);
        try {
            return new URI(uri).normalize();
        } catch (final URISyntaxException ex) {
            throw new ShouldNeverHappenException(ex);
        }
    }
}
