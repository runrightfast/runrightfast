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
package co.runrightfast.app;

import static co.runrightfast.commons.utils.AppUtils.uri;
import java.net.URI;

/**
 *
 * @author alfio
 */
public interface ArtifactId {

    URI getNamespaceUri();

    String getName();

    Version getVersion();

    default URI toUri() {
        return uri(new StringBuilder(128)
                .append(getNamespaceUri()).append('/')
                .append(getName())
                .append(getVersion().toURI())
                .toString()
        ).normalize();
    }

}
