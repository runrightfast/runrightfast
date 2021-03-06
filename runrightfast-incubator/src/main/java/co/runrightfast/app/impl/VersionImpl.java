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

import co.runrightfast.app.Version;
import static co.runrightfast.commons.utils.ValidationUtils.greaterThanOrEqualZero;
import static co.runrightfast.commons.utils.ValidationUtils.greaterThanZero;
import lombok.Value;

/**
 *
 * @author alfio
 */
@Value
public class VersionImpl implements Version {

    int majorVersion;

    int minorVersion;

    int patchVersion;

    public VersionImpl(final int major, final int minor, final int patch) {
        greaterThanZero(major, "major");
        greaterThanOrEqualZero(minor, "minor");
        greaterThanOrEqualZero(patch, "patch");
        this.majorVersion = major;
        this.minorVersion = minor;
        this.patchVersion = patch;
    }
}
