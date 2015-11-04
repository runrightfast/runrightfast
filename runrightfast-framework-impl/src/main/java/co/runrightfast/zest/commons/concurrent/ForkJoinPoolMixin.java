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
package co.runrightfast.zest.commons.concurrent;

import co.runrightfast.zest.composites.services.concurrent.config.ForkJoinPoolConfig;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import org.qi4j.api.configuration.Configuration;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.service.ServiceActivation;

/**
 *
 * @author alfio
 */
public class ForkJoinPoolMixin implements ServiceActivation, Supplier<Executor> {

    @This
    private Configuration<ForkJoinPoolConfig> config;

    private ExecutorService executor;

    @Override
    public void activateService() throws Exception {

    }

    @Override
    public void passivateService() throws Exception {

    }

    @Override
    public Executor get() {
        return executor;
    }
}
