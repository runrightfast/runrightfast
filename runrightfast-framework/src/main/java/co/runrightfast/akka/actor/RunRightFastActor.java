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
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * The design pattern is to delegate the message processing to a worker actor. This actor's role is to serve as the worker's supervisor and decide what to do in
 * case of a message processing exception.
 *
 * The additional benefit is that ActorSystem messages, e.g., Identify, will be processed by the supervisor immediately.
 *
 *
 * @author alfio
 * @param <WORKER> all messages are forwarded to the worker actor
 */
@Slf4j
public final class RunRightFastActor<WORKER extends Actor> extends UntypedActor {

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
        createWorker();
    }

    private void createWorker() {
        this.worker = context().watch(context().actorOf(workerProps, WORKER_ACTOR_NAME));
    }

    @Override
    public void onReceive(final Object msg) throws Exception {
        if (msg instanceof Terminated) {
            createWorker();
        } else {
            worker.forward(msg, context());
        }
    }

}
