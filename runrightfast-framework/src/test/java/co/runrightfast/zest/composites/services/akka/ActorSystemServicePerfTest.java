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
import org.qi4j.api.activation.ActivatorAdapter;
import org.qi4j.api.structure.Module;
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

    static final Object DONE = new Object();

    static class Worker extends UntypedActor {

        int msgReceivedCount;

        @Override
        public void onReceive(final Object msg) throws Exception {
            msgReceivedCount++;
            sender().tell(msg, self());
            if (msg instanceof Integer && ((Integer) msg) % 10000 == 0) {
                log.info("{} : {}", msg, msgReceivedCount);
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

    static ActorRef testActor;

    @Test
    public void perfTest() throws TimeoutException, InterruptedException {
        final ActorSystemService service = this.module.findService(ActorSystemService.class).get();

        assertThat(service, is(notNullValue()));
        assertThat(service.actorSystem(), is(notNullValue()));

        final ActorSystem actorSystem = service.actorSystem();

        final Inbox inbox = Inbox.create(actorSystem);
        final long startTime = System.currentTimeMillis();

        final ExecutorService inboxReceiveExecutor = Executors.newSingleThreadExecutor();
        inboxReceiveExecutor.execute(() -> {
            while (true) {
                try {
                    inbox.receive(Duration.create(250, TimeUnit.MILLISECONDS));
                } catch (final Exception ex) {
                    return;
                }
            }
        });

        final ExecutorService messageSenderExecutor = Executors.newWorkStealingPool(8);
        // messages sent will be : latchCount * 1 million
        final int latchCount = 1;
        final CountDownLatch latch = new CountDownLatch(latchCount);
        for (int i = 0; i < latchCount; i++) {
            messageSenderExecutor.execute(() -> {
                for (int ii = 0; ii < 1000000; ii++) {
                    testActor.tell(ii, inbox.getRef());
                }
                latch.countDown();
            });
        }

        latch.await();
        log.info("*** Sent all messages");

        final Inbox doneBox = Inbox.create(actorSystem);
        testActor.tell(DONE, doneBox.getRef());
        doneBox.receive(Duration.create(10, TimeUnit.SECONDS));
        final long endTime = System.currentTimeMillis();
        log.info("total time = {}", endTime - startTime);

        messageSenderExecutor.shutdown();
        inboxReceiveExecutor.shutdown();
    }

    @Override
    public void assemble(final ModuleAssembly assembly) throws AssemblyException {
        AkkaAssemblers.assembleActorSystem(assembly, getClass().getSimpleName());
        assembly.withActivators(TestSetup.class);
    }

    public static class TestSetup extends ActivatorAdapter<Module> {

        @Override
        public void afterActivation(final Module module) throws Exception {
            final ActorSystemService service = module.findService(ActorSystemService.class).get();
            final ActorSystem actorSystem = service.actorSystem();
            testActor = actorSystem.actorOf(Props.create(TestActor.class, () -> new TestActor()), TestActor.class.getSimpleName());
            log.info("created TestActor : {} : {}", testActor, testActor.path());
        }

    }

}
