/*
 Copyright 2015 Alfio Zappala

 Licensed under the Apache License, VersionImpl 2.0 (the "License");
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

import co.runrightfast.app.Version;
import static co.runrightfast.commons.utils.AppUtils.uri;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class VersionImplTest {

    @Test
    public void testIsCompatible() {
        assertThat(new VersionImpl(1, 0, 0).isCompatible(1, 0), is(true));
        assertThat(new VersionImpl(1, 1, 0).isCompatible(1, 0), is(true));
        assertThat(new VersionImpl(1, 1, 1).isCompatible(1, 0), is(true));
        assertThat(new VersionImpl(1, 1, 1).isCompatible(1, 2), is(false));
        assertThat(new VersionImpl(1, 1, 1).isCompatible(2, 0), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMajorVersionImpl() {
        new VersionImpl(0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinorVersionImpl() {
        new VersionImpl(1, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidpatchVersionImpl() {
        new VersionImpl(1, 1, -1);
    }

    @Test
    public void testToUri() {
        final Version version = new VersionImpl(1, 0, 0);
        assertThat(version.toURI(), is(uri("/1/0/0")));
    }

    @Test
    public void testToString() {
        final Version version = new VersionImpl(1, 0, 0);
        log.info(version.toString());
    }

    @Test
    public void testHashCode() {
        final Version version = new VersionImpl(1, 0, 0);
        assertThat(new VersionImpl(1, 0, 0).equals(new VersionImpl(1, 0, 0)), is(true));
        assertThat(new VersionImpl(1, 0, 0).hashCode() == new VersionImpl(1, 0, 0).hashCode(), is(true));

        assertThat(new VersionImpl(1, 0, 0).equals(new VersionImpl(1, 0, 1)), is(false));
        assertThat(new VersionImpl(1, 0, 0).hashCode() == new VersionImpl(1, 0, 1).hashCode(), is(false));
    }

    @Test
    public void testCompareTo() {
        assertThat(new VersionImpl(1, 0, 0).compareTo(null) < 0, is(true));
        assertThat(new VersionImpl(1, 0, 0).compareTo(new VersionImpl(1, 0, 0)) == 0, is(true));
        assertThat(new VersionImpl(1, 0, 0).compareTo(new VersionImpl(1, 0, 1)) < 0, is(true));
        assertThat(new VersionImpl(1, 0, 0).compareTo(new VersionImpl(1, 1, 0)) < 0, is(true));
        assertThat(new VersionImpl(1, 0, 0).compareTo(new VersionImpl(2, 0, 0)) < 0, is(true));
        assertThat(new VersionImpl(2, 0, 0).compareTo(new VersionImpl(1, 1, 0)) > 0, is(true));
    }

}
