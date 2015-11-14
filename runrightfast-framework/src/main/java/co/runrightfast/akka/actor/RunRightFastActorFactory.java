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
package co.runrightfast.akka.actor;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.qi4j.api.structure.Module;

/**
 *
 * The worker actor is created by the Zest module.
 *
 * @author alfio
 * @param <A> worker actor type
 */
@RequiredArgsConstructor
public class RunRightFastActorFactory<A extends Actor> implements IndirectActorProducer {

    private final Class<A> workerClass;

    @NonNull
    private final Module module;

    @Override
    public Actor produce() {
        return module.newObject(workerClass);
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return RunRightFastActor.class;
    }

}
