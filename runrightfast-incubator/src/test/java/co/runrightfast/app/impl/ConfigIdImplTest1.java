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

import co.runrightfast.app.ConfigId;
import static co.runrightfast.app.impl.FactoryImpl.factory;
import static co.runrightfast.commons.utils.AppUtils.uri;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class ConfigIdImplTest1 {

    @Test
    public void test_newConfigId() {
        final ConfigId id = factory.configId(uri("http://www.runrightfast.co"), "  event-service   ", factory.version(1, 0, 0));
        log.info(String.format("appId: %s", id));

        assertThat(id.getNamespaceUri(), is(uri("http://www.runrightfast.co")));
        assertThat(id.getName(), is("event-service"));
        assertThat(id.getVersion(), is(factory.version(1, 0, 0)));

        final ConfigId id2 = factory.configId(uri("http://www.runrightfast.co"), "  event-service   ", factory.version(1, 0, 0));

        assertThat(id, is(id2));

        final Set<ConfigId> ids = new HashSet<>();
        ids.add(id);
        assertThat(ids.add(id2), is(false));
        assertThat(ids.add(factory.configId(uri("http://www.runrightfast.co"), "event-service", factory.version(1, 0, 0))), is(false));
        assertThat(ids.add(factory.configId(uri("http://www.runrightfast.co"), "event-service", factory.version(1, 1, 0))), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void test_newConfigId_withNullNamespaceURI() {
        factory.configId(null, "event-service", factory.version(1, 0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_newConfigId_withBlankName() {
        factory.configId(uri("http://www.runrightfast.co"), " ", factory.version(1, 0, 0));
    }

    @Test(expected = NullPointerException.class)
    public void test_newConfigId_withNullVersion() {
        factory.configId(uri("http://www.runrightfast.co"), "event-service", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_newConfigId_withInvalidVersion() {
        factory.configId(uri("http://www.runrightfast.co"), "event-service", factory.version(0, 1, 0));
    }

}
