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
package co.runrightfast.zest.assemblers;

import co.runrightfast.zest.composites.services.concurrent.reactor.ReactorEnvironment;
import co.runrightfast.zest.composites.services.concurrent.reactor.RingBufferDispatcherProvider;
import co.runrightfast.zest.composites.services.concurrent.reactor.WorkQueueDispatcherProvider;
import co.runrightfast.zest.composites.services.concurrent.reactor.config.RingBufferDispatcherConfig;
import co.runrightfast.zest.composites.services.concurrent.reactor.config.WorkQueueDispatcherConfig;
import co.runrightfast.zest.fragments.mixins.concurrent.reactor.ApplicationRingBufferDispatcherProviderMixin;
import co.runrightfast.zest.fragments.mixins.concurrent.reactor.ApplicationWorkQueueDispatcherProviderMixin;
import co.runrightfast.zest.fragments.mixins.concurrent.reactor.DefaultReactorEnvironmentMixin;
import co.runrightfast.zest.fragments.mixins.concurrent.reactor.ModuleRingBufferDispatcherProviderMixin;
import co.runrightfast.zest.fragments.mixins.concurrent.reactor.ModuleWorkQueueDispatcherProviderMixin;
import lombok.NonNull;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ModuleAssembly;

/**
 *
 * @author alfio
 */
public interface ReactorAssemblers {

    static ModuleAssembly assembleDefaultReactorEnvironment(@NonNull final ModuleAssembly module) {
        module.services(ReactorEnvironment.class)
                .withMixins(DefaultReactorEnvironmentMixin.class)
                .visibleIn(Visibility.application)
                .instantiateOnStartup();

        module.services(RingBufferDispatcherProvider.class)
                .withMixins(ApplicationRingBufferDispatcherProviderMixin.class)
                .visibleIn(Visibility.application);

        module.services(WorkQueueDispatcherProvider.class)
                .withMixins(ApplicationWorkQueueDispatcherProviderMixin.class)
                .visibleIn(Visibility.application);

        return module;
    }

    /**
     *
     * The {@link RingBufferDispatcherProvider} service identity will be: {RingBufferDispatcherProvider}-{layer-name}-{module-name}
     *
     * @param module ModuleAssembly
     * @return ModuleAssembly
     */
    static ModuleAssembly assembleModuleRingBufferDispatcherProvider(@NonNull final ModuleAssembly module) {
        final String serviceName = String.format("%s-%s-%s", RingBufferDispatcherProvider.class.getSimpleName(), module.layer().name(), module.name());
        module.services(RingBufferDispatcherProvider.class)
                .withMixins(ModuleRingBufferDispatcherProviderMixin.class)
                .identifiedBy(serviceName);
        module.entities(RingBufferDispatcherConfig.class);
        return module;
    }

    /**
     *
     * The {@link WorkQueueDispatcherProvider} service identity will be: {WorkQueueDispatcherProvider}-{layer-name}-{module-name}
     *
     * @param module ModuleAssembly
     * @return ModuleAssembly
     */
    static ModuleAssembly assembleModuleWorkQueueDispatcherProvider(@NonNull final ModuleAssembly module) {
        final String serviceName = String.format("%s-%s-%s", WorkQueueDispatcherProvider.class.getSimpleName(), module.layer().name(), module.name());
        module.services(WorkQueueDispatcherProvider.class)
                .withMixins(ModuleWorkQueueDispatcherProviderMixin.class)
                .identifiedBy(serviceName);
        module.entities(WorkQueueDispatcherConfig.class);
        return module;
    }

}
