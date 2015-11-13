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
package co.runrightfast.akka;

import static co.runrightfast.commons.utils.ValidationUtils.notBlank;
import java.util.Arrays;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author alfio
 */
public interface AkkaUtils {

    public static final String USER = "/user";

    public static final String WILDCARD = "*";

    /**
     * Concatenates the path via path separator : '/'
     *
     *
     * @param basePath base path
     * @param path appended to base path
     * @param paths optional additional paths
     * @return actor path
     */
    static String actorPath(final String basePath, final String path, final String... paths) {
        notBlank(basePath, "basePath");
        notBlank(path, "path");
        if (paths != null) {
            Validate.noNullElements(paths);
        }

        final StringBuilder sb = new StringBuilder(128)
                .append(basePath)
                .append('/')
                .append(path);

        if (paths != null) {
            Arrays.stream(paths).forEach(p -> sb.append('/').append(p));
        }

        return sb.toString();
    }

}
