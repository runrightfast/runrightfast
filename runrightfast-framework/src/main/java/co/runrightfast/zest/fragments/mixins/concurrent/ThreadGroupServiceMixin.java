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
package co.runrightfast.zest.fragments.mixins.concurrent;

import co.runrightfast.zest.composites.services.concurrent.ThreadGroupService;
import co.runrightfast.zest.composites.services.ApplicationModule;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.NonNull;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.structure.Application;

/**
 *
 * TODO: add ability to plug in a Thread.UncaughtExceptionHandler - subclass ThreadGroup to provide alternative handling of uncaught exceptions.
 *
 * @author alfio
 */
public class ThreadGroupServiceMixin implements ThreadGroupService {

    private final ThreadGroup applicationThreadGroup;
    private final Map<String, ThreadGroup> layerThreadGroups = new ConcurrentHashMap<>();
    private final Map<ApplicationModule, ThreadGroup> moduleThreadGroups = new ConcurrentHashMap<>();

    public ThreadGroupServiceMixin(@Structure final Application app) {
        this.applicationThreadGroup = new ThreadGroup(applicationThreadGroupName(app));
    }

    @Override
    public ThreadGroup getThreadGroup(@NonNull final ApplicationModule applicationModule) {
        return moduleThreadGroups.computeIfAbsent(applicationModule, (co.runrightfast.zest.composites.services.ApplicationModule key) -> {
            final ThreadGroup layerThreadGroup = layerThreadGroups.computeIfAbsent(key.layerName().get(), (java.lang.String layerName) -> {
                return new ThreadGroup(applicationThreadGroup, layerThreadGroupName(layerName));
            });
            return new ThreadGroup(layerThreadGroup, moduleThreadGroupName(layerThreadGroup, applicationModule));
        });
    }

    private String applicationThreadGroupName(final Application app) {
        return new StringBuilder(app.name().length() + app.version().length() + 2).append('/').append(app.name()).append('/').append(app.version()).toString();
    }

    private String layerThreadGroupName(final String layerName) {
        return threadGroupName(applicationThreadGroup, layerName);
    }

    private String moduleThreadGroupName(final ThreadGroup layer, final ApplicationModule applicationModule) {
        return threadGroupName(layer, applicationModule.moduleName().get());
    }

    private String threadGroupName(final ThreadGroup parent, final String name) {
        return new StringBuilder(parent.getName().length() + name.length() + 1).append(parent.getName()).append('/').append(name).toString();
    }

}
