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

import co.runrightfast.zest.composites.values.ApplicationModuleFactory.ApplicationModuleSupplierMixin;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.structure.Application;
import org.qi4j.api.structure.Layer;
import org.qi4j.api.structure.Module;
import org.qi4j.api.value.ValueBuilder;

/**
 *
 * @author alfio
 */
@Mixins({ApplicationModuleSupplierMixin.class})
public interface ApplicationModuleFactory {

    ApplicationModule applicationModule();

    public static class ApplicationModuleSupplierMixin implements ApplicationModuleFactory {

        private final ApplicationModule applicationModule;

        public ApplicationModuleSupplierMixin(@Structure final Application app, @Structure final Layer layer, @Structure final Module module) {
            final ValueBuilder<ApplicationModule> builder = module.newValueBuilder(ApplicationModule.class);
            final ApplicationModule prototype = builder.prototypeFor(ApplicationModule.class);
            prototype.applicationName().set(app.name());
            prototype.applicationVersion().set(app.version());
            prototype.layerName().set(layer.name());
            prototype.moduleName().set(module.name());
            this.applicationModule = builder.newInstance();
        }

        @Override
        public ApplicationModule applicationModule() {
            return applicationModule;
        }

    }
}
