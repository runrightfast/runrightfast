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

import com.google.common.util.concurrent.AbstractIdleService;
import static java.util.logging.Level.INFO;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

/**
 *
 * @author alfio
 */
@RequiredArgsConstructor
@Log
public abstract class RunRightFastComponent extends AbstractIdleService {

    @Getter
    @NonNull
    protected final RunRightFastComponentConfig config;

    @Override
    protected String serviceName() {
        return config.getComponentVersionUri().toString();
    }

    @Override
    protected void startUp() throws Exception {
        log.log(INFO, serviceName());
    }

    @Override
    protected void shutDown() throws Exception {
        log.log(INFO, serviceName());
    }

}
