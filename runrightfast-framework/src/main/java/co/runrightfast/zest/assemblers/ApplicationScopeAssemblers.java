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

import co.runrightfast.exceptions.ConfigurationException;
import co.runrightfast.zest.composites.services.concurrent.ThreadGroupService;
import lombok.NonNull;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.valueserialization.orgjson.OrgJsonValueSerializationAssembler;

/**
 *
 * @author alfio
 */
public interface ApplicationScopeAssemblers {

    /**
     * Adds the following service composites to the module with application scope visibility:
     * <ol>
     * <li>{@link ThreadGroupService}
     * </ol>
     *
     * Should be created as a singleton within a runtime layer that is visible by all other layers in the application.
     *
     * @param module
     * @return ModuleAssembly
     */
    static ModuleAssembly assembleThreadGroupService(@NonNull final ModuleAssembly module) {
        module.services(ThreadGroupService.class).visibleIn(Visibility.application);
        return module;
    }

    static ModuleAssembly assembleOrgJsonValueSerialization(@NonNull final ModuleAssembly module) {
        final OrgJsonValueSerializationAssembler assembler = new OrgJsonValueSerializationAssembler();
        assembler.visibleIn(Visibility.application);
        try {
            assembler.assemble(module);
        } catch (final AssemblyException ex) {
            throw new ConfigurationException(ex);
        }
        return module;
    }

}
