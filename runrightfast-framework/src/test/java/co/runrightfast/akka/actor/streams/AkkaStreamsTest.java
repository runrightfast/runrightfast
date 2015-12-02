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
package co.runrightfast.akka.actor.streams;

import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.stream.ActorMaterializer;
import akka.stream.ClosedShape;
import akka.stream.FanInShape2;
import akka.stream.Graph;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.FlowGraph;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.ZipWith;
import co.runrightfast.commons.utils.JsonUtils;
import static co.runrightfast.zest.assemblers.akka.AkkaAssemblers.assembleActorSystem;
import co.runrightfast.zest.composites.services.akka.ActorSystemService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.qi4j.api.service.ServiceReference;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

/**
 *
 * @author alfio
 */
@Slf4j
public class AkkaStreamsTest extends AbstractQi4jTest {

    @Test
    public void test() throws InterruptedException {
        final ServiceReference<ActorSystemService> serviceRef = module.findService(ActorSystemService.class);
        final ActorSystem actorSystem = serviceRef.get().actorSystem();
        final Materializer mat = ActorMaterializer.create(actorSystem);

        final FiniteDuration oneSecond = FiniteDuration.create(10, TimeUnit.MILLISECONDS);
        final Source<String, Cancellable> msgSource = Source.tick(oneSecond, oneSecond, "message!").named("message");
        final Source<String, Cancellable> tickSource = Source.tick(oneSecond.mul(3), oneSecond.mul(3), "tick").named("tick");
        final Flow<String, Integer, BoxedUnit> conflate = Flow.of(String.class).conflate(first -> 1, (count, elem) -> count + 1);

        final Graph<ClosedShape, BoxedUnit> graph = FlowGraph.<ClosedShape>create(b -> {
            final FanInShape2<String, Integer, Integer> zipper = b.add(ZipWith.create((String tick, Integer count) -> count));
            b.from(b.add(msgSource)).via(b.add(conflate)).toInlet(zipper.in1());
            b.from(b.add(tickSource)).toInlet(zipper.in0());
            b.from(zipper.out()).to(b.add(Sink.<Integer>foreach(elem -> log.info("{}", elem)).named("logger")));
            return ClosedShape.getInstance();
        }).named("logging-zipper");
        logShapeInfo(graph);

        final RunnableGraph<BoxedUnit> runnableGraph = RunnableGraph.fromGraph(graph);
        runnableGraph.run(mat);

        Thread.sleep(1000L * 1);
    }

    private void logShapeInfo(final Graph graph) {
        final JsonObject json = new JsonObject();
        final JsonArray inlets = new JsonArray();
        json.add("inlets", inlets);
        final JsonArray outlets = new JsonArray();
        json.add("outlets", outlets);
        graph.shape().getInlets().forEach(inlet -> inlets.add(inlet.toString()));
        graph.shape().getOutlets().forEach(outlet -> outlets.add(outlet.toString()));
        log.info("shape inlets and outlets:\n{}", JsonUtils.prettyPrintingGson.toJson(json));
    }

    @Override
    public void assemble(ModuleAssembly assembly) throws AssemblyException {
        assembleActorSystem(assembly);
    }

}
