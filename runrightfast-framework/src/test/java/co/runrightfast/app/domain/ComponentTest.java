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
package co.runrightfast.app.domain;

import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class ComponentTest {

    @Test
    public void test() throws URISyntaxException {
        final Namespace ns = new Namespace(new URI("http://runrightfast.co"));
        final Component comp1 = new Component(ns, "comp1");

        log.info(comp1.toString());
        log.info(comp1.getNamespace().toString());

        assertThat(comp1.getUri(), is(new URI("http://runrightfast.co/comp1")));
    }

    @Test(expected = NullPointerException.class)
    public void testNamespaceIsRequired() {
        new Component(null, "comp1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameIsRequired() throws URISyntaxException {
        final Namespace ns = new Namespace(new URI("http://runrightfast.co"));
        final Component comp1 = new Component(ns, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameCannotBeBlank() throws URISyntaxException {
        final Namespace ns = new Namespace(new URI("http://runrightfast.co"));
        final Component comp1 = new Component(ns, " ");
    }

}
