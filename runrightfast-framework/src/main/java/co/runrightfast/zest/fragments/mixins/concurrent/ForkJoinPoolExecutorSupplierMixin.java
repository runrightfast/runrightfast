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

import co.runrightfast.zest.composites.services.concurrent.ExecutorSupplier;
import co.runrightfast.zest.composites.services.concurrent.config.ForkJoinPoolConfig;
import static com.google.common.base.Preconditions.checkState;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import lombok.extern.java.Log;
import org.qi4j.api.configuration.Configuration;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.service.ServiceActivation;

/**
 *
 * @author alfio
 */
@Log
public class ForkJoinPoolExecutorSupplierMixin implements ExecutorSupplier, ServiceActivation {

    private ExecutorService executorService;

    @This
    private Configuration<ForkJoinPoolConfig> config;

    @Override
    public Executor executor() {
        checkState(executorService != null, "service is not active");
        return executorService;
    }

    @Override
    public void activateService() throws Exception {
        if (executorService == null) {

        }
    }

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted.
     *
     * Tasks that are in the process of being submitted concurrently during the course of this method may or may not be rejected.
     *
     * @throws Exception
     */
    @Override
    public void passivateService() throws Exception {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

}
