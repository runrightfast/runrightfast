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

import co.runrightfast.zest.composites.services.ApplicationModule;
import co.runrightfast.zest.composites.services.ApplicationModuleFactory;
import co.runrightfast.zest.composites.services.concurrent.reactor.ReactorEnvironment;
import co.runrightfast.zest.composites.services.concurrent.reactor.RingBufferDispatcherProvider;
import co.runrightfast.zest.composites.services.concurrent.reactor.config.RingBufferDispatcherConfig;
import org.qi4j.api.configuration.Configuration;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.service.ServiceActivation;
import reactor.Environment;
import reactor.core.Dispatcher;

/**
 * Used to provide a module specific dispatcher.
 *
 * @author alfio
 */
public class ModuleRingBufferDispatcherProviderMixin implements RingBufferDispatcherProvider, ServiceActivation {

    @Service
    private ReactorEnvironment env;

    @Service
    private ApplicationModuleFactory appModuleFactory;

    @This
    private Configuration<RingBufferDispatcherConfig> config;

    private Dispatcher dispatcher;

    private String dispatcherKey(final ApplicationModule appModule) {
        final String layerName = appModule.layerName().get();
        final String moduleName = appModule.moduleName().get();
        return new StringBuilder(layerName.length() + moduleName.length() + 2)
                .append('/').append(layerName)
                .append('/').append(moduleName)
                .toString();
    }

    @Override

    public Dispatcher ringBufferDispatcher() {
        return dispatcher;
    }

    @Override
    public void activateService() throws Exception {
        final String dispatcherKey = dispatcherKey(appModuleFactory.applicationModule());
        this.dispatcher = Environment.newDispatcher(dispatcherKey, RingBufferDispatcherConfig.backlog(config.get().backlog()));
    }

    @Override
    public void passivateService() {
    }

}
