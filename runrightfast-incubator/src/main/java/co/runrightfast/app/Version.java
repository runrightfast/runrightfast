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
package co.runrightfast.app;

import static co.runrightfast.commons.utils.AppUtils.uri;
import static com.google.common.base.Preconditions.checkNotNull;
import java.net.URI;

/**
 *
 * @author alfio
 */
public interface Version extends Comparable<Version> {

    int getMajorVersion();

    int getMinorVersion();

    int getPatchVersion();

    /**
     * The specified version is compatible with this version if the major versions match and the specified minor version is less than or equal to this minor
     * version.
     *
     * @param version CpmmandVersion
     * @return true if the specified version is compatible with this version
     */
    default boolean isCompatible(final Version version) {
        checkNotNull(version);
        return isCompatible(version.getMajorVersion(), version.getMinorVersion());
    }

    /**
     * The specified version is compatible with this version if the major versions match and the specified minor version is less than or equal to this minor
     * version.
     *
     * @param majorVersion major version
     * @param minorVersion minor version
     * @return true if the specified version is compatible with this version
     */
    default boolean isCompatible(final int majorVersion, final int minorVersion) {
        return majorVersion == getMajorVersion() && minorVersion <= getMinorVersion();
    }

    default URI toURI() {
        return uri(new StringBuilder()
                .append('/')
                .append(getMajorVersion()).append('/')
                .append(getMinorVersion()).append('/')
                .append(getPatchVersion())
                .toString()
        );
    }

    @Override
    default int compareTo(final Version other) {
        if (other == null) {
            return -1;
        }

        if (this.getMajorVersion() == other.getMajorVersion()) {
            if (this.getMinorVersion() == other.getMinorVersion()) {
                return this.getPatchVersion() - other.getPatchVersion();
            }
            return this.getMinorVersion() - other.getMinorVersion();
        }

        return this.getMajorVersion() - other.getMajorVersion();

    }

}
