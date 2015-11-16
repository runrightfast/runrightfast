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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import static co.runrightfast.zest.assemblers.akka.AkkaAssemblers.assembleActorSystem;
import co.runrightfast.zest.composites.services.akka.ActorSystemService;
import co.runrightfast.zest.composites.services.akka.ActorSystemServiceTest;
import com.typesafe.config.ConfigFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;
import scala.concurrent.duration.Duration;

/**
 *
 * @author alfio
 */
@Slf4j
public class RunRightFastActorTest extends AbstractQi4jTest {

    static {
        ConfigFactory.invalidateCaches();
        System.setProperty("config.resource", String.format("/%s.conf", ActorSystemServiceTest.class.getSimpleName()));
    }

    static class Worker extends WorkerActor {

        @Override
        protected void processMessage(Object msg) throws Exception {
            if (msg instanceof String) {
                log.info("Received message : {}", msg);
                sender().tell(msg, self());
            } else if (msg instanceof Long) {
                log.info("Sleeping for {} msec", msg);
                Thread.sleep((Long) msg);
                log.info("Awake after sleeping for {} msec", msg);
            } else {
                unhandled(msg);
            }
        }

    }

    @Test
    public void testActorSystem() throws TimeoutException {
        final ActorSystemService service = this.module.findService(ActorSystemService.class).get();
        assertThat(service, is(notNullValue()));
        assertThat(service.actorSystem(), is(notNullValue()));

        final ActorSystem actorSystem = service.actorSystem();
        final ActorRef testActor = actorSystem.actorOf(Props.create(RunRightFastActor.class, () -> new RunRightFastActor(Worker.class, () -> new Worker())), RunRightFastActor.class.getSimpleName());
        final Inbox inbox = Inbox.create(actorSystem);
        final Object msg = "CIAO COMPARI !!!";
        testActor.tell(msg, inbox.getRef());
        final Object reply = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
        assertThat(reply, is(notNullValue()));
        assertThat(reply, is(msg));

        for (int i = 0; i < 100; i++) {
            testActor.tell("MSG #" + i, inbox.getRef());
        }

        for (int i = 0; i < 100; i++) {
            final Object msgReply = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            assertThat(msgReply, is(notNullValue()));
            log.info("Received reply: {}", msgReply);
        }

        testActor.tell(java.time.Duration.ofSeconds(2).toMillis(), ActorRef.noSender());
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // expecting this message to not be delivered because it will sent after the poison pill when the ActorSystemService is shutting down
                testActor.tell("DROPPED MESSAGE", ActorRef.noSender());
            }
        }, 1000L);
    }

    @Override
    public void assemble(final ModuleAssembly assembly) throws AssemblyException {
        assembleActorSystem(assembly);
    }

}
