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
package co.runrightfast.command.domain;

import co.runrightfast.app.domain.Version;
import co.runrightfast.exceptions.ShouldNeverHappenException;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.Value;

/**
 *
 * @author alfio
 */
@Value
public class CommandId {

    String catalog;

    String name;

    Version version;

    /**
     *
     * @return uri format : {catalog}/{name}/{version.major}/{version.minor}
     */
    public URI getCommandURI() {
        try {
            return new URI(new StringBuilder(catalog).append('/')
                    .append(name).append('/')
                    .append(version.getMajor()).append('/')
                    .append(version.getMinor())
                    .toString());
        } catch (final URISyntaxException ex) {
            throw new ShouldNeverHappenException(ex);
        }
    }

}
