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

import co.runrightfast.zest.composites.services.concurrent.ThreadFactoryService;
import co.runrightfast.zest.composites.services.concurrent.config.ThreadFactoryConfig;
import co.runrightfast.zest.composites.values.ApplicationModule;
import co.runrightfast.zest.composites.values.ApplicationModuleFactory;
import java.util.function.Function;
import lombok.NonNull;
import static org.apache.commons.lang3.Validate.noNullElements;
import static org.apache.commons.lang3.Validate.notEmpty;
import org.qi4j.bootstrap.ModuleAssembly;

/**
 * Groups assemblers for composites that every module should include as a base.
 *
 * @author alfio
 */
public interface BaseModuleAssemblers {

    /**
     * Base composeAssembler functions :
     * <ol>
     * <li>{@link #assembleApplicationModule(org.qi4j.bootstrap.ModuleAssembly) }
     * <li>{@link #assempleThreadFactoryService(org.qi4j.bootstrap.ModuleAssembly) }
     * </ol>
     *
     * @param assemblers composeAssembler chain - required
     * @return composed module composeAssembler function
     */
    static Function<ModuleAssembly, ModuleAssembly> composeAssemblerWithBaseAssemblers(final Function<ModuleAssembly, ModuleAssembly>... assemblers) {
        notEmpty(assemblers);
        noNullElements(assemblers);

        Function<ModuleAssembly, ModuleAssembly> chain = baseAssemblers();
        for (final Function<ModuleAssembly, ModuleAssembly> assembler : assemblers) {
            chain = chain.andThen(assembler);
        }
        return chain;
    }

    /**
     * Base assembler functions include :
     * <ol>
     * <li>{@link #assembleApplicationModule(org.qi4j.bootstrap.ModuleAssembly) }
     * <li>{@link #assempleThreadFactoryService(org.qi4j.bootstrap.ModuleAssembly) }
     * </ol>
     *
     * @return composed module composeAssembler function
     */
    static Function<ModuleAssembly, ModuleAssembly> baseAssemblers() {
        Function<ModuleAssembly, ModuleAssembly> chain = BaseModuleAssemblers::assembleApplicationModule;
        chain = chain.andThen(BaseModuleAssemblers::assempleThreadFactoryService);
        return chain;
    }

    /**
     * Adds the following value composites to the module with module scope visibility:
     * <ol>
     * <li>{@link ApplicationModule}
     * <li>{@link ApplicationModuleFactory}
     * </ol>
     *
     * Every module is expected to include these.
     *
     * @param module
     * @return ModuleAssembly
     */
    static ModuleAssembly assembleApplicationModule(@NonNull final ModuleAssembly module) {
        module.values(
                ApplicationModule.class,
                ApplicationModuleFactory.class
        );

        return module;
    }

    static ModuleAssembly assempleThreadFactoryService(@NonNull final ModuleAssembly module) {
        module.services(ThreadFactoryService.class);
        module.entities(ThreadFactoryConfig.class);
        return module;
    }
}
