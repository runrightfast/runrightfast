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

import co.runrightfast.zest.composites.services.ApplicationModule;
import co.runrightfast.zest.composites.services.ApplicationModuleFactory;
import lombok.NonNull;
import org.qi4j.bootstrap.ModuleAssembly;

/**
 * Groups assemblers for composites that every module should include as a base.
 *
 * @author alfio
 */
public interface BaseModuleAssemblers {

    /**
     * Adds the following value composites to the module with module scope visibility:
     * <ol>
     * <li>{@link ApplicationModule}
     * <li>{@link ApplicationModuleFactory}
     * </ol>
     *
     * Every module is expected to include these.
     *
     * @param module ModuleAssembly
     * @return ModuleAssembly
     */
    static ModuleAssembly assembleApplicationModule(@NonNull final ModuleAssembly module) {
        module.values(
                ApplicationModule.class
        );

        module.services(ApplicationModuleFactory.class);

        return module;
    }

}
