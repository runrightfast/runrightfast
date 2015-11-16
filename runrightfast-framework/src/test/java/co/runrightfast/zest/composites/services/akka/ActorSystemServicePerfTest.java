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
import co.runrightfast.zest.assemblers.akka.AkkaAssemblers;
import com.typesafe.config.ConfigFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class ActorSystemServicePerfTest extends AbstractQi4jTest {

    static {
        ConfigFactory.invalidateCaches();
        System.setProperty("config.resource", String.format("/%s.conf", ActorSystemServicePerfTest.class.getSimpleName()));
    }

    static final Object DONE = new Object();

    static class Worker extends UntypedActor {

        @Override
        public void onReceive(final Object msg) throws Exception {
            sender().tell(msg, self());
            if (msg instanceof Integer && ((Integer) msg) % 10000 == 0) {
                log.info("{}", msg);
            }
        }

    }

    static class TestActor extends UntypedActor {

        private ActorRef worker;

        @Override
        public void preStart() throws Exception {
            super.preStart();

            this.worker = context().actorOf(Props.create(Worker.class, () -> new Worker()), Worker.class.getSimpleName());
        }

        @Override
        public void onReceive(final Object msg) throws Exception {
            // forwarding worker messages in order to be able to process system messages, e.g., the Identify message
            worker.forward(msg, context());
        }

    }

    /**
     * Send 1 million messages to Akka actor which simply replies with the message that was sent.
     *
     * @throws TimeoutException
     */
    @Test
    public void perfTest() throws TimeoutException, InterruptedException {
        final ActorSystemService service = this.module.findService(ActorSystemService.class).get();

        assertThat(service, is(notNullValue()));
        assertThat(service.actorSystem(), is(notNullValue()));

        final ActorSystem actorSystem = service.actorSystem();
        final ActorRef testActor = actorSystem.actorOf(Props.create(TestActor.class, () -> new TestActor()), TestActor.class.getSimpleName());

        final Inbox inbox = Inbox.create(actorSystem);
        final long startTime = System.currentTimeMillis();

        final ExecutorService executor = Executors.newWorkStealingPool(8);

        final int latchCount = 4;
        final CountDownLatch latch = new CountDownLatch(latchCount);
        for (int i = 0; i < latchCount; i++) {
            executor.execute(() -> {
                for (int ii = 0; ii < 1000000; ii++) {
                    testActor.tell(ii, inbox.getRef());
                }
                latch.countDown();
            });
        }

        executor.execute(() -> {
            while (true) {
                try {
                    inbox.receive(Duration.create(1, TimeUnit.SECONDS));
                } catch (final Exception ex) {
                    log.error("Inbox error", ex);
                    return;
                }
            }
        });

        latch.await();
        log.info("*** Sent all messages");

        final Inbox doneBox = Inbox.create(actorSystem);
        testActor.tell(DONE, doneBox.getRef());
        doneBox.receive(Duration.create(10, TimeUnit.SECONDS));
        final long endTime = System.currentTimeMillis();
        log.info("total time = {}", endTime - startTime);

        executor.shutdown();
    }

    @Override
    public void assemble(final ModuleAssembly assembly) throws AssemblyException {
        AkkaAssemblers.assembleActorSystem(assembly);
    }

}
