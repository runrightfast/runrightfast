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

import org.junit.Test;

/**
 *
 * @author alfio
 */
public class ValidationUtilsTest {

    @Test
    public void testGreaterThanOrEqualZero() {
        ValidationUtils.greaterThanOrEqualZero(1, "a");
        ValidationUtils.greaterThanOrEqualZero(0, "a");
    }

    @Test
    public void testGreaterThanZero() {
        ValidationUtils.greaterThanZero(1, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanOrEqualZero_invalid() {
        ValidationUtils.greaterThanOrEqualZero(-1, "a");
    }

    /**
     * Test of greaterThanZero method, of class ValidationUtils.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanZero_invalid() {
        ValidationUtils.greaterThanZero(0, "a");
    }

}
