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

import co.runrightfast.app.ArtifactId;
import co.runrightfast.app.Version;
import static co.runrightfast.commons.utils.ValidationUtils.notBlank;
import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author alfio
 */
@EqualsAndHashCode
public abstract class ArtifactIdImpl implements ArtifactId {

    @Getter
    protected final URI namespaceUri;

    @Getter
    protected final String name;

    @Getter
    protected final Version version;

    /**
     *
     * @param namespaceUri will be normalized
     * @param name must not be blank and will be trimmed
     * @param version artifact version
     */
    public ArtifactIdImpl(@NonNull final URI namespaceUri, final String name, @NonNull final Version version) {
        notBlank(name);
        this.namespaceUri = namespaceUri.normalize();
        this.name = name.trim();
        this.version = version;
    }

}
