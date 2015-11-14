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
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import lombok.NonNull;

/**
 *
 *
 * The worker Actor should use the parent as the sender when sending messages.
 *
 * @author alfio
 * @param <WORKER> all messages are forwarded to the worker actor
 */
public class RunRightFastActor<WORKER extends Actor> extends UntypedActor {

    private final Props workerProps;

    private final SupervisorStrategy strategy;

    private ActorRef worker;

    public static final String WORKER_ACTOR_NAME = "worker";

    public RunRightFastActor(@NonNull final Props workerProps, @NonNull final SupervisorStrategy strategy) {
        this.workerProps = workerProps;
        this.strategy = strategy;
    }

    public RunRightFastActor(@NonNull final Props workerProps) {
        this(workerProps, SupervisorStrategy.defaultStrategy());
    }

    public RunRightFastActor(@NonNull final Class<WORKER> workerActorClass, @NonNull final Creator<WORKER> workerFactory) {
        this(workerActorClass, workerFactory, SupervisorStrategy.defaultStrategy());
    }

    public RunRightFastActor(@NonNull final Class<WORKER> workerActorClass, @NonNull final Creator<WORKER> workerFactory, @NonNull final SupervisorStrategy strategy) {
        this(Props.create(workerActorClass, workerFactory), strategy);
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.worker = context().watch(context().actorOf(workerProps, WORKER_ACTOR_NAME));
    }

    @Override
    public void onReceive(final Object msg) throws Exception {
        worker.forward(msg, context());
    }

}
