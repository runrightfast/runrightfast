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
package co.runrightfast.zest.composites.values;

import co.runrightfast.zest.composites.services.ApplicationModuleFactory;
import co.runrightfast.zest.composites.services.ApplicationModule;
import static co.runrightfast.zest.assemblers.BaseModuleAssemblers.assembleApplicationModule;
import static co.runrightfast.zest.composites.values.ApplicationModuleFactoryTest.AppLayer.DOMAIN;
import static co.runrightfast.zest.composites.values.ApplicationModuleFactoryTest.AppLayer.INFRASTRUCTURE;
import static co.runrightfast.zest.composites.values.ApplicationModuleFactoryTest.AppModule.APP_MODULE;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qi4j.api.activation.ActivationException;
import org.qi4j.api.activation.PassivationException;
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
public class ApplicationModuleFactoryTest {

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

        APP_MODULE;

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
    public void testApplicationModule_singleLayer() throws AssemblyException, ActivationException {
        app = qi4j.newApplication(applicationFactory -> {
            final ApplicationAssembly assembly = applicationFactory.newApplicationAssembly();
            assembly.setName(getClass().getSimpleName());
            assembly.setVersion("1.0.1");
            final LayerAssembly infrastructure = createLayer(assembly, INFRASTRUCTURE);
            return assembly;
        });

        app.registerActivationEventListener(event -> log.info(event.toString()));

        app.activate();

        final Module module = app.findModule(INFRASTRUCTURE.name, APP_MODULE.name);
        final ApplicationModuleFactory appModuleFactory = module.findService(ApplicationModuleFactory.class).get();
        assertThat(appModuleFactory, is(notNullValue()));

        final ApplicationModule appModule = appModuleFactory.applicationModule();
        log.info(String.format("appModule : %s", appModule));
    }

    @Test
    public void testApplicationModule_multiLayers() throws AssemblyException, ActivationException {
        app = qi4j.newApplication(applicationFactory -> {
            final ApplicationAssembly assembly = applicationFactory.newApplicationAssembly();
            assembly.setName(getClass().getSimpleName());
            assembly.setVersion("1.0.1");
            final LayerAssembly infrastructure = createLayer(assembly, INFRASTRUCTURE);
            final LayerAssembly domain = createLayer(assembly, DOMAIN);

            domain.uses(infrastructure);

            return assembly;
        });

        app.registerActivationEventListener(event -> log.info(event.toString()));

        app.activate();

        final ApplicationModule domainAppModule = app.findModule(DOMAIN.name, APP_MODULE.name).findService(ApplicationModuleFactory.class).get().applicationModule();
        log.info(String.format("appModule : %s", domainAppModule));
        assertThat(domainAppModule.applicationName().get(), is(getClass().getSimpleName()));
        assertThat(domainAppModule.applicationVersion().get(), is(app.version()));
        assertThat(domainAppModule.layerName().get(), is(DOMAIN.name));
        assertThat(domainAppModule.moduleName().get(), is(APP_MODULE.name));

        final ApplicationModule infrastructureAppModule = app.findModule(INFRASTRUCTURE.name, APP_MODULE.name).findService(ApplicationModuleFactory.class).get().applicationModule();
        log.info(String.format("appModule : %s", infrastructureAppModule));
        assertThat(infrastructureAppModule.applicationName().get(), is(getClass().getSimpleName()));
        assertThat(infrastructureAppModule.applicationVersion().get(), is(app.version()));
        assertThat(infrastructureAppModule.layerName().get(), is(INFRASTRUCTURE.name));
        assertThat(infrastructureAppModule.moduleName().get(), is(APP_MODULE.name));
    }

    private LayerAssembly createLayer(final ApplicationAssembly assembly, final AppLayer appLayer) {
        final LayerAssembly layer = assembly.layer(appLayer.name);
        createAppModule(layer);
        return layer;
    }

    private void createAppModule(final LayerAssembly layer) {
        final ModuleAssembly module = layer.module(APP_MODULE.name);
        assembleApplicationModule(module);
    }

}
