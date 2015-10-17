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

import static co.runrightfast.commons.utils.PreconditionUtils.greaterThanOrEqualZero;
import static co.runrightfast.commons.utils.PreconditionUtils.greaterThanZero;
import static com.google.common.base.Preconditions.checkNotNull;
import lombok.Value;

/**
 *
 * @author alfio
 */
@Value
public class CommandVersion {

    int major;

    int minor;

    int patch;

    public CommandVersion(final int major, final int minor, final int patch) {
        greaterThanZero(major, "major");
        greaterThanOrEqualZero(minor, "minor");
        greaterThanOrEqualZero(patch, "patch");
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * The specified version is compatible with this version if the major versions match and the specified minor version is less than or equal to this minor
     * version.
     *
     * @param version CpmmandVersion
     * @return true if the specified version is compatible with this version
     */
    public boolean isCompatible(final CommandVersion version) {
        checkNotNull(version);
        return isCompatible(version.major, version.minor);
    }

    /**
     * The specified version is compatible with this version if the major versions match and the specified minor version is less than or equal to this minor
     * version.
     *
     * @param majorVersion major version
     * @param minorVersion minor version
     * @return true if the specified version is compatible with this version
     */
    public boolean isCompatible(final int majorVersion, final int minorVersion) {
        return majorVersion == major && minorVersion <= minor;
    }

}
