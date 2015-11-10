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
package co.runrightfast.zest.composites.services.concurrent.reactor;

import co.runrightfast.exceptions.ConfigurationException;
import co.runrightfast.zest.assemblers.ApplicationCoreAssemblers;
import co.runrightfast.zest.assemblers.BaseModuleAssemblers;
import co.runrightfast.zest.assemblers.ModuleAssembler;
import co.runrightfast.zest.assemblers.ReactorAssemblers;
import static co.runrightfast.zest.composites.services.concurrent.reactor.WorkQueueDispatcherProviderTest.AppLayer.LAYER_1;
import static co.runrightfast.zest.composites.services.concurrent.reactor.WorkQueueDispatcherProviderTest.AppLayer.LAYER_2;
import static co.runrightfast.zest.composites.services.concurrent.reactor.WorkQueueDispatcherProviderTest.AppModule.CORE;
import static co.runrightfast.zest.composites.services.concurrent.reactor.WorkQueueDispatcherProviderTest.AppModule.MODULE_2;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qi4j.api.activation.ActivationException;
import org.qi4j.api.activation.PassivationException;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.service.ServiceReference;
import org.qi4j.api.structure.Application;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.Energy4Java;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreAssembler;
import reactor.Environment;
import reactor.core.Dispatcher;

/**
 *
 * @author alfio
 */
@Slf4j
public class WorkQueueDispatcherProviderTest {

    private Energy4Java qi4j;
    private Application app;

    enum AppLayer {

        LAYER_1,
        LAYER_2;

        public final String name;

        private AppLayer() {
            this.name = name().toLowerCase().replace('_', '-');
        }
    }

    enum AppModule {

        CORE,
        MODULE_2;

        public final String name;

        private AppModule() {
            this.name = name().toLowerCase().replace('_', '-');
        }
    }

    @Before
    public void setUp() {
        qi4j = new Energy4Java();
    }

    @After
    public void tearDown() throws PassivationException {
        if (app != null) {
            app.passivate();
        }
        app = null;
        qi4j = null;
    }

    @Test
    public void testRingBufferDispatcher() throws AssemblyException, ActivationException {
        assembleApp();

        final ServiceReference<RingBufferDispatcherProvider> coreRingBufferDispatcherProviderRef = app.findModule(LAYER_1.name, CORE.name).findService(RingBufferDispatcherProvider.class);
        log.info("core RingBufferDispatcherProvider identity: {}", coreRingBufferDispatcherProviderRef.identity());
        final RingBufferDispatcherProvider coreRingBufferDispatcherProvider = coreRingBufferDispatcherProviderRef.get();
        final Dispatcher coreRingBufferDispatcher = coreRingBufferDispatcherProvider.ringBufferDispatcher();
        assertThat(coreRingBufferDispatcher, is(Environment.sharedDispatcher()));
        coreRingBufferDispatcher.dispatch("CIAO", log::info, exception -> log.error("dispatch failed", exception));

        final ServiceReference<RingBufferDispatcherProvider> layer_1_module_2_RingBufferDispatcherProviderRef = app.findModule(LAYER_1.name, MODULE_2.name).findService(RingBufferDispatcherProvider.class);
        log.info("layer_1_module_2_RingBufferDispatcherProviderRef RingBufferDispatcherProvider identity: {}", layer_1_module_2_RingBufferDispatcherProviderRef.identity());
        final RingBufferDispatcherProvider layer_1_module_2_RingBufferDispatcherProvider = layer_1_module_2_RingBufferDispatcherProviderRef.get();
        final Dispatcher layer_1_module_2_RingBufferDispatcher = layer_1_module_2_RingBufferDispatcherProvider.ringBufferDispatcher();
        assertThat(layer_1_module_2_RingBufferDispatcher, is(not(Environment.sharedDispatcher())));
        assertThat(layer_1_module_2_RingBufferDispatcher.backlogSize(), is(1024L));

        final ServiceReference<RingBufferDispatcherProvider> layer_2_module_2_RingBufferDispatcherProviderRef = app.findModule(LAYER_2.name, MODULE_2.name).findService(RingBufferDispatcherProvider.class);
        log.info("layer_2_module_2_RingBufferDispatcherProviderRef RingBufferDispatcherProvider identity: {}", layer_2_module_2_RingBufferDispatcherProviderRef.identity());
        final RingBufferDispatcherProvider layer_2_module_2_RingBufferDispatcherProvider = layer_2_module_2_RingBufferDispatcherProviderRef.get();
        final Dispatcher layer_2_module_2_RingBufferDispatcher = layer_2_module_2_RingBufferDispatcherProvider.ringBufferDispatcher();
        assertThat(layer_2_module_2_RingBufferDispatcher, is(not(Environment.sharedDispatcher())));
        assertThat(layer_2_module_2_RingBufferDispatcher, is(not(layer_1_module_2_RingBufferDispatcher)));
        assertThat(layer_2_module_2_RingBufferDispatcher.backlogSize(), is(512L));

        final ServiceReference<WorkQueueDispatcherProvider> coreWorkQueueDispatcherProviderRef = app.findModule(LAYER_1.name, CORE.name).findService(WorkQueueDispatcherProvider.class);
        log.info("core coreWorkQueueDispatcherProviderRef identity: {}", coreRingBufferDispatcherProviderRef.identity());
        final WorkQueueDispatcherProvider coreWorkQueueDispatcherProvider = coreWorkQueueDispatcherProviderRef.get();
        final Dispatcher coreWorkQueueDispatcher = coreWorkQueueDispatcherProvider.workQueueDispatcher();
        assertThat(coreWorkQueueDispatcher, is(Environment.workDispatcher()));
        coreWorkQueueDispatcher.dispatch("CIAO", log::info, exception -> log.error("dispatch failed", exception));

        final ServiceReference<WorkQueueDispatcherProvider> layer_1_module_2_WorkQueueDispatcherProviderRef = app.findModule(LAYER_1.name, MODULE_2.name).findService(WorkQueueDispatcherProvider.class);
        log.info("core layer_1_module_2_WorkQueueDispatcherProviderRef identity: {}", layer_1_module_2_WorkQueueDispatcherProviderRef.identity());
        final WorkQueueDispatcherProvider layer_1_module_2_WorkQueueDispatcherProvider = layer_1_module_2_WorkQueueDispatcherProviderRef.get();
        final Dispatcher layer_1_module_2_WorkQueueDispatcher = layer_1_module_2_WorkQueueDispatcherProvider.workQueueDispatcher();
        assertThat(layer_1_module_2_WorkQueueDispatcher, is(not(Environment.workDispatcher())));
        layer_1_module_2_WorkQueueDispatcher.dispatch("CIAO", log::info, exception -> log.error("dispatch failed", exception));

        final ServiceReference<WorkQueueDispatcherProvider> layer_2_module_2_WorkQueueDispatcherProviderRef = app.findModule(LAYER_2.name, MODULE_2.name).findService(WorkQueueDispatcherProvider.class);
        log.info("core layer_2_module_2_WorkQueueDispatcherProviderRef identity: {}", layer_2_module_2_WorkQueueDispatcherProviderRef.identity());
        final WorkQueueDispatcherProvider layer_2_module_2_WorkQueueDispatcherProvider = layer_2_module_2_WorkQueueDispatcherProviderRef.get();
        final Dispatcher layer_2_module_2_WorkQueueDispatcher = layer_2_module_2_WorkQueueDispatcherProvider.workQueueDispatcher();
        assertThat(layer_2_module_2_WorkQueueDispatcher, is(not(Environment.workDispatcher())));
        assertThat(layer_2_module_2_WorkQueueDispatcher, is(not(layer_1_module_2_WorkQueueDispatcher)));
        layer_2_module_2_WorkQueueDispatcher.dispatch("CIAO", log::info, exception -> log.error("dispatch failed", exception));
    }

    private void assembleApp() throws AssemblyException, ActivationException {
        /*
         * <pre>
         *  LAYER_1
         *      CORE
         *      MODULE_2
         *  LAYER_2
         *      MODULE_2
         * </pre>
         */
        app = qi4j.newApplication(applicationFactory -> {
            final ApplicationAssembly assembly = applicationFactory.newApplicationAssembly();
            assembly.setName(getClass().getSimpleName());
            assembly.setVersion("1.0.1");

            final LayerAssembly layer_1 = assembly.layer(LAYER_1.name);
            ModuleAssembler.composeAssembler(
                    ApplicationCoreAssemblers.coreAssemblers(),
                    module -> {
                        final MemoryEntityStoreAssembler memoryEntityStoreAssembler = new MemoryEntityStoreAssembler();
                        memoryEntityStoreAssembler.visibleIn(Visibility.application);
                        try {
                            memoryEntityStoreAssembler.assemble(module);
                        } catch (final AssemblyException ex) {
                            log.error("MemoryEntityStoreAssembler failed", ex);
                            throw new ConfigurationException(ex);
                        }

                        return module;
                    }
            ).apply(layer_1.module(CORE.name));
            ModuleAssembler.composeAssembler(
                    BaseModuleAssemblers::assembleApplicationModule,
                    ReactorAssemblers::assembleModuleRingBufferDispatcherProvider,
                    ReactorAssemblers::assembleModuleWorkQueueDispatcherProvider
            ).apply(layer_1.module(MODULE_2.name));

            final LayerAssembly layer_2 = assembly.layer(LAYER_2.name);
            ModuleAssembler.composeAssembler(
                    BaseModuleAssemblers::assembleApplicationModule,
                    ReactorAssemblers::assembleModuleRingBufferDispatcherProvider,
                    ReactorAssemblers::assembleModuleWorkQueueDispatcherProvider
            ).apply(layer_2.module(MODULE_2.name));

            layer_2.uses(layer_1);

            return assembly;
        });

        app.activate();
    }

}
