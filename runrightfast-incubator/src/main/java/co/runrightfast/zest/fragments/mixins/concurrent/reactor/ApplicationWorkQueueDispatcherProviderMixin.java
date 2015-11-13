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
package co.runrightfast.zest.fragments.mixins.concurrent.reactor;

import co.runrightfast.zest.composites.services.concurrent.reactor.ReactorEnvironment;
import co.runrightfast.zest.composites.services.concurrent.reactor.WorkQueueDispatcherProvider;
import org.qi4j.api.injection.scope.Service;
import reactor.Environment;
import reactor.core.Dispatcher;

/**
 *
 * @author alfio
 */
public class ApplicationWorkQueueDispatcherProviderMixin implements WorkQueueDispatcherProvider {

    private final Dispatcher dispatcher;

    /**
     *
     * @param env used to declare ${@link ReactorEnvironment} as a dependency to ensure the Reactor Environment is initialized
     */
    public ApplicationWorkQueueDispatcherProviderMixin(@Service final ReactorEnvironment env) {
        this.dispatcher = Environment.workDispatcher();
    }

    @Override
    public Dispatcher workQueueDispatcher() {
        return dispatcher;
    }

}
