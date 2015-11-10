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

import co.runrightfast.zest.composites.services.json.GsonProvider;
import co.runrightfast.zest.fragments.mixins.json.GsonValueSerialization;
import java.util.function.Function;
import lombok.NonNull;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.value.ValueSerialization;
import org.qi4j.bootstrap.ModuleAssembly;

/**
 *
 * @author alfio
 */
public interface ApplicationCoreAssemblers {

    /**
     * Base assembler functions include :
     * <ol>
     * <li>{@link #assembleJsonValueSerialization(org.qi4j.bootstrap.ModuleAssembly) }
     * <li>{@link #assembleDefaultGsonProvider(org.qi4j.bootstrap.ModuleAssembly) }
     * <li>{@link ReactorAssemblers#assembleDefaultReactorEnvironment(org.qi4j.bootstrap.ModuleAssembly) }
     * <li>{@link BaseModuleAssemblers#assembleApplicationModule(org.qi4j.bootstrap.ModuleAssembly) }
     * </ol>
     *
     * @return function for composing modules
     */
    static Function<ModuleAssembly, ModuleAssembly> coreAssemblers() {
        return ModuleAssembler.composeAssembler(
                ApplicationCoreAssemblers::assembleDefaultGsonProvider,
                ApplicationCoreAssemblers::assembleJsonValueSerialization,
                ReactorAssemblers::assembleDefaultReactorEnvironment,
                BaseModuleAssemblers::assembleApplicationModule
        );
    }

    static ModuleAssembly assembleJsonValueSerialization(@NonNull final ModuleAssembly module) {
        module.services(ValueSerialization.class)
                .withMixins(GsonValueSerialization.class)
                .taggedWith(ValueSerialization.Formats.JSON)
                .visibleIn(Visibility.application);
        return module;
    }

    static ModuleAssembly assembleDefaultGsonProvider(@NonNull final ModuleAssembly module) {
        module.services(GsonProvider.class)
                .withMixins(GsonProvider.DefaultGsonProviderMixin.class)
                .visibleIn(Visibility.application);
        return module;
    }

}
