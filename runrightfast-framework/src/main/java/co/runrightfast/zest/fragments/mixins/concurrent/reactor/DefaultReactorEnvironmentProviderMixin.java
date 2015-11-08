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
package co.runrightfast.zest.fragments.mixins.concurrent.reactor;

import co.runrightfast.zest.composites.services.concurrent.reactor.ReactorEnvironmentProvider;
import lombok.extern.slf4j.Slf4j;
import reactor.Environment;

/**
 *
 * @author alfio
 */
@Slf4j
public class DefaultReactorEnvironmentProviderMixin implements ReactorEnvironmentProvider {

    private Environment environment;

    @Override
    public Environment environment() {
        return this.environment;
    }

    @Override
    public void activateService() throws Exception {
        environment = Environment.initialize(uncaughtException -> {
            log.error("Uncaught exception from Reactor Environment", uncaughtException);
        });
    }

    @Override
    public void passivateService() throws Exception {
        environment.shutdown();
    }

}
