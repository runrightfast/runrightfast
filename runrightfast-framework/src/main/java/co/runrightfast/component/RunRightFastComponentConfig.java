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
package co.runrightfast.component;

import com.typesafe.config.Config;
import java.net.URI;
import java.util.Optional;
import lombok.NonNull;
import lombok.Value;

/**
 *
 * @author alfio
 */
@Value
public class RunRightFastComponentConfig {

    @NonNull
    URI componentVersionUri;

    @NonNull
    Optional<Config> config;

    /**
     * meant to store sensitive config info that we don't want to share, e.g., database credentials
     */
    @NonNull
    Optional<Config> privateConfig;

}
