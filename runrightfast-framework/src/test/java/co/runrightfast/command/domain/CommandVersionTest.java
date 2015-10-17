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

import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author alfio
 */
@Log
public class CommandVersionTest {

    /**
     * Test of isCompatible method, of class CommandVersion.
     */
    @Test
    public void testIsCompatible() {
        assertThat(new CommandVersion(1, 0, 0).isCompatible(1, 0), is(true));
        assertThat(new CommandVersion(1, 1, 0).isCompatible(1, 0), is(true));
        assertThat(new CommandVersion(1, 1, 1).isCompatible(1, 0), is(true));
        assertThat(new CommandVersion(1, 1, 1).isCompatible(1, 2), is(false));
        assertThat(new CommandVersion(1, 1, 1).isCompatible(2, 0), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMajorVersion() {
        new CommandVersion(0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinorVersion() {
        new CommandVersion(1, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidpatchVersion() {
        new CommandVersion(1, 1, -1);
    }

}
