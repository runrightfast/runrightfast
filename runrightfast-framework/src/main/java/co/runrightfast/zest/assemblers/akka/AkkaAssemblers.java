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
package co.runrightfast.zest.assemblers.akka;

import co.runrightfast.zest.composites.services.akka.ActorSystemService;
import co.runrightfast.zest.fragments.mixins.akka.ActorSystemServiceMixin;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.service.ServiceActivation;
import org.qi4j.bootstrap.ModuleAssembly;

/**
 *
 * @author alfio
 */
public interface AkkaAssemblers {

    /**
     * delegates to {@link AkkaAssemblers#assembleActorSystem(org.qi4j.bootstrap.ModuleAssembly, java.lang.String) }
     * using "application" as the actorSystemName.
     *
     * @param module module
     * @return ModuleAssembly
     */
    static ModuleAssembly assembleActorSystem(final ModuleAssembly module) {
        return assembleActorSystem(module, "application");
    }

    static ModuleAssembly assembleActorSystem(final ModuleAssembly module, final String actorSystemName) {
        module.services(ActorSystemService.class)
                .withMixins(ActorSystemServiceMixin.class)
                .withTypes(ServiceActivation.class)
                .visibleIn(Visibility.application)
                .instantiateOnStartup()
                .identifiedBy(actorSystemName);

        return module;
    }

}
