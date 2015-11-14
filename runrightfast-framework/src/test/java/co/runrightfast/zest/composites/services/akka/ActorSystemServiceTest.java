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
package co.runrightfast.zest.composites.services.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.UntypedActor;
import static co.runrightfast.zest.assemblers.akka.AkkaAssemblers.assembleActorSystem;
import com.typesafe.config.ConfigFactory;
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
public class ActorSystemServiceTest extends AbstractQi4jTest {

    static {
        ConfigFactory.invalidateCaches();
        System.setProperty("config.resource", String.format("/%s.conf", ActorSystemServiceTest.class.getSimpleName()));
    }

    static class EchoMessageActor extends UntypedActor {

        @Override
        public void onReceive(final Object msg) throws Exception {
            log.info("Received message : {}", msg);
            sender().tell(msg, self());
        }

    }

    @Test
    public void testActorSystem() throws TimeoutException {
        final ActorSystemService service = this.module.findService(ActorSystemService.class).get();
        assertThat(service, is(notNullValue()));
        assertThat(service.actorSystem(), is(notNullValue()));

        final ActorSystem actorSystem = service.actorSystem();
        final ActorRef echoMessageActor = actorSystem.actorOf(Props.create(EchoMessageActor.class, () -> new EchoMessageActor()), EchoMessageActor.class.getSimpleName());
        final Inbox inbox = Inbox.create(actorSystem);
        final Object msg = "CIAO COMPARI !!!";
        echoMessageActor.tell(msg, inbox.getRef());
        final Object reply = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
        assertThat(reply, is(notNullValue()));
        assertThat(reply, is(msg));

        for (int i = 0; i < 100; i++) {
            echoMessageActor.tell("MSG #" + i, inbox.getRef());
        }

        for (int i = 0; i < 100; i++) {
            final Object msgReply = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            assertThat(msgReply, is(notNullValue()));
            log.info("Received reply: {}", msgReply);
        }
    }

    @Override
    public void assemble(final ModuleAssembly assembly) throws AssemblyException {
        assembleActorSystem(assembly);
    }

}
