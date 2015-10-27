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

import co.runrightfast.app.ApplicationId;
import co.runrightfast.app.ComponentId;
import co.runrightfast.app.ConfigId;
import co.runrightfast.app.Factory;
import co.runrightfast.app.Version;
import java.net.URI;

/**
 *
 * @author alfio
 */
public final class FactoryImpl implements Factory {

    public static final Factory factory = new FactoryImpl();

    private FactoryImpl() {
    }

    @Override
    public ApplicationId applicationId(final URI namespaceUri, final String name, final Version version) {
        return new ApplicationIdImpl(namespaceUri, name, version);
    }

    @Override
    public ComponentId componentId(final URI namespaceUri, final String name, final Version version) {
        return new ComponentIdImpl(namespaceUri, name, version);
    }

    @Override
    public ConfigId configId(final URI namespaceUri, final String name, final Version version) {
        return new ConfigIdImpl(namespaceUri, name, version);
    }

    @Override
    public Version version(final int major, final int minor, final int patch) {
        return new VersionImpl(major, minor, patch);
    }

}
