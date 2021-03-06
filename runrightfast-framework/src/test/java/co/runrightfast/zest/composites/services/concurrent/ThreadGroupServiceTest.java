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
package co.runrightfast.zest.composites.services.concurrent;

import co.runrightfast.zest.assemblers.BaseModuleAssemblers;
import static co.runrightfast.zest.assemblers.BaseModuleAssemblers.assembleApplicationModule;
import co.runrightfast.zest.assemblers.ConcurrentAssemblers;
import co.runrightfast.zest.assemblers.ModuleAssembler;
import static co.runrightfast.zest.composites.services.concurrent.ThreadGroupServiceTest.AppLayer.DOMAIN;
import static co.runrightfast.zest.composites.services.concurrent.ThreadGroupServiceTest.AppLayer.INFRASTRUCTURE;
import static co.runrightfast.zest.composites.services.concurrent.ThreadGroupServiceTest.AppModule.CORE;
import static co.runrightfast.zest.composites.services.concurrent.ThreadGroupServiceTest.AppModule.MODULE_1;
import static co.runrightfast.zest.composites.services.concurrent.ThreadGroupServiceTest.AppModule.MODULE_2;
import co.runrightfast.zest.composites.services.ApplicationModule;
import co.runrightfast.zest.composites.services.ApplicationModuleFactory;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qi4j.api.activation.ActivationException;
import org.qi4j.api.activation.PassivationException;
import org.qi4j.api.service.ServiceReference;
import org.qi4j.api.structure.Application;
import org.qi4j.api.structure.Module;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.Energy4Java;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;

/**
 *
 * @author alfio
 */
@Log
public class ThreadGroupServiceTest {

    private Energy4Java qi4j;
    private Application app;

    enum AppLayer {

        INFRASTRUCTURE,
        DOMAIN;

        public final String name;

        private AppLayer() {
            this.name = name().toLowerCase().replace('_', '-');
        }
    }

    enum AppModule {

        MODULE_1,
        MODULE_2,
        CORE;

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
    public void testGetThreadGroup() throws AssemblyException, ActivationException {
        /*
         * <pre>
         *  INFRASTRUCTURE
         *      CORE
         *      MODULE_1
         *      MODULE_2
         *  DOMAIN
         *      MODULE_1
         *      MODULE_2
         * </pre>
         */
        app = qi4j.newApplication(applicationFactory -> {
            final ApplicationAssembly assembly = applicationFactory.newApplicationAssembly();
            assembly.setName(getClass().getSimpleName());
            assembly.setVersion("1.0.1");

            final LayerAssembly infrastructure = assembly.layer(INFRASTRUCTURE.name);
            assembleCoreModule(infrastructure, CORE);
            assembleAppModule(infrastructure, MODULE_1);
            assembleAppModule(infrastructure, MODULE_2);

            final LayerAssembly domain = assembly.layer(DOMAIN.name);
            assembleAppModule(domain, MODULE_1);
            assembleAppModule(domain, MODULE_2);

            domain.uses(infrastructure);

            return assembly;
        });

        app.registerActivationEventListener(event -> log.info(event.toString()));

        app.activate();

        checkModuleAssertions(app.findModule(INFRASTRUCTURE.name, CORE.name));
        checkModuleAssertions(app.findModule(INFRASTRUCTURE.name, MODULE_1.name));
        checkModuleAssertions(app.findModule(INFRASTRUCTURE.name, MODULE_2.name));
        checkModuleAssertions(app.findModule(DOMAIN.name, MODULE_1.name));
        checkModuleAssertions(app.findModule(DOMAIN.name, MODULE_2.name));
    }

    private void checkModuleAssertions(final Module module) {
        final ServiceReference<ThreadGroupService> threadGroupServiceRef = module.findService(ThreadGroupService.class);
        log.info(String.format("service identity = %s", threadGroupServiceRef.identity()));
        assertThat(threadGroupServiceRef.isAvailable(), is(true));
        final ThreadGroupService service = threadGroupServiceRef.get();

        final ApplicationModule moduleAppModule = module.findService(ApplicationModuleFactory.class).get().applicationModule();
        final ThreadGroup module1ThreadGroup = service.getThreadGroup(moduleAppModule);
        log.info(String.format("module1ThreadGroup : %s", module1ThreadGroup.getName()));
        assertThat(module1ThreadGroup.getName(), is(String.format("/%s/%s/%s/%s", moduleAppModule.applicationName(), moduleAppModule.applicationVersion(), moduleAppModule.layerName(), moduleAppModule.moduleName())));

        // same instance should be returned every time
        assertThat(service.getThreadGroup(moduleAppModule) == service.getThreadGroup(moduleAppModule), is(true));

        final ThreadGroup layerThreadGroup = module1ThreadGroup.getParent();
        assertThat(layerThreadGroup.getName(), is(String.format("/%s/%s/%s", moduleAppModule.applicationName(), moduleAppModule.applicationVersion(), moduleAppModule.layerName())));

        final ThreadGroup appThreadGroup = layerThreadGroup.getParent();
        assertThat(appThreadGroup.getName(), is(String.format("/%s/%s", moduleAppModule.applicationName(), moduleAppModule.applicationVersion())));
    }

    private void assembleAppModule(final LayerAssembly layer, final AppModule appModule) {
        final ModuleAssembly module = layer.module(appModule.name);
        assembleApplicationModule(module);
    }

    private void assembleCoreModule(final LayerAssembly layer, final AppModule appModule) {
        ModuleAssembler.composeAssembler(
                BaseModuleAssemblers::assembleApplicationModule,
                ConcurrentAssemblers::assembleThreadGroupService
        ).apply(layer.module(appModule.name));
    }

}
