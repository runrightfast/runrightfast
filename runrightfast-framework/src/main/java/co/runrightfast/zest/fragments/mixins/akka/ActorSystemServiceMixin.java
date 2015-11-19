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
package co.runrightfast.zest.fragments.mixins.akka;

import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Identify;
import akka.actor.Inbox;
import akka.actor.PoisonPill;
import akka.actor.Terminated;
import co.runrightfast.akka.AkkaUtils;
import static co.runrightfast.akka.AkkaUtils.USER;
import static co.runrightfast.akka.AkkaUtils.WILDCARD;
import static co.runrightfast.commons.utils.ConcurrentUtils.awaitCountdownLatchIgnoringInterruptedException;
import co.runrightfast.zest.composites.services.akka.ActorSystemService;
import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.qi4j.api.common.Optional;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.service.ServiceActivation;
import org.qi4j.api.structure.Application;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

/**
 * The Application name will be used as the ActorSystem's name.
 *
 * @author alfio
 */
@Slf4j
public abstract class ActorSystemServiceMixin implements ActorSystemService, ServiceActivation {

    private ActorSystem actorSystem;

    @Structure
    private Application application;

    @Optional
    @Service
    private ExecutionContext executionContext;

    @Override
    public ActorSystem actorSystem() {
        return actorSystem;
    }

    @Override
    public void activateService() throws Exception {
        final Config config = ConfigFactory.load(identity().get());
        if (executionContext != null) {
            this.actorSystem = ActorSystem.create(
                    application.name(),
                    config,
                    getClass().getClassLoader(),
                    executionContext
            );
        } else {
            this.actorSystem = ActorSystem.create(
                    application.name(),
                    config
            );
        }
    }

    /**
     * Each of the top level actors are watched for terminations. A PoisonPill is sent to each of the top level actors. After all top level actors have
     * terminated, then the actor system is terminated.
     *
     * @throws Exception if anything goes wrong
     */
    @Override
    public void passivateService() throws Exception {
        awaitTopLevelActorsTerminated(getTopLevelActors());
        terminateActorSystem();
    }

    private void awaitTopLevelActorsTerminated(final List<ActorRef> topLevelActors) {
        if (!topLevelActors.isEmpty()) {
            topLevelActors.forEach(actorRef -> actorRef.tell(PoisonPill.getInstance(), ActorRef.noSender()));

            final CountDownLatch latch = new CountDownLatch(topLevelActors.size());
            final Inbox inbox = Inbox.create(actorSystem);
            topLevelActors.forEach(actorRef -> {
                log.info("Waiting for top level actor to terminate : {}", actorRef.path());
                inbox.watch(actorRef);
            });
            new Thread(() -> {
                int totalWaitTimeSeconds = 0;
                while (true) {
                    try {
                        final Terminated terminated = (Terminated) inbox.receive(Duration.create(1, TimeUnit.SECONDS));
                        log.info("Actor is terminated : {}", terminated.actor().path());
                        latch.countDown();
                        if (latch.getCount() == 0) {
                            return;
                        }
                    } catch (final TimeoutException ex) {
                        log.warn("awaitTopLevelActorsTerminated() : total wait time is {} secs", ++totalWaitTimeSeconds);
                    }
                }
            }).start();

            awaitCountdownLatchIgnoringInterruptedException(latch, java.time.Duration.ofSeconds(10), "Waiting for top level actors to terminate");
        }
    }

    private void terminateActorSystem() {
        final CountDownLatch latch = new CountDownLatch(1);
        this.actorSystem.registerOnTermination(() -> latch.countDown());
        final String actorSystemName = this.actorSystem.name();
        this.actorSystem.terminate();
        awaitCountdownLatchIgnoringInterruptedException(latch, java.time.Duration.ofSeconds(10), String.format("Waiting for ActorSystem (%s) to terminate", actorSystemName));
        log.info("ActorSystem has terminated : {}", actorSystemName);
    }

    private List<ActorRef> getTopLevelActors() {
        final ActorSelection topLevelActorsSelection = this.actorSystem.actorSelection(AkkaUtils.actorPath(USER, WILDCARD));
        final Inbox inbox = Inbox.create(actorSystem);
        topLevelActorsSelection.tell(new Identify(Boolean.TRUE), inbox.getRef());

        final ImmutableList.Builder<ActorRef> actorRefs = ImmutableList.builder();
        while (true) {
            try {
                final ActorIdentity identity = (ActorIdentity) inbox.receive(Duration.create(1, TimeUnit.SECONDS));
                if (identity.ref().isEmpty()) {
                    return actorRefs.build();
                }
                final ActorRef actorRef = identity.getRef();
                log.info("top level actor : {}", actorRef.path());
                actorRefs.add(actorRef);
            } catch (final TimeoutException ex) {
                return actorRefs.build();
            }
        }
    }

}
