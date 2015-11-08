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

import co.runrightfast.zest.composites.services.concurrent.reactor.ReactorEnvironmentProvider;
import co.runrightfast.zest.fragments.mixins.concurrent.reactor.DefaultReactorEnvironmentProviderMixin;
import lombok.NonNull;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ModuleAssembly;

/**
 *
 * @author alfio
 */
public interface ReactorAssemblers {

    static ModuleAssembly assembleDefaultReactorEnvironment(@NonNull final ModuleAssembly module) {
        module.services(ReactorEnvironmentProvider.class)
                .withMixins(DefaultReactorEnvironmentProviderMixin.class)
                .visibleIn(Visibility.application);
        return module;
    }

}
